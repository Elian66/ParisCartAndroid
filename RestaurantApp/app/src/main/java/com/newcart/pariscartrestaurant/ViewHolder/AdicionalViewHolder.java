package com.newcart.pariscartrestaurant.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.newcart.pariscartrestaurant.Interface.ItemClickListener;
import com.newcart.pariscartrestaurant.R;

public class AdicionalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtMenuName, txtPrice;

    private ItemClickListener itemClickListener;

    public AdicionalViewHolder(View itemView) {
        super(itemView);
        txtMenuName = itemView.findViewById(R.id.ad_name);
        txtPrice = itemView.findViewById(R.id.ad_price);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(), false);

    }
}
