package com.newcart.newpariscart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.newcart.newpariscart.Common.Common;
import com.newcart.newpariscart.Common.Config;
import com.newcart.newpariscart.Database.Database;
import com.newcart.newpariscart.Model.FCMResponse;
import com.newcart.newpariscart.Model.FCMSendData;
import com.newcart.newpariscart.Model.Food;
import com.newcart.newpariscart.Model.Order;
import com.newcart.newpariscart.Model.Request;
import com.newcart.newpariscart.Model.Restaurant;
import com.newcart.newpariscart.Model.TokenModel;
import com.newcart.newpariscart.Remote.IFCMService;
import com.newcart.newpariscart.Remote.RetrofitFCMClient;
import com.newcart.newpariscart.SquareUp.CheckoutActivity;
import com.newcart.newpariscart.ViewHolder.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public TextView txtTotalPrice;
    Button btnPlace;
    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    EditText comment;

    ImageView pimg, cart_back;
    TextView pname, palterar;

    FirebaseDatabase database;
    DatabaseReference requests;

    String getRID = "";
    String myRes = "";

    Restaurant ress;

    double total = 0;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    IFCMService ifcmService;
    String paym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);

        pimg = findViewById(R.id.cart_pimg);
        pname = findViewById(R.id.cart_pname);
        palterar = findViewById(R.id.cart_palterar);
        cart_back = findViewById(R.id.cart_back);

        //Init
        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        cart_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        paym = pref.getString("paymentWay", null);

        if (paym.equals("Dinheiro")){
            pimg.setImageDrawable(getResources().getDrawable(R.drawable.money_bill));
            pname.setText("Dinheiro");
        }else if (paym.equals("Maquina")){
            pimg.setImageDrawable(getResources().getDrawable(R.drawable.maquinacar));
            pname.setText("Máquina de Cartão");
        }else if (paym.equals("Caixa")){
            pimg.setImageDrawable(getResources().getDrawable(R.drawable.maquinacar));
            pname.setText("Cartão Virtual Caixa");
        }else if (paym.equals("PicPay")){
            pimg.setImageDrawable(getResources().getDrawable(R.drawable.maquinacar));
            pname.setText("PicPay");
        }else if (paym.equals("Cripto")){
            pimg.setImageDrawable(getResources().getDrawable(R.drawable.maquinacar));
            pname.setText("Criptomoeda (10%)");
        }

        palterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cart.this,PagamentoActivity.class);
                startActivity(intent);
            }
        });

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.size() > 0)
                    //showAlertDialog();
                    Toast.makeText(Cart.this, "AAA", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Cart.this, "Seu carrinho está vazio", Toast.LENGTH_SHORT).show();
            }
        });

        loadListFood();

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        cart.remove(position);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        new Database(this).cleanCart();
        for (Order item : cart) {
            new Database(this).addToCart(item);
        }
        loadListFood();
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        for (final Order order : cart){
            total += (Double.parseDouble(order.getPrice())) * (Double.parseDouble(order.getQuantity()));
            final String pId = order.getProductId();

            final FirebaseDatabase dt = FirebaseDatabase.getInstance();
            DatabaseReference dr = dt.getReference("Pratos");

            dr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(pId).exists()){
                        Food user = dataSnapshot.child(pId).getValue(Food.class);

                        getRID = user.getRestId();

                        DatabaseReference dp = dt.getReference("Restaurants");

                        dp.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(getRID).exists()){
                                    ress = dataSnapshot.child(getRID).getValue(Restaurant.class);

                                    myRes = ress.getEntrega();
                                    final String myDisc = Common.currentUser.getSaldo();

                                    Locale locale = new Locale("pt", "BR");
                                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                                    //txtTotalPrice.setText(fmt.format(total));

                                    TextView sub, tax, ofctot, address;

                                    address = findViewById(R.id.tab2_edt_address);
                                    address.setText(Common.currentUser.getAddress());
                                    comment = findViewById(R.id.tab2_edt_comments);

                                    sub = findViewById(R.id.subtotal);
                                    tax = findViewById(R.id.taxprice);
                                    ofctot = findViewById(R.id.ofcprice);

                                    sub.setText(fmt.format(total));
                                    tax.setText("R$"+myRes);
                                    double subcc = Double.parseDouble(myRes.replace(",","."));
                                    double discc = Double.parseDouble(myDisc.replace(",","."));

                                    double nowOK = (subcc + total) - discc;
                                    if (paym.equals("Cripto") && nowOK != 0){
                                        nowOK = (nowOK*9)/10;
                                    }
                                    ofctot.setText("R$"+String.valueOf(nowOK).replace(",","."));
                                    txtTotalPrice.setText("R$"+String.valueOf(nowOK).replace(",","."));

                                    btnPlace.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View view) {

                                            AlertDialog alerta;

                                            AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
                                            //define o titulo
                                            builder.setTitle("Pagamento");
                                            //define a mensagem
                                            builder.setMessage("Escolha a forma de pagamento");
                                            //define um botão como positivo
                                            builder.setPositiveButton("Cartão", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                    //TODO: CARD
                                                    Intent intent = new Intent(Cart.this, CheckoutActivity.class);
                                                    startActivity(intent);

                                                }
                                            });
                                            //define um botão como negativo.
                                            builder.setNegativeButton("Dinheiro", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface arg0, int arg1) {


                                                    if (!Common.currentUser.getCpf().equals("")){

                                                        Request request = new Request(
                                                                Common.currentUser.getPhoneNumber(),
                                                                Common.currentUser.getName(),
                                                                Common.currentUser.getAddress(),
                                                                txtTotalPrice.getText().toString()+"0".replace(".",","),
                                                                "0",
                                                                comment.getText().toString(),
                                                                ress.getRestId(),
                                                                cart
                                                        );

                                                        String order_number = String.valueOf(System.currentTimeMillis());
                                                        requests.child(order_number).setValue(request);

                                                        FirebaseDatabase.getInstance()
                                                                .getReference(Common.TOKEN_REF)
                                                                .child(request.getRestaurant())
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()){
                                                                            //TODO: NOTIFICATIONS
                                                                            TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);
                                                                            Map<String,String> notiData = new HashMap<>();
                                                                            notiData.put(Common.NOTI_TITLE,"ParisCart - Novo Pedido");
                                                                            notiData.put(Common.NOTI_CONTENT,"Você tem um pedido de "+Common.currentUser.getName());

                                                                            FCMSendData sendData = new FCMSendData(tokenModel.getToken(),notiData);

                                                                            compositeDisposable.add(ifcmService.sendNotification(sendData)
                                                                                    .subscribeOn(Schedulers.io())
                                                                                    .observeOn(AndroidSchedulers.mainThread())
                                                                                    .subscribe(new Consumer<FCMResponse>() {
                                                                                                   @Override
                                                                                                   public void accept(FCMResponse fcmResponse) throws Exception {
                                                                                                       if (fcmResponse.getSuccess() == 1){
                                                                                                           Toast.makeText(Cart.this, "Pedido Feito!", Toast.LENGTH_SHORT).show();
                                                                                                       }else{
                                                                                                           Toast.makeText(Cart.this, "Falha ao Notificar Restaurante", Toast.LENGTH_SHORT).show();
                                                                                                       }

                                                                                                   }
                                                                                               }, new Consumer<Throwable>() {
                                                                                                   @Override
                                                                                                   public void accept(Throwable throwable) throws Exception {
                                                                                                       Toast.makeText(Cart.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                   }
                                                                                               }
                                                                                    ));

                                                                        }else {
                                                                            Toast.makeText(Cart.this, "Error: Token not found", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    }
                                                                });

                                                        new Database(getBaseContext()).cleanCart();
                                                        finish();

                                                    }

                                                    else {

                                                        final AlertDialog.Builder mensagem = new AlertDialog.Builder(Cart.this);
                                                        mensagem.setTitle("Pagamento em Dinheiro");
                                                        mensagem.setMessage("Por motivos de segurança de seu pedido, pedimos que você informe seu CPF abaixo (opcional)");

                                                        // DECLARACAO DO EDITTEXT
                                                        LayoutInflater inflater = Cart.this.getLayoutInflater();
                                                        View order_address_comment = inflater.inflate(R.layout.update_cpf_layout, null);

                                                        final MaterialEditText edtComment = order_address_comment.findViewById(R.id.edtCpf);

                                                        SimpleMaskFormatter smfDDD = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
                                                        MaskTextWatcher mtwDDD = new MaskTextWatcher(edtComment, smfDDD);
                                                        edtComment.addTextChangedListener(mtwDDD);

                                                        mensagem.setView(order_address_comment);

                                                        final String cpfNum = edtComment.getText().toString().replace(".","");
                                                        cpfNum.replace("-","");

                                                        mensagem.setPositiveButton("Fazer Pedido", new DialogInterface.OnClickListener() {

                                                            public void onClick(DialogInterface dialog, int which) {

                                                                if (isValid(edtComment.getText().toString()) || edtComment.getText().toString().equals("")){
                                                                    Map<String, Object> passwordUpdate = new HashMap<>();
                                                                    passwordUpdate.put("cpf",edtComment.getText().toString());

                                                                    DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users");
                                                                    user.child(Common.currentUser.getPhoneNumber())
                                                                            .updateChildren(passwordUpdate)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    Request request = new Request(
                                                                                            Common.currentUser.getPhoneNumber(),
                                                                                            Common.currentUser.getName(),
                                                                                            Common.currentUser.getAddress(),
                                                                                            txtTotalPrice.getText().toString()+"0".replace(".",","),
                                                                                            "0",
                                                                                            comment.getText().toString(),
                                                                                            ress.getRestId(),
                                                                                            cart
                                                                                    );

                                                                                    String order_number = String.valueOf(System.currentTimeMillis());
                                                                                    requests.child(order_number).setValue(request);

                                                                                    FirebaseDatabase.getInstance()
                                                                                            .getReference(Common.TOKEN_REF)
                                                                                            .child(request.getRestaurant())
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                    if (dataSnapshot.exists()){
                                                                                                        //TODO: NOTIFICATIONS
                                                                                                        TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);
                                                                                                        Map<String,String> notiData = new HashMap<>();
                                                                                                        notiData.put(Common.NOTI_TITLE,"ParisCart - Novo Pedido");
                                                                                                        notiData.put(Common.NOTI_CONTENT,"Você tem um pedido de "+Common.currentUser.getName());

                                                                                                        FCMSendData sendData = new FCMSendData(tokenModel.getToken(),notiData);

                                                                                                        compositeDisposable.add(ifcmService.sendNotification(sendData)
                                                                                                                .subscribeOn(Schedulers.io())
                                                                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                                                                .subscribe(new Consumer<FCMResponse>() {
                                                                                                                               @Override
                                                                                                                               public void accept(FCMResponse fcmResponse) throws Exception {
                                                                                                                                   if (fcmResponse.getSuccess() == 1){
                                                                                                                                       Toast.makeText(Cart.this, "Pedido Feito!", Toast.LENGTH_SHORT).show();
                                                                                                                                   }else{
                                                                                                                                       Toast.makeText(Cart.this, "Falha ao Notificar Restaurante", Toast.LENGTH_SHORT).show();
                                                                                                                                   }

                                                                                                                               }
                                                                                                                           }, new Consumer<Throwable>() {
                                                                                                                               @Override
                                                                                                                               public void accept(Throwable throwable) throws Exception {
                                                                                                                                   Toast.makeText(Cart.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                                               }
                                                                                                                           }
                                                                                                                ));

                                                                                                    }else {
                                                                                                        Toast.makeText(Cart.this, "Error: Token not found", Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                                                }
                                                                                            });

                                                                                    new Database(getBaseContext()).cleanCart();
                                                                                    finish();

                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(Cart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                }else {
                                                                    Toast.makeText(Cart.this, "CPF Inválido", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }

                                                        });

                                                        mensagem.show();



                                                    }

                                                }
                                            });
                                            //cria o AlertDialog
                                            alerta = builder.create();
                                            //Exibe
                                            alerta.show();

                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }

    }

    private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isValid(String cpfCnpj) {
        return (isValidCPF(cpfCnpj) || isValidCNPJ(cpfCnpj));
    }

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice=str.length()-1, digito; indice >= 0; indice-- ) {
            digito = Integer.parseInt(str.substring(indice,indice+1));
            soma += digito*peso[peso.length-str.length()+indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    private static String padLeft(String text, char character) {
        return String.format("%11s", text).replace(' ', character);
    }

    private static boolean isValidCPF(String cpf) {
        cpf = cpf.trim().replace(".", "").replace("-", "");
        if ((cpf==null) || (cpf.length()!=11)) return false;

        for (int j = 0; j < 10; j++)
            if (padLeft(Integer.toString(j), Character.forDigit(j, 10)).equals(cpf))
                return false;

        Integer digito1 = calcularDigito(cpf.substring(0,9), pesoCPF);
        Integer digito2 = calcularDigito(cpf.substring(0,9) + digito1, pesoCPF);
        return cpf.equals(cpf.substring(0,9) + digito1.toString() + digito2.toString());
    }

    private static boolean isValidCNPJ(String cnpj) {
        cnpj = cnpj.trim().replace(".", "").replace("-", "");
        if ((cnpj==null)||(cnpj.length()!=14)) return false;

        Integer digito1 = calcularDigito(cnpj.substring(0,12), pesoCNPJ);
        Integer digito2 = calcularDigito(cnpj.substring(0,12) + digito1, pesoCNPJ);
        return cnpj.equals(cnpj.substring(0,12) + digito1.toString() + digito2.toString());
    }

}
