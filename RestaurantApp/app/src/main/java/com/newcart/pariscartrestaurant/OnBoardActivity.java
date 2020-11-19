package com.newcart.pariscartrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OnBoardActivity extends AppCompatActivity {

    Button boa,bb,bc,bd;
    ImageView testeeee;
    TextView pa,pb,pc,b_cripto;
    RelativeLayout roa,rob,roc,rod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        testeeee = findViewById(R.id.testeeee);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("paymentWay", "Dinheiro");
        editor.apply();

        boa = findViewById(R.id.board_bt1);
        bb = findViewById(R.id.board_bt2);
        bc = findViewById(R.id.board_bt3);
        bd = findViewById(R.id.board_bt4);

        pa = findViewById(R.id.b_pular1);
        pb = findViewById(R.id.b_pular2);
        pc = findViewById(R.id.b_pular3);
        b_cripto = findViewById(R.id.b_cripto);

        roa = findViewById(R.id.board_lay01);
        rob = findViewById(R.id.board_lay02);
        roc = findViewById(R.id.board_lay03);
        rod = findViewById(R.id.board_lay04);

        boa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roa.setVisibility(View.GONE);
                rob.setVisibility(View.VISIBLE);
            }
        });

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rob.setVisibility(View.GONE);
                roc.setVisibility(View.VISIBLE);
            }
        });

        bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roc.setVisibility(View.GONE);
                rod.setVisibility(View.VISIBLE);
            }
        });

        bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnBoardActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnBoardActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        pb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnBoardActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnBoardActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b_cripto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnBoardActivity.this,InfoCryptoActivity.class);
                startActivity(intent);
            }
        });
    }
}