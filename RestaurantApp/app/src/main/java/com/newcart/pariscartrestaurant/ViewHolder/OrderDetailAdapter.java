package com.newcart.pariscartrestaurant.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newcart.pariscartrestaurant.Model.Order;
import com.newcart.pariscartrestaurant.R;

import java.util.List;

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name, comment, price, quantity;

    public MyViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.orderD_name);
        comment = itemView.findViewById(R.id.orderD_comment);
        price = itemView.findViewById(R.id.orderD_price);
        quantity = itemView.findViewById(R.id.orderD_quantidade);

    }

}

public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<Order> myOrders;

    public OrderDetailAdapter(List<Order> myOrders){
        this.myOrders = myOrders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = myOrders.get(position);
        holder.name.setText("Prato: "+order.getProductName());
        holder.comment.setText("Detalhes: " + order.getDiscount());
        holder.price.setText("Preço: R$"+ order.getPrice());
        holder.quantity.setText("Quantidade: "+ order.getQuantity());
    }


    @Override
    public int getItemCount() {

        return myOrders.size();
    }
}
