package com.newcart.pariscartrestaurant.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.newcart.pariscartrestaurant.Interface.ItemClickListener;
import com.newcart.pariscartrestaurant.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtMenuName, txtIngred, txtPrice;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);
        txtMenuName = (TextView)itemView.findViewById(R.id.food_name);
        imageView = (ImageView)itemView.findViewById(R.id.food_image);
        txtIngred = itemView.findViewById(R.id.food_description);
        txtPrice = itemView.findViewById(R.id.food_price);

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
