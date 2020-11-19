package com.newcart.pariscartrestaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.newcart.pariscartrestaurant.Common.Common;
import com.newcart.pariscartrestaurant.Model.Adicional;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class UpdateAddActivity extends AppCompatActivity {

    MaterialEditText edtName, edtPrice;
    Button btnUpload;

    FirebaseDatabase db;
    DatabaseReference foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_add);

        db = FirebaseDatabase.getInstance();
        foodList = db.getReference("Pratos").child(Common.foodName).child("adicionais");

        edtName = findViewById(R.id.edtNameADDN);
        edtPrice = findViewById(R.id.edtPriceADDN);

        edtName.setText(Common.addSelected.getName());
        edtPrice.setText(Common.addSelected.getPrice());

        btnUpload = findViewById(R.id.edtBtnADDN);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adicional newFood = new Adicional();
                newFood.setName(edtName.getText().toString());
                newFood.setPrice(edtPrice.getText().toString());

                Common.addSelected.setName(edtName.getText().toString());
                Common.addSelected.setPrice(edtPrice.getText().toString());
                foodList
                        .child(Common.addName)
                        .setValue(Common.addSelected)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(UpdateAddActivity.this, "Atualizado", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}
