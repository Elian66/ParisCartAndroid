package com.newcart.pariscartrestaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newcart.pariscartrestaurant.Common.Common;
import com.newcart.pariscartrestaurant.Model.FCMResponse;
import com.newcart.pariscartrestaurant.Model.FCMSendData;
import com.newcart.pariscartrestaurant.Model.Order;
import com.newcart.pariscartrestaurant.Model.Request;
import com.newcart.pariscartrestaurant.Model.TokenModel;
import com.newcart.pariscartrestaurant.Remote.IFCMService;
import com.newcart.pariscartrestaurant.Remote.RetrofitFCMClient;
import com.newcart.pariscartrestaurant.ViewHolder.OrderDetailAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailsActivity extends AppCompatActivity {

    String pdfname, pdfadd, pdfphone, pdftotal;
    String pdfood1,pdfood2,pdfood3,pdfood4;
    String pdfood5 = "",pdfood6 = "",pdfood7 = "",pdfood8 = "",pdfood9 = "",pdfood10 = "",pdfood11 = "", pdfood12 = "";
    String pdfood13 = "",pdfood14 = "",pdfood15 = "",pdfood16 = "",pdfood17 = "",pdfood18 = "", pdfood19 = "", pdfood20 = "";
    String pdfood21 = "",pdfood22 = "",pdfood23 = "",pdfood24 = "",pdfood25 = "",pdfood26 = "", pdfood27 = "", pdfood28 = "";


    TextView name,comment,price,quantity;

    Button imprimir;

    TextView day, status,address, phone;

    RecyclerView recycler_rest;
    RecyclerView.LayoutManager layout_rest;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference requests;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IFCMService ifcmService;

    Request currentFood;

    String order_id_value = "";

    LinearLayout atualizar_status;

    MaterialSpinner spinner;

    Uri docUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);

        final String newline = System.getProperty("line.separator");

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        requests = firebaseDatabase.getReference("Requests").child(Common.orderSelected);

        atualizar_status = findViewById(R.id.atualizar_status);
        atualizar_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateDialog(Common.orderSelected,Common.currentOrdet);

            }
        });

        /*
        name=findViewById(R.id.orderD_name);
        comment=findViewById(R.id.orderD_comment);
        price=findViewById(R.id.orderD_price);
        quantity=findViewById(R.id.orderD_quantidade);
        */

        day = findViewById(R.id.ODPDAY);
        status = findViewById(R.id.ODPSTATUS);
        address = findViewById(R.id.ODPADDRESS);
        phone = findViewById(R.id.ODPPhone);

        recycler_rest = findViewById(R.id.recycler_orderD);
        recycler_rest.setHasFixedSize(true);
        layout_rest = new LinearLayoutManager(this);
        recycler_rest.setLayoutManager(layout_rest);

        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Request.class);

                day.setText(currentFood.getTotal());
                status.setText(Common.convertCodeToStatus(currentFood.getStatus()));
                address.setText(currentFood.getAddress());
                phone.setText("Contato do Cliente: "+currentFood.getPhone());

                order_id_value = getIntent().getStringExtra("OrderId");

                final OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentOrdet.getFoods());
                adapter.notifyDataSetChanged();
                recycler_rest.setAdapter(adapter);

                imprimir = findViewById(R.id.imprimir);
                imprimir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        List<Order> myOrders = Common.currentOrdet.getFoods();

                        pdfname = "Cliente: "+currentFood.getName();
                        pdfadd = currentFood.getAddress();
                        pdfphone = "Telefone para Contato: "+currentFood.getPhone();
                        pdftotal = currentFood.getTotal();

                        int position = 0;

                        Order order = myOrders.get(position);

                        pdfood1 = "- "+order.getProductName();
                        pdfood2 = "Comentário: "+order.getDiscount();
                        pdfood3 = "R$"+order.getPrice();
                        pdfood4 = order.getQuantity()+" unid.";

                        int pos2 = 1;

                        if (myOrders.size()>=2){
                            Order order1 = myOrders.get(pos2);

                            pdfood5 = "- "+order1.getProductName();
                            pdfood6 = "Comentário: "+order1.getDiscount();
                            pdfood7 = "R$"+order1.getPrice();
                            pdfood8 = order1.getQuantity()+" unid.";

                        }

                        int pos3 = 2;

                        if (myOrders.size()>=3){
                            Order order2 = myOrders.get(pos3);

                            pdfood9 = "- "+order2.getProductName();
                            pdfood10 = "Comentário: "+order2.getDiscount();
                            pdfood11 = "R$"+order2.getPrice();
                            pdfood12 = order2.getQuantity()+" unid.";

                        }

                        int pos4 = 3;

                        if (myOrders.size()>=4){
                            Order order3 = myOrders.get(pos4);

                            pdfood13 = "- "+order3.getProductName();
                            pdfood14 = "Comentário: "+order3.getDiscount();
                            pdfood15 = "R$"+order3.getPrice();
                            pdfood16 = order3.getQuantity()+" unid.";

                        }

                        int pos5 = 4;

                        if (myOrders.size()>=5){
                            Order order4 = myOrders.get(pos5);

                            pdfood17 = "- "+order4.getProductName();
                            pdfood18 = "Comentário: "+order4.getDiscount();
                            pdfood19 = "R$"+order4.getPrice();
                            pdfood20 = order4.getQuantity()+" unid.";

                        }

                        int pos6 = 5;

                        if (myOrders.size()>=6){
                            Order order5 = myOrders.get(pos6);

                            pdfood21 = "- "+order5.getProductName();
                            pdfood22 = "Comentário: "+order5.getDiscount();
                            pdfood23 = "R$"+order5.getPrice();
                            pdfood24 = order5.getQuantity()+" unid.";

                        }

                        int pos7 = 6;

                        if (myOrders.size()>=7){
                            Order order6 = myOrders.get(pos7);

                            pdfood25 = "- "+order6.getProductName();
                            pdfood26 = "Comentário: "+order6.getDiscount();
                            pdfood27 = "R$"+order6.getPrice();
                            pdfood28 = order6.getQuantity()+" unid.";

                        }

                        String data = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                        // create a new document
                        PdfDocument document = new PdfDocument();
                        // crate a page description
                        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
                        // start a page
                        PdfDocument.Page page = document.startPage(pageInfo);
                        Canvas canvas = page.getCanvas();
                        Paint paint = new Paint();
                        paint.setColor(Color.BLACK);
                        paint.setTextSize(40);
                        canvas.drawText("ParisCart", 20, 45, paint);
                        paint.setColor(Color.BLACK);
                        paint.setTextSize(12);
                        canvas.drawText("RECIBO DE PEDIDO", 20, 62, paint);
                        paint.setColor(Color.BLACK);
                        canvas.drawText("Data: " + data, 20, 90, paint);
                        paint.setColor(Color.BLACK);
                        canvas.drawText(pdfname, 20, 105, paint);
                        paint.setColor(Color.BLACK);
                        canvas.drawText(pdfadd, 20, 120, paint);
                        paint.setColor(Color.BLACK);
                        canvas.drawText(pdfphone, 20, 135, paint);
                        paint.setColor(Color.BLACK);
                        canvas.drawText(pdftotal, 20, 150, paint);
                        paint.setColor(Color.BLACK);
                        paint.setTextSize(17);
                        canvas.drawText("ENTREGA", 20, 170, paint);
                        paint.setTextSize(22);
                        canvas.drawText("Valor: "+pdftotal, 20, 185, paint);
                        paint.setTextSize(12);
                        canvas.drawText("---------------------------", 20, 195, paint);
                        paint.setColor(Color.BLACK);
                        canvas.drawText("Detalhes:", 20, 200, paint);

                        canvas.drawText(pdfood1, 20, 220, paint);
                        paint.setTextSize(15);
                        canvas.drawText(pdfood3, 20, 240, paint);
                        paint.setTextSize(13);
                        canvas.drawText(pdfood4, 100, 240, paint);
                        canvas.drawText(pdfood2, 20, 255, paint);

                        canvas.drawText(pdfood5, 20, 270, paint);
                        paint.setTextSize(15);
                        canvas.drawText(pdfood7, 20, 290, paint);
                        paint.setTextSize(13);
                        canvas.drawText(pdfood8, 100, 290, paint);
                        canvas.drawText(pdfood6, 20, 305, paint);

                        canvas.drawText(pdfood9, 20, 320, paint);
                        paint.setTextSize(15);
                        canvas.drawText(pdfood11, 20, 335, paint);
                        paint.setTextSize(13);
                        canvas.drawText(pdfood12, 100, 335, paint);
                        canvas.drawText(pdfood10, 20, 350, paint);

                        canvas.drawText(pdfood13, 20, 365, paint);
                        paint.setTextSize(15);
                        canvas.drawText(pdfood15, 20, 380, paint);
                        paint.setTextSize(13);
                        canvas.drawText(pdfood16, 100, 380, paint);
                        canvas.drawText(pdfood14, 20, 395, paint);

                        canvas.drawText(pdfood17, 20, 410, paint);
                        paint.setTextSize(15);
                        canvas.drawText(pdfood19, 20, 425, paint);
                        paint.setTextSize(13);
                        canvas.drawText(pdfood20, 100, 425, paint);
                        canvas.drawText(pdfood18, 20, 440, paint);

                        canvas.drawText(pdfood21, 20, 455, paint);
                        paint.setTextSize(15);
                        canvas.drawText(pdfood23, 20, 470, paint);
                        paint.setTextSize(13);
                        canvas.drawText(pdfood24, 100, 470, paint);
                        canvas.drawText(pdfood22, 20, 485, paint);

                        canvas.drawText(pdfood25, 20, 500, paint);
                        paint.setTextSize(15);
                        canvas.drawText(pdfood27, 20, 515, paint);
                        paint.setTextSize(13);
                        canvas.drawText(pdfood28, 100, 515, paint);
                        canvas.drawText(pdfood26, 20, 530, paint);

                        canvas.drawText("---------------", 20, 545, paint);
                        canvas.drawText("Se houver + de 7 pedidos impressos", 20, 560, paint);
                        canvas.drawText("Checar no app se existem mais", 20, 570, paint);

                        //canvas.drawt
                        // finish the page
                        document.finishPage(page);
                        // draw text on the graphics object of the page
                        // write the document content
                        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Documents/";
                        File file = new File(directory_path);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        String targetPdf = directory_path+"paris_cart_order.pdf";
                        File filePath = new File(targetPdf);
                        try {
                            document.writeTo(new FileOutputStream(filePath));
                            Toast.makeText(OrderDetailsActivity.this, "Pdf criado. Pronto para imprimir", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Log.e("main", "error "+e.toString());
                            Toast.makeText(OrderDetailsActivity.this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
                        }
                        // close the document
                        document.close();

                        docUri = Uri.fromFile(filePath);

                        Intent printIntent = new Intent(OrderDetailsActivity.this, PrintDialogActivity.class);
                        printIntent.setDataAndType(docUri, "application/pdf");
                        printIntent.putExtra("title", "ParisCart");
                        startActivity(printIntent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createPdf(String sometext){
    }

    private void showUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetailsActivity.this);
        alertDialog.setTitle("Atualizar Status do Pedido");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout,null);

        spinner = (MaterialSpinner)view.findViewById(R.id.statusSpinner);
        spinner.setItems("Aguardando Confirmação", "Serviço Aceito", "À Caminho" , "Finalizado", "Cancelar Pedido");

        alertDialog.setView(view);
        final String localKey = key;
        alertDialog.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                dialog.dismiss();
                Map<String, Object> passwordUpdate = new HashMap<>();
                passwordUpdate.put("status",String.valueOf(spinner.getSelectedIndex()));

                requests.updateChildren(passwordUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                //TODO: NOTIFY
                                final AlertDialog dialog1 = new SpotsDialog.Builder().setContext(OrderDetailsActivity.this)
                                        .setCancelable(false).build();
                                dialog1.show();

                                FirebaseDatabase.getInstance()
                                        .getReference(Common.TOKEN_REF)
                                        .child(item.getPhone())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    dialog1.dismiss();
                                                    //TODO: NOTIFICATIONS
                                                    TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);
                                                    Map<String,String> notiData = new HashMap<>();
                                                    notiData.put(Common.NOTI_TITLE,"ParisCart - Pedido Atualizado");
                                                    notiData.put(Common.NOTI_CONTENT,"Pedido: "+Common.convertCodeToStatus(String.valueOf(spinner.getSelectedIndex())));

                                                    FCMSendData sendData = new FCMSendData(tokenModel.getToken(),notiData);

                                                    compositeDisposable.add(ifcmService.sendNotification(sendData)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Consumer<FCMResponse>() {
                                                                           @Override
                                                                           public void accept(FCMResponse fcmResponse) throws Exception {
                                                                               if (fcmResponse.getSuccess() == 1){
                                                                                   Toast.makeText(OrderDetailsActivity.this, "Status Atualizado", Toast.LENGTH_SHORT).show();
                                                                               }else{
                                                                                   Toast.makeText(OrderDetailsActivity.this, "Falha ao Notificar Cliente", Toast.LENGTH_SHORT).show();
                                                                               }

                                                                           }
                                                                       }, new Consumer<Throwable>() {
                                                                           @Override
                                                                           public void accept(Throwable throwable) throws Exception {
                                                                               Toast.makeText(OrderDetailsActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                                           }
                                                                       }
                                                            ));

                                                }else {
                                                    dialog1.dismiss();
                                                    Toast.makeText(OrderDetailsActivity.this, "Error: Token not found", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                dialog1.dismiss();
                                            }
                                        });
                            }
                        });
                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

            }
        });
        alertDialog.show();
    }
}