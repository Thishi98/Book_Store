package com.example.jeyabookcentre.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Models.Category_section;
import com.example.jeyabookcentre.R;

import java.util.List;

public class RecommenderAdapter extends RecyclerView.Adapter<RecommenderAdapter.RecommenderVH> {

    private Context context;
    private List<Category_section> category_sectionList;

    public RecommenderAdapter(Context context, List<Category_section> category_sectionList) {
        this.context = context;
        this.category_sectionList = category_sectionList;
    }

    @NonNull
    @Override
    public RecommenderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommender_row,parent,false);
        return new RecommenderVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommenderVH holder, int position) {
        Glide.with(context).load(category_sectionList.get(position).getBookimage()).into(holder.recommenderImg);
    }

    @Override
    public int getItemCount() {
        return category_sectionList == null ? 0 : category_sectionList.size();
    }

    public class RecommenderVH extends RecyclerView.ViewHolder {

        ImageView recommenderImg;

        public RecommenderVH(@NonNull View itemView) {
            super(itemView);

            recommenderImg = itemView.findViewById(R.id.recImg);

        }
    }
}
