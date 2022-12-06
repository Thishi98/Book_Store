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
import com.example.jeyabookcentre.Models.OrderModel;
import com.example.jeyabookcentre.R;

import java.util.List;

public class OrderViewListAdapter extends RecyclerView.Adapter<OrderViewListAdapter.OrderListViewH>{

    private Context context;
    private List<OrderModel> orderModelList;

    public OrderViewListAdapter(Context context, List<OrderModel> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public OrderListViewH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderListViewH(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row,parent,false));  //inflate layout
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewListAdapter.OrderListViewH holder, int position) {

        Glide.with(context).load(orderModelList.get(position).getBookImg()).into(holder.OrderImageV);
        holder.Ordbook.setText(orderModelList.get(position).getBookName());
        holder.Ordauthor.setText(orderModelList.get(position).getBookAuthor());
        holder.OritemsCount.setText(String.valueOf("Item Quantity: " +orderModelList.get(position).getTotal_quantity()));
        holder.ordprice.setText(String.valueOf("Rs. "+orderModelList.get(position).getTotal_price()+ ".00"));

    }

    @Override
    public int getItemCount() {
        return orderModelList.size();  //return list size
    }

    public class OrderListViewH extends RecyclerView.ViewHolder {

        ImageView OrderImageV;
        TextView Ordbook,Ordauthor,OritemsCount,ordprice;

        public OrderListViewH(@NonNull View itemView) {
            super(itemView);

            //init view
            OrderImageV = itemView.findViewById(R.id.OrderIV);
            Ordbook = itemView.findViewById(R.id.txtBookName);
            Ordauthor = itemView.findViewById(R.id.txtBookAuthor);
            OritemsCount = itemView.findViewById(R.id.totQtytxt);
            ordprice = itemView.findViewById(R.id.txtBookprice);

        }
    }
}
