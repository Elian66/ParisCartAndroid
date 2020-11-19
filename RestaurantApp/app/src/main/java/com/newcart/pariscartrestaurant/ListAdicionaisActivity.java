package com.newcart.pariscartrestaurant;

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

import com.newcart.pariscartrestaurant.Common.Common;
import com.newcart.pariscartrestaurant.Interface.ItemClickListener;
import com.newcart.pariscartrestaurant.Model.Adicional;
import com.newcart.pariscartrestaurant.ViewHolder.AdicionalViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListAdicionaisActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference foodList, att;

    RecyclerView recycler_rest;
    RecyclerView.LayoutManager layout_rest;

    Button acomp_extraADD, update, delete;

    FirebaseRecyclerAdapter<Adicional, AdicionalViewHolder> adapter_rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_adicionais);

        db = FirebaseDatabase.getInstance();
        foodList = db.getReference("Pratos").child(Common.foodName).child("adicionais");
        att = FirebaseDatabase.getInstance().getReference("Pratos");

        recycler_rest = findViewById(R.id.recycler_listAds);
        layout_rest = new LinearLayoutManager(ListAdicionaisActivity.this);
        recycler_rest.setLayoutManager(layout_rest);

        update = findViewById(R.id.acomp_extraUPDATE);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListAdicionaisActivity.this,UpdateFoodActivity.class);
                startActivity(intent);
            }
        });

        delete = findViewById(R.id.acomp_extraDELETE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                att.child(Common.foodName).removeValue();
                finish();
            }
        });

        acomp_extraADD = findViewById(R.id.acomp_extraADD);
        acomp_extraADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListAdicionaisActivity.this,NewAddActivity.class);
                startActivity(intent);
            }
        });

        FirebaseRecyclerOptions<Adicional> optionsRest =
                new FirebaseRecyclerOptions.Builder<Adicional>()
                        .setQuery(foodList, Adicional.class)
                        .build();
        adapter_rest = new FirebaseRecyclerAdapter<Adicional, AdicionalViewHolder>(optionsRest)
        {
            @NonNull
            @Override
            public AdicionalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adicional_item, parent, false);

                return new AdicionalViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull AdicionalViewHolder viewHolder, int position, @NonNull final Adicional model) {

                viewHolder.txtMenuName.setText(model.getName());
                viewHolder.txtPrice.setText("R$"+model.getPrice());
                final Adicional Adicional = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Common.addName = adapter_rest.getRef(position).getKey();
                        Common.addSelected = model;
                        Intent intent = new Intent(ListAdicionaisActivity.this,UpdateAddActivity.class);
                        startActivity(intent);

                    }
                });
            }
        };
        recycler_rest.setAdapter(adapter_rest);

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
