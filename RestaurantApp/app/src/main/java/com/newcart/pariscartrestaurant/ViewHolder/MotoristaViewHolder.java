package com.newcart.pariscartrestaurant.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.newcart.pariscartrestaurant.Interface.ItemClickListener;
import com.newcart.pariscartrestaurant.R;

public class MotoristaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtMenuName, txtPlaca, corridas;

    private ItemClickListener itemClickListener;

    public MotoristaViewHolder(View itemView) {
        super(itemView);
        txtMenuName = itemView.findViewById(R.id.mot_name);
        txtPlaca = itemView.findViewById(R.id.mot_placa);
        corridas = itemView.findViewById(R.id.mot_corridas);

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
