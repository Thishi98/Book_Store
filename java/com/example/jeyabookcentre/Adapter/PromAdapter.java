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
import com.example.jeyabookcentre.Models.Promotions;
import com.example.jeyabookcentre.R;

import java.util.List;

public class PromAdapter extends RecyclerView.Adapter<PromAdapter.PromVH>{

    private List<Promotions> promotionsList;
    private Context context;

    public PromAdapter(Context context,List<Promotions> promotionsList) {
        this.promotionsList = promotionsList;
        this.context = context;
    }

    @NonNull
    @Override
    public PromVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_prom_row,parent,false);
        return new PromVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromVH holder, int position) {

        Glide.with(context).load(promotionsList.get(position).getPromotion_img()).into(holder.promImg);
        holder.promNote.setText(promotionsList.get(position).getPromotion_note());

    }

    @Override
    public int getItemCount() {
        return promotionsList == null ? 0 : promotionsList.size();
    }

    public class PromVH extends RecyclerView.ViewHolder {

        ImageView promImg;
        TextView promNote;

        public PromVH(@NonNull View itemView) {
            super(itemView);

            promImg = itemView.findViewById(R.id.ADsimg);
            promNote = itemView.findViewById(R.id.ADNote);

        }
    }
}
