package com.newcart.pariscartrestaurant.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.newcart.pariscartrestaurant.Interface.ItemClickListener;
import com.newcart.pariscartrestaurant.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId, txtOrderAddress,ide,data,namee,payway,order_iniciatexto;
    public ImageView color;
    public LinearLayout order_iniciapreparo;
    public Button order_ac,order_re,order_de;
    public CardView vis;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);
        order_ac = itemView.findViewById(R.id.order_ac);
        order_re = itemView.findViewById(R.id.order_re);
        order_de = itemView.findViewById(R.id.order_de);
        order_iniciapreparo = itemView.findViewById(R.id.order_iniciapreparo);
        order_iniciatexto = itemView.findViewById(R.id.order_iniciatexto);
        ide = itemView.findViewById(R.id.order_pedido);
        data = itemView.findViewById(R.id.order_data);
        namee = itemView.findViewById(R.id.order_nome);
        payway = itemView.findViewById(R.id.order_pagamento);
        txtOrderAddress = (TextView)itemView.findViewById(R.id.order_price);
        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        color = itemView.findViewById(R.id.orderItemColorLeft);
        vis = itemView.findViewById(R.id.order_vis);

        order_iniciapreparo.setOnClickListener(this);
        order_ac.setOnClickListener(this);
        order_re.setOnClickListener(this);
        order_de.setOnClickListener(this);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
