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
import com.example.jeyabookcentre.Chat_Screen;
import com.example.jeyabookcentre.Models.PublisherModel;
import com.example.jeyabookcentre.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>{

    private Context context;
    private List<PublisherModel> publisherModelList;

    public ChatListAdapter(Context context, List<PublisherModel> publisherModelList) {
        this.context = context;
        this.publisherModelList = publisherModelList;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.publisher_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        PublisherModel publisherModel = publisherModelList.get(position);

        Glide.with(context).load(publisherModelList.get(position).getDp()).into(holder.PubDP);
        holder.companyName.setText(publisherModelList.get(position).getPubName());
        holder.CompanyStatus.setText(publisherModelList.get(position).getPubStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chat_Screen.class);
                intent.putExtra("pubName",publisherModel.getPubName());
                intent.putExtra("pubImg",publisherModel.getDp());
                intent.putExtra("pubID",publisherModel.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return publisherModelList.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {

        //View of publisher_row.xml
        ImageView PubDP;
        TextView companyName,CompanyStatus;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);

            //Init view
            PubDP = itemView.findViewById(R.id.pubDP);
            companyName = itemView.findViewById(R.id.CompanyNameTXT);
            CompanyStatus = itemView.findViewById(R.id.pubStatusTXT);

        }
    }
}
