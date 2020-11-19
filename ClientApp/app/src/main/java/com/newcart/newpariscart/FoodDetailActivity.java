package com.newcart.newpariscart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newcart.newpariscart.Common.Common;
import com.newcart.newpariscart.Database.Database;
import com.newcart.newpariscart.Interface.ItemClickListener;
import com.newcart.newpariscart.Model.Adds;
import com.newcart.newpariscart.Model.Food;
import com.newcart.newpariscart.Model.Order;
import com.newcart.newpariscart.ViewHolder.AddsViewHolder;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodDetailActivity extends AppCompatActivity {

    Button linearLayout;

    ImageView image;
    TextView ingred, price, name;

    ElegantNumberButton numberButton;
    Button btnOrder;

    FirebaseDatabase database;
    DatabaseReference reference;

    EditText editText;

    Food currentFood;

    RecyclerView recycler_rest;
    RecyclerView.LayoutManager layout_rest;

    FirebaseRecyclerAdapter<Adds, AddsViewHolder> adapter_rest;
    String choose = "1";
    double priceAdd = Double.parseDouble(Common.foodSelected.getPrice());
    double cart = priceAdd;
    List<String> lista = new ArrayList<>();

    public static double somaCarrinho;
    public static double multiplica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Pratos").child(Common.foodName);

        somaCarrinho = Double.valueOf(Common.foodSelected.getPrice());
        multiplica = Double.valueOf(Common.foodSelected.getPrice());

        editText = findViewById(R.id.fd_edit_comment);
        image = findViewById(R.id.fd_image);
        name = findViewById(R.id.fd_name);
        price = findViewById(R.id.fd_price);
        ingred = findViewById(R.id.fd_ing);
        numberButton = findViewById(R.id.number_button);
        btnOrder = findViewById(R.id.fd_btn);

        linearLayout = findViewById(R.id.viewCartBtnFD);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FoodDetailActivity.this,Cart.class);
                startActivity(intent);

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(image);

                name.setText(currentFood.getName());
                price.setText("R$"+currentFood.getPrice());
                ingred.setText(currentFood.getIngredientes());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnOrder.setText("Adicionar: R$"+Double.valueOf(cart));

        recycler_rest = findViewById(R.id.recycler_adds);
        layout_rest = new LinearLayoutManager(FoodDetailActivity.this);
        recycler_rest.setLayoutManager(layout_rest);

        final DatabaseReference adicionais = database.getReference("Pratos").child(Common.foodName).child("adicionais");

        FirebaseRecyclerOptions<Adds> optionsRest =
                new FirebaseRecyclerOptions.Builder<Adds>()
                        .setQuery(adicionais, Adds.class)
                        .build();
        adapter_rest = new FirebaseRecyclerAdapter<Adds, AddsViewHolder>(optionsRest)
        {
            @NonNull
            @Override
            public AddsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adds_item, parent, false);

                return new AddsViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final AddsViewHolder viewHolder, int position, @NonNull final Adds model) {

                viewHolder.txtMenuName.setText(model.getName());
                viewHolder.txtPrice.setText("+ R$"+model.getPrice());
                final Adds local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (!model.isChecked()){
                            model.setChecked(true);
                            cart = somaCarrinho + Double.parseDouble(model.getPrice());
                            somaCarrinho = cart;
                            multiplica = somaCarrinho*Integer.valueOf(numberButton.getNumber());
                            btnOrder.setText("Adicionar: R$"+Double.valueOf(multiplica));
                            lista.add(model.getName());
                            viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_black_24dp));

                        }else {
                            model.setChecked(false);
                            cart = somaCarrinho - Double.parseDouble(model.getPrice());
                            somaCarrinho = cart;
                            lista.remove(model.getName());
                            multiplica = somaCarrinho*Integer.valueOf(numberButton.getNumber());
                            btnOrder.setText("Adicionar: R$"+Double.valueOf(multiplica));
                            viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));

                        }
                    }
                });
            }
        };
        recycler_rest.setAdapter(adapter_rest);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        Common.foodName,
                        currentFood.getName()+" -> "+lista,
                        numberButton.getNumber(),
                        String.valueOf(somaCarrinho),
                        editText.getText().toString(),
                        currentFood.getImage()
                ));

                Common.isCartEmpty = "1";

                Toast.makeText(FoodDetailActivity.this, "Adicionado ao Carrinho", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter_rest.startListening();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter_rest != null){
            adapter_rest.startListening();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter_rest.stopListening();

    }

}
