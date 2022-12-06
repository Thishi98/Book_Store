package com.example.jeyabookcentre.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Home_Screen;
import com.example.jeyabookcentre.Models.Promotions;
import com.example.jeyabookcentre.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.sliderViewHolder> {

    private List<Promotions> promotionsList;
    private Context context;

    public SliderAdapter(Context context,List<Promotions> promotionsList) {
        this.promotionsList = promotionsList;
        this.context = context;
    }

    @NonNull
    @Override
    public sliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_row,parent,false);
        return new sliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.sliderViewHolder holder, int position) {

        Glide.with(context).load(promotionsList.get(position).getPromotion_img()).into(holder.promImg);
        holder.promNote.setText(promotionsList.get(position).getPromotion_note());

    }

    @Override
    public int getItemCount() {
        return promotionsList == null ? 5 : promotionsList.size();
    }

    public class sliderViewHolder extends RecyclerView.ViewHolder {

        ImageView promImg;
        TextView promNote;

        public sliderViewHolder(@NonNull View itemView) {
            super(itemView);

            promImg = itemView.findViewById(R.id.ADimg);
            promNote = itemView.findViewById(R.id.promNote);

        }
    }
}
