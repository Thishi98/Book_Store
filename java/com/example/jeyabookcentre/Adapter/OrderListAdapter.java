package com.example.jeyabookcentre.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Models.CartModel;
import com.example.jeyabookcentre.Models.OrderModel;
import com.example.jeyabookcentre.R;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>{

    private Context context;
    private List<OrderModel> orderModelList;

    public OrderListAdapter(Context context, List<OrderModel> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row,parent,false));  //inflate layout
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        Glide.with(context).load(orderModelList.get(position).getBookImg()).into(holder.OrderIV);
        holder.Orderbook.setText(orderModelList.get(position).getBookName());
        holder.Orderauthor.setText(orderModelList.get(position).getBookAuthor());
        holder.Orderitems.setText(String.valueOf("Item Quantity: " +orderModelList.get(position).getTotal_quantity()));
        holder.orderprice.setText(String.valueOf("Rs. "+orderModelList.get(position).getTotal_price()+ ".00"));

    }

    @Override
    public int getItemCount() {
        return orderModelList.size();  //return list size
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        //view of order_row.xml
        ImageView OrderIV;
        TextView Orderbook,Orderauthor,Orderitems,orderprice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            //init view
            OrderIV = itemView.findViewById(R.id.OrderIV);
            Orderbook = itemView.findViewById(R.id.txtBookName);
            Orderauthor = itemView.findViewById(R.id.txtBookAuthor);
            Orderitems = itemView.findViewById(R.id.totQtytxt);
            orderprice = itemView.findViewById(R.id.txtBookprice);

        }
    }
}
