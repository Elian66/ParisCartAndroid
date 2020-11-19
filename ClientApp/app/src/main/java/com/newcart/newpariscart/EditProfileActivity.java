package com.newcart.newpariscart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.newcart.newpariscart.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    EditText name, senha;
    TextView add;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = findViewById(R.id.registerE_name);
        add = findViewById(R.id.registerE_pass);
        senha = findViewById(R.id.registerE_senha);

        SimpleMaskFormatter smfPH = new SimpleMaskFormatter("+NN (NN) NNNNN-NNNN");
        MaskTextWatcher mtwPH = new MaskTextWatcher(add, smfPH);
        add.addTextChangedListener(mtwPH);

        name.setText(Common.currentUser.getName());
        add.setText(Common.currentUser.getPhoneNumber());
        senha.setText(Common.currentUser.getCpf());

        btn = findViewById(R.id.id_registerE_entrar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.currentUser.setName(name.getText().toString());
                Common.currentUser.setAddress(add.getText().toString());
                Common.currentUser.setSenha(senha.getText().toString());
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(Common.currentUser.getPhoneNumber())
                        .setValue(Common.currentUser)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EditProfileActivity.this, "Perfil Atualizado!",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
