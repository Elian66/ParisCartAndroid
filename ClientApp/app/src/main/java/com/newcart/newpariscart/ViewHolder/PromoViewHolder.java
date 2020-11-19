package com.newcart.newpariscart.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.newcart.newpariscart.Interface.ItemClickListener;
import com.newcart.newpariscart.R;

public class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtMenuName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public PromoViewHolder(View itemView) {
        super(itemView);
        txtMenuName = (TextView)itemView.findViewById(R.id.promo_name);
        imageView = (ImageView)itemView.findViewById(R.id.promo_image);

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