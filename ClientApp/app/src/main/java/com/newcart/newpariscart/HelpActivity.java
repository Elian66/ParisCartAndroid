package com.newcart.newpariscart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class HelpActivity extends AppCompatActivity {

    Button term, button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        button = findViewById(R.id.help_contato);
        term = findViewById(R.id.help_termos);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(HelpActivity.this);
                //Uncomment the below code to Set the message and title from the strings.xml file
                builder.setTitle("ParisCart - Contato");

                //Setting message manually and performing action on button click
                builder.setMessage("E-mail: paris.cart@gmail.com \n WhatsApp: +55 92 99238-9735");
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this,TermosActivity.class);
                startActivity(intent);
            }
        });
    }
}
