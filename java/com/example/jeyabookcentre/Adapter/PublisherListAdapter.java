package com.example.jeyabookcentre.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Models.PublisherModel;
import com.example.jeyabookcentre.Publisher_Screens.Publisher_Login_Screen;
import com.example.jeyabookcentre.R;

import java.util.List;

public class PublisherListAdapter extends RecyclerView.Adapter<PublisherListAdapter.PublisherViewHolder> {

    private Context context;
    private List<PublisherModel> publisherModelList;

    public PublisherListAdapter(Context context, List<PublisherModel> publisherModelList) {
        this.context = context;
        this.publisherModelList = publisherModelList;
    }

    @NonNull
    @Override
    public PublisherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PublisherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.publisher_row,parent,false));  //inflate layout
    }

    @Override
    public void onBindViewHolder(@NonNull PublisherListAdapter.PublisherViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //Picasso.get().load(publisherModelList.get(position).getDp()).into(holder.PubDP);
        Glide.with(context).load(publisherModelList.get(position).getDp()).into(holder.PubDP);
        holder.companyName.setText(publisherModelList.get(position).getPubName());
        holder.CompanyStatus.setText(publisherModelList.get(position).getPubStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Publisher_Login_Screen.class);
                intent.putExtra("publisher_ID",publisherModelList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return publisherModelList.size();
    }

    public class PublisherViewHolder extends RecyclerView.ViewHolder {

        //View of publisher_row.xml
        ImageView PubDP;
        TextView companyName,CompanyStatus;

        public PublisherViewHolder(@NonNull View itemView) {
            super(itemView);

            //Init view
            PubDP = itemView.findViewById(R.id.pubDP);
            companyName = itemView.findViewById(R.id.CompanyNameTXT);
            CompanyStatus = itemView.findViewById(R.id.pubStatusTXT);

        }
    }
}
