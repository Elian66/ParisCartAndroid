package com.newcart.newpariscart.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.newcart.newpariscart.Interface.ItemClickListener;
import com.newcart.newpariscart.R;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtMenuName, txtCategory, txtAddress, txtEntrega;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        txtMenuName = (TextView)itemView.findViewById(R.id.rest_name);
        imageView = (ImageView)itemView.findViewById(R.id.rest_image);
        txtCategory = itemView.findViewById(R.id.rest_description);
        txtAddress = itemView.findViewById(R.id.rest_city);
        txtEntrega = itemView.findViewById(R.id.rest_entrega);

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
