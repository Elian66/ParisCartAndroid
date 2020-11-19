package com.newcart.newpariscart.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.newcart.newpariscart.Interface.ItemClickListener;
import com.newcart.newpariscart.R;

public class AddsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
public TextView txtMenuName, txtPrice;
public ImageView imageView;

private ItemClickListener itemClickListener;

public AddsViewHolder(View itemView) {
        super(itemView);
        txtMenuName = itemView.findViewById(R.id.add_name);
        txtPrice = itemView.findViewById(R.id.add_price);
        imageView = itemView.findViewById(R.id.add_image);

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
