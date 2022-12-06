package com.example.jeyabookcentre.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Item_View_Screen;
import com.example.jeyabookcentre.Models.StationeryModel;
import com.example.jeyabookcentre.R;
import com.example.jeyabookcentre.Stationery_item_view_screen;

import java.util.List;

public class StationeryHorizontalAdapter extends RecyclerView.Adapter<StationeryHorizontalAdapter.stationeryVH> {

    private List<StationeryModel> stationeryModelList;
    private Context context;

    public StationeryHorizontalAdapter(Context context,List<StationeryModel> stationeryModelList) {
        this.stationeryModelList = stationeryModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public stationeryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stationery_row,parent,false);
        return new stationeryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull stationeryVH holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(stationeryModelList.get(position).getItemimage()).into(holder.stationeryImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Stationery_item_view_screen.class);
                intent.putExtra("Sinfo",stationeryModelList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationeryModelList == null ? 0 : stationeryModelList.size();
    }

    public class stationeryVH extends RecyclerView.ViewHolder {

        ImageView stationeryImg;

        public stationeryVH(@NonNull View itemView) {
            super(itemView);

            stationeryImg = itemView.findViewById(R.id.statImg);

        }
    }
}
