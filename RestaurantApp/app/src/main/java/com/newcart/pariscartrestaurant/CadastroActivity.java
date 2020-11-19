package com.newcart.pariscartrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.newcart.pariscartrestaurant.Model.Restaurant;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.CountryCodePicker;

public class CadastroActivity extends AppCompatActivity {

    Button entrar;
    EditText area, number, name, pass, senha, taxa;
    CountryCodePicker ccp;

    String ph1, ph2, ph3;

    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Restaurants");

        entrar = findViewById(R.id.id_register_entrar);

        name = findViewById(R.id.register_name);
        pass = findViewById(R.id.register_pass);
        senha = findViewById(R.id.register_senha);
        taxa = findViewById(R.id.register_taxa);

        ccp = findViewById(R.id.register_cccp);
        area = findViewById(R.id.register_uf_number);
        number = findViewById(R.id.register_phone_number);

        SimpleMaskFormatter smfDDD = new SimpleMaskFormatter("NN");
        MaskTextWatcher mtwDDD = new MaskTextWatcher(area, smfDDD);
        area.addTextChangedListener(mtwDDD);

        SimpleMaskFormatter smfPH = new SimpleMaskFormatter("NNNNNNNNN");
        MaskTextWatcher mtwPH = new MaskTextWatcher(number, smfPH);
        number.addTextChangedListener(mtwPH);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ph1 = ccp.getSelectedCountryCodeWithPlus();
                ph2 = area.getText().toString();
                ph3 = number.getText().toString();

                final String ph5 = ph1+ph2+ph3;

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(ph5).exists()) {

                            Toast.makeText(CadastroActivity.this, "Este número já existe", Toast.LENGTH_SHORT).show();


                        } else {

                            Restaurant user = new Restaurant(name.getText().toString(),
                                    "https://www.clipartkey.com/mpngs/m/272-2721843_icon-free-icons-library-restaurant-icon-png.png",
                                    pass.getText().toString(),
                                    ph5,
                                    "01",
                                    taxa.getText().toString()+",00",
                                    senha.getText().toString());
                            table_user.child(ph5).setValue(user);
                            Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
