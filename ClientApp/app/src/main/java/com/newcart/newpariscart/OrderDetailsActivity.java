package com.newcart.newpariscart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.newcart.newpariscart.Common.Common;
import com.newcart.newpariscart.Model.Request;
import com.newcart.newpariscart.ViewHolder.OrderDetailAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView name,comment,price,quantity;

    TextView day, status,address;

    RecyclerView recycler_rest;
    RecyclerView.LayoutManager layout_rest;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference requests;

    Request currentFood;

    String order_id_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        requests = firebaseDatabase.getReference("Requests").child(Common.orderSelected);

        /*
        name=findViewById(R.id.orderD_name);
        comment=findViewById(R.id.orderD_comment);
        price=findViewById(R.id.orderD_price);
        quantity=findViewById(R.id.orderD_quantidade);
        */

        day = findViewById(R.id.ODPDAY);
        status = findViewById(R.id.ODPSTATUS);
        address = findViewById(R.id.ODPADDRESS);

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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        order_id_value = getIntent().getStringExtra("OrderId");

        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentOrdet.getFoods());
        adapter.notifyDataSetChanged();
        recycler_rest.setAdapter(adapter);

    }

}
