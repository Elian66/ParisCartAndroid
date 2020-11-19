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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newcart.pariscartrestaurant.Common.Common;
import com.newcart.pariscartrestaurant.Interface.ItemClickListener;
import com.newcart.pariscartrestaurant.Model.Adicional;
import com.newcart.pariscartrestaurant.Model.Motorista;
import com.newcart.pariscartrestaurant.Model.Restaurant;
import com.newcart.pariscartrestaurant.ViewHolder.AdicionalViewHolder;
import com.newcart.pariscartrestaurant.ViewHolder.MotoristaViewHolder;

public class MotoristasActivity extends AppCompatActivity {

    LinearLayout add, viw;
    EditText nome,placa,senha,num;
    Button btn;

    FirebaseDatabase database;
    DatabaseReference table_user;

    RecyclerView recycler_rest;
    RecyclerView.LayoutManager layout_rest;

    FirebaseRecyclerAdapter<Motorista, MotoristaViewHolder> adapter_rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motoristas);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Motoristas");

        recycler_rest = findViewById(R.id.recycler_moto);
        layout_rest = new LinearLayoutManager(MotoristasActivity.this);
        recycler_rest.setLayoutManager(layout_rest);

        num = findViewById(R.id.motor_num);
        add = findViewById(R.id.motor_add);
        viw = findViewById(R.id.motor_view);
        nome = findViewById(R.id.motor_nome);
        placa = findViewById(R.id.motor_placa);
        senha = findViewById(R.id.motor_senha);
        btn = findViewById(R.id.motor_btn);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viw.setVisibility(View.VISIBLE);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(num.getText().toString()).exists()) {

                            Toast.makeText(MotoristasActivity.this, "Este motorista já está cadastrado", Toast.LENGTH_SHORT).show();


                        } else {
                            viw.setVisibility(View.GONE);
                            Motorista user = new Motorista(num.getText().toString(),
                                    nome.getText().toString(),
                                    placa.getText().toString(),
                                    senha.getText().toString(),
                                    Common.currentUser.getRestId());
                            table_user.child(num.getText().toString()).setValue(user);
                            Toast.makeText(MotoristasActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        FirebaseRecyclerOptions<Motorista> optionsRest =
                new FirebaseRecyclerOptions.Builder<Motorista>()
                        .setQuery(table_user, Motorista.class)
                        .build();
        adapter_rest = new FirebaseRecyclerAdapter<Motorista, MotoristaViewHolder>(optionsRest)
        {
            @NonNull
            @Override
            public MotoristaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.motorista_item, parent, false);

                return new MotoristaViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull MotoristaViewHolder viewHolder, int position, @NonNull final Motorista model) {

                viewHolder.txtMenuName.setText(model.getNome());
                viewHolder.txtPlaca.setText(model.getPlaca());
                viewHolder.corridas.setText(model.getCorridas()+" corridas");
                //final Adicional Adicional = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        /*Common.addName = adapter_rest.getRef(position).getKey();
                        Common.addSelected = model;
                        Intent intent = new Intent(ListAdicionaisActivity.this,UpdateAddActivity.class);
                        startActivity(intent);*/

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
