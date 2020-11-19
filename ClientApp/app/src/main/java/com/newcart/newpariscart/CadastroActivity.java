package com.newcart.newpariscart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.newcart.newpariscart.Adapter.PlacesAutoCompleteAdapter;
import com.newcart.newpariscart.Common.Common;
import com.newcart.newpariscart.Model.User;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.CountryCodePicker;

import java.util.Arrays;
import java.util.List;

public class CadastroActivity extends AppCompatActivity{

    Button entrar;
    EditText number, name, senha, pass;
    CountryCodePicker ccp;
    ImageView eye,back;

    String ph1, ph2, ph3, myPlac;

    FirebaseDatabase database;
    DatabaseReference table_user;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;

    boolean seePass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        back = findViewById(R.id.register_back);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Users");

        entrar = findViewById(R.id.id_register_entrar);
        eye = findViewById(R.id.register_eye);

        name = findViewById(R.id.register_name);
        //pass = findViewById(R.id.register_pass);
        senha = findViewById(R.id.register_senha);

        ccp = findViewById(R.id.register_ccp);
        number = findViewById(R.id.register_phone_number);

        SimpleMaskFormatter smfPH = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtwPH = new MaskTextWatcher(number, smfPH);
        number.addTextChangedListener(mtwPH);

        number.setText(Common.segundoNumCad);

        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.register_pass);
        //autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_SHORT).show();
                myPlac = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!seePass){
                    senha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye.setBackgroundResource(R.drawable.ic_visibility_off_black_24dp);
                    seePass = true;
                }else if (seePass){
                    senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye.setBackgroundResource(R.drawable.ic_remove_red_eye_black_24dp);
                    seePass = false;
                }
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ph1 = ccp.getSelectedCountryCodeWithPlus();
                ph3 = number.getText().toString().replace("(","")
                .replace(")","").replace(" ","").replace("-","");

                final String ph5 = ph1+ph3;

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(ph5).exists()) {

                            Toast.makeText(CadastroActivity.this, "Este número já existe", Toast.LENGTH_SHORT).show();


                        } else {

                            User user = new User(name.getText().toString(),"", ph5,myPlac,senha.getText().toString());
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
