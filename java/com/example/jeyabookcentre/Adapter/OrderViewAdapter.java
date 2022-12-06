package com.example.jeyabookcentre.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Admin_Screens.Customer_Order_Details;
import com.example.jeyabookcentre.Models.OrdHistory;
import com.example.jeyabookcentre.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.OrderViewVH>{

    private Context context;
    private List<OrdHistory> ordHistoryList;
    private String CurrentDate,CurrentTime;

    public OrderViewAdapter(Context context, List<OrdHistory> ordHistoryList) {
        this.context = context;
        this.ordHistoryList = ordHistoryList;
    }

    @NonNull
    @Override
    public OrderViewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate Layout
        return new OrderViewVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_view_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewAdapter.OrderViewVH holder, @SuppressLint("RecyclerView") int position) {

        //set data
        OrdHistory ordHistory = ordHistoryList.get(position);
        String orderID = ordHistory.getOrder_id();
        String OrderedBy = ordHistory.getOrdered_by();
        String OrderTotal = ordHistory.getTotal_price();
        String orderStatus = ordHistory.getOrder_status();
        String orderDate = ordHistory.getOrder_date();
        String orderphone = ordHistory.getCust_phone();
        String orderCus_name = ordHistory.getCust_name();

        //Set data
        holder.name.setText(orderCus_name);
        holder.Date.setText(orderDate);
        holder.OrderId.setText("Order ID: "+ " " + orderID);
        holder.phone.setText(orderphone);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open order details and pass order id as the key
                Intent intent = new Intent(context, Customer_Order_Details.class);
                intent.putExtra("orderid",orderID );
                intent.putExtra("orderedby",OrderedBy);
                context.startActivity(intent);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.OrderId.getContext(),R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure to DELETE this Item detail ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Orders").child(ordHistoryList.get(position).getKey())
                                .removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();

            }
        });

        /*holder.name.setText(ordHistoryList.get(position).getCust_name());
        holder.phone.setText(ordHistoryList.get(position).getCust_phone());
        holder.OrderId.setText("Order ID:  " + ordHistoryList.get(position).getOrder_id());
        holder.Date.setText(ordHistoryList.get(position).getOrder_date());*/


    }

    @Override
    public int getItemCount() {
        return ordHistoryList.size();
    }

    public class OrderViewVH extends RecyclerView.ViewHolder {

        TextView OrderId,Date,name,phone;
        ImageView deleteBtn;

        public OrderViewVH(@NonNull View itemView) {
            super(itemView);

            OrderId = itemView.findViewById(R.id.AllorderId);
            Date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.customer);
            phone = itemView.findViewById(R.id.Phone);

            deleteBtn = itemView.findViewById(R.id.deleteOrd);

        }
    }
}
