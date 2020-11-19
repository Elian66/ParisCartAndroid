package com.localplatze.parisrider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        String user = Paper.book().read(Common.USER_KEY);
        if (user != null)
        {
            if(!user.isEmpty())
            {
                login(user);
            }

        }else {
            Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }

    }

    private void login(final String phone) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Motoristas");

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(phone).exists()) {

                    final Motorista user = dataSnapshot.child(phone).getValue(Motorista.class);
                    user.setNumero(phone);

                    FirebaseInstanceId.getInstance()
                            .getInstanceId()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    //JUST TEST
                                    Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                                    Common.currentUser = user;
                                    Common.currentToken = token;
                                    startActivity(homeIntent);
                                    finish();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            //JUST TEST
                            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                            Common.currentUser = user;
                            updateToken(MainActivity.this,task.getResult().getToken());
                            token = task.getResult().getToken();
                            Common.currentToken = token;
                            startActivity(homeIntent);
                            finish();
                        }
                    });

                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
}
