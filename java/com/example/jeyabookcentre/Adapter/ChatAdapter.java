package com.example.jeyabookcentre.Adapter;

import static com.example.jeyabookcentre.Chat_Screen.rImage;
import static com.example.jeyabookcentre.Chat_Screen.sImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Models.MessageModel;
import com.example.jeyabookcentre.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<MessageModel> messageArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECIEVE = 2;

    public ChatAdapter(Context context, ArrayList<MessageModel> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SEND)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_row,parent,false);
            return new SenderViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_row,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messages = messageArrayList.get(position);

        if (holder.getClass() == SenderViewHolder.class)
        {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;

            viewHolder.txtmessage.setText(messages.getMessage());

            Picasso.get().load(sImage).into(viewHolder.circleImageView);

        }
        else
        {
            RecieverViewHolder viewHolder = (RecieverViewHolder) holder;

            viewHolder.txtmessage.setText(messages.getMessage());

            Picasso.get().load(rImage).into(viewHolder.circleImageView);
        }

    }

    @Override
    public int getItemCount() {
        return messageArrayList == null ? 0 : messageArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String messageModel = messageArrayList.get(position).getSenderID();

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messageModel))
        {
            return ITEM_SEND;
        }
        else
        {
            return ITEM_RECIEVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txtmessage;

      public SenderViewHolder(@NonNull View itemView) {
          super(itemView);

          circleImageView = itemView.findViewById(R.id.ProfileImg);
          txtmessage = itemView.findViewById(R.id.txtMessage);

      }
  }

  class RecieverViewHolder extends RecyclerView.ViewHolder {

      CircleImageView circleImageView;
      TextView txtmessage;

      public RecieverViewHolder(@NonNull View itemView) {
          super(itemView);

          circleImageView = itemView.findViewById(R.id.ProfileImg);
          txtmessage = itemView.findViewById(R.id.txtMessage);

      }
  }
}
