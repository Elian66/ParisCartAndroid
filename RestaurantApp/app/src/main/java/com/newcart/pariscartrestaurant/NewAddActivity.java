package com.newcart.pariscartrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.newcart.pariscartrestaurant.Common.Common;
import com.newcart.pariscartrestaurant.Model.Adicional;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class NewAddActivity extends AppCompatActivity {

    MaterialEditText edtName, edtPrice;
    Button btnUpload;

    FirebaseDatabase db;
    DatabaseReference foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add);

        db = FirebaseDatabase.getInstance();
        foodList = db.getReference("Pratos").child(Common.foodName).child("adicionais");

        edtName = findViewById(R.id.edtNameADD);
        edtPrice = findViewById(R.id.edtPriceADD);

        btnUpload = findViewById(R.id.edtBtnADD);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adicional newFood = new Adicional();
                newFood.setName(edtName.getText().toString());
                newFood.setPrice(edtPrice.getText().toString());

                if (newFood != null){
                    foodList.push().setValue(newFood);
                    Toast.makeText(NewAddActivity.this, "Prato adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
