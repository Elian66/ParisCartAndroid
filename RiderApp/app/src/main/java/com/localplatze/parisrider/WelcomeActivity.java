package com.localplatze.parisrider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.localplatze.parisrider.Common.Common;
import com.localplatze.parisrider.Model.Motorista;
import com.localplatze.parisrider.Model.Restaurant;
import com.localplatze.parisrider.Model.TokenModel;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rilixtech.CountryCodePicker;

import io.paperdb.Paper;

public class WelcomeActivity extends AppCompatActivity implements TextWatcher {

    TextView login_termos;
    EditText number;
    CountryCodePicker ccp;

    String ph1, ph3, token;

    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Paper.init(this);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Motoristas");

        ccp = findViewById(R.id.login_ccp);
        number = findViewById(R.id.login_number);

        login_termos = findViewById(R.id.login_termos);

        login_termos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(WelcomeActivity.this,TermosActivity.class);
                //startActivity(intent);
            }
        });

        SimpleMaskFormatter smfPH = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtwPH = new MaskTextWatcher(number, smfPH);
        number.addTextChangedListener(mtwPH);

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(number.getText().toString().length()==15)
                {
                    final ProgressDialog mDialog = new ProgressDialog(WelcomeActivity.this);
                    mDialog.setMessage("Carregando...");
                    mDialog.show();

                    ph1 = ccp.getSelectedCountryCodeWithPlus();
                    ph3 = number.getText().toString().replace("(","")
                            .replace(")","").replace(" ","")
                            .replace("-","");

                    final String ph5 = ph1+ph3;

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(ph5).exists()) {

                                mDialog.dismiss();

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(WelcomeActivity.this);
                                alertDialog.setTitle("Insira sua Senha");

                                LayoutInflater inflater = WelcomeActivity.this.getLayoutInflater();
                                View order_address_comment = inflater.inflate(R.layout.order_address_layout, null);

                                final MaterialEditText edtComment = (MaterialEditText) order_address_comment.findViewById(R.id.edtComment);


                                alertDialog.setView(order_address_comment);
                                alertDialog.setIcon(R.drawable.ic_person_black_24dp);
                                alertDialog.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String comment = edtComment.getText().toString();

                                        //TODO: SCREEN --> HOME
                                        final Motorista user = dataSnapshot.child(ph5).getValue(Motorista.class);

                                        if (comment.equals(user.getSenha())){

                                            Paper.book().write(Common.USER_KEY, ph5);
                                            Paper.book().write(Common.PWD_KEY, comment);

                                            FirebaseInstanceId.getInstance()
                                                    .getInstanceId()
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(WelcomeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            //JUST TEST
                                                            Intent homeIntent = new Intent(WelcomeActivity.this, OnBoardActivity.class);
                                                            Common.currentUser = user;
                                                            //Common.myPhone = user.getPhoneNumber();
                                                            Common.currentToken = token;
                                                            startActivity(homeIntent);
                                                            finish();
                                                        }
                                                    }).addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                    //JUST TEST
                                                    Intent homeIntent = new Intent(WelcomeActivity.this, OnBoardActivity.class);
                                                    Common.currentUser = user;
                                                    user.setNome(Common.currentUser.getNome());
                                                    updateToken(WelcomeActivity.this,task.getResult().getToken());
                                                    //Common.myPhone = user.getPhoneNumber();
                                                    token = task.getResult().getToken();
                                                    Common.currentToken = token;
                                                    startActivity(homeIntent);
                                                    finish();
                                                }
                                            });

                                        }else{
                                            Toast.makeText(WelcomeActivity.this, "Senha Incorreta", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                });
                                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        dialogInterface.dismiss();

                                    }
                                });
                                alertDialog.show();

                            } else {
                                Toast.makeText(WelcomeActivity.this, "Motorista não encontrado", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void updateToken(final Context context, String newToken){
        if (Common.currentUser!=null){
            FirebaseDatabase.getInstance()
                    .getReference(Common.TOKEN_REF)
                    .child(Common.currentUser.getNumero())
                    .setValue(new TokenModel(Common.currentUser.getNumero(),newToken))
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
