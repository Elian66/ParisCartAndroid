package com.newcart.newpariscart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

public class PagamentoActivity extends AppCompatActivity {

    ImageView a,b,c,d,e,pg_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);

        a = findViewById(R.id.pag_dinheiro);
        b = findViewById(R.id.pag_cartao);
        c = findViewById(R.id.pag_caixa);
        d = findViewById(R.id.pag_picpay);
        e = findViewById(R.id.pag_cripto);
        pg_back = findViewById(R.id.pg_back);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();

        final String paym = pref.getString("paymentWay", null);

        if (paym.equals("Dinheiro")){
            a.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_orange_24dp));
            b.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            c.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            d.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            e.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
        }else if (paym.equals("Maquina")){
            a.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            b.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_orange_24dp));
            c.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            d.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            e.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
        }else if (paym.equals("Caixa")){
            a.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            b.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            c.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_orange_24dp));
            d.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            e.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
        }else if (paym.equals("PicPay")){
            a.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            b.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            c.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            d.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_orange_24dp));
            e.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
        }else if (paym.equals("Cripto")){
            a.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            b.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            c.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            d.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
            e.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_orange_24dp));
        }

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_orange_24dp));
                b.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                c.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                d.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                e.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));

                editor.putString("paymentWay", "Dinheiro");
                editor.apply();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                b.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_orange_24dp));
                c.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                d.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                e.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));

                editor.putString("paymentWay", "Maquina");
                editor.apply();
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                b.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                c.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_orange_24dp));
                d.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                e.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                editor.putString("paymentWay", "Caixa");
                editor.apply();
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                b.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                c.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                d.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_orange_24dp));
                e.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));

                editor.putString("paymentWay", "PicPay");
                editor.apply();
            }
        });

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                b.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                c.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                d.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_gray_24dp));
                e.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_orange_24dp));

                editor.putString("paymentWay", "Cripto");
                editor.apply();
            }
        });

        pg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(PagamentoActivity.this,Cart.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent a = new Intent(this,Cart.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
