package com.example.jeyabookcentre.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Models.OrdHistory;
import com.example.jeyabookcentre.Order_details;
import com.example.jeyabookcentre.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<OrdHistory> ordHistoryList;
    private String CurrentDate,CurrentTime;

    public OrderAdapter(Context context, List<OrdHistory> ordHistoryList) {
        this.context = context;
        this.ordHistoryList = ordHistoryList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate Layout
        return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_user,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        //Get data
        /*holder.OrderIdTV.setText(ordHistoryList.get(position).getOrder_id());
        holder.DateTV.setText(ordHistoryList.get(position).getOrder_time());
        holder.StatusTV.setText(ordHistoryList.get(position).getOrder_status());
        holder.AmountTV.setText(ordHistoryList.get(position).getTotal_price());*/

        OrdHistory ordHistory = ordHistoryList.get(position);
        String orderID = ordHistory.getOrder_id();
        String OrderedBy = ordHistory.getOrdered_by();
        String OrderTotal = ordHistory.getTotal_price();
        String orderStatus = ordHistory.getOrder_status();
        String orderTime = ordHistory.getOrder_date();
        String orderphone = ordHistory.getCust_phone();
        String orderCus_name = ordHistory.getCust_name();

        //Set data
        holder.AmountTV.setText("Total Amount : "+ " " +OrderTotal);
        holder.StatusTV.setText(orderStatus);
        holder.OrderIdTV.setText("Order ID: "+ " " + orderID);

        //Changing order status text color
        if (orderStatus != null && orderStatus.equalsIgnoreCase("InProgress"))
        {
            holder.StatusTV.setTextColor(context.getResources().getColor(R.color.red));
        }
        else if (orderStatus != null && orderStatus.equalsIgnoreCase("Completed"))
        {
            holder.StatusTV.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if (orderStatus != null && orderStatus.equalsIgnoreCase("Canceled"))
        {
            holder.StatusTV.setTextColor(context.getResources().getColor(R.color.purple_700));
        }

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        CurrentDate = currentDate.format(calendar.getTime());

        /*SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = currentTime.format(calendar.getTime());*/

        holder.DateTV.setText(CurrentDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open order details and pass order id as the key
                Intent intent = new Intent(context, Order_details.class);
                intent.putExtra("orderid",orderID );
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ordHistoryList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView OrderIdTV,DateTV,AmountTV,StatusTV;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            OrderIdTV = itemView.findViewById(R.id.orderId);
            DateTV = itemView.findViewById(R.id.dateTV);
            AmountTV = itemView.findViewById(R.id.amountTV);
            StatusTV = itemView.findViewById(R.id.statusTV);

        }
    }
}
