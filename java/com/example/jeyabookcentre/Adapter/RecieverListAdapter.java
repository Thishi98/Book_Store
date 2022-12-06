package com.example.jeyabookcentre.Adapter;

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
import com.example.jeyabookcentre.Publisher_Screens.Chat_publisher_Screen;
import com.example.jeyabookcentre.Models.Users;
import com.example.jeyabookcentre.Publisher_Screens.Publisher_UserList_Screen;
import com.example.jeyabookcentre.R;

import java.util.List;

public class RecieverListAdapter extends RecyclerView.Adapter<RecieverListAdapter.RecieverVH>{

    private Context context;
    private List<Users> usersList;

    public RecieverListAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public RecieverVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecieverVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.recieverlist_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecieverListAdapter.RecieverVH holder, int position) {

        Users users = usersList.get(position);
        String fName = users.getfName();
        String lName = users.getlName();
        String fullname = fName + " " + lName;

        Glide.with(context).load(usersList.get(position).getDp()).into(holder.userDP);
        //holder.userName.setText(usersList.get(position).getfName());
        holder.userName.setText(fullname);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chat_publisher_Screen.class);
                intent.putExtra("userName",users.getfName()+ " " +users.getlName());
                intent.putExtra("userImg",users.getDp());
                intent.putExtra("userID",users.getId());
                context.startActivity(intent);

                Intent cahtsintent = new Intent(context, Publisher_UserList_Screen.class);
                cahtsintent.putExtra("usersID",users.getId());
                context.startActivity(cahtsintent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class RecieverVH extends RecyclerView.ViewHolder {

        //View of publisher_row.xml
        ImageView userDP;
        TextView userName;

        public RecieverVH(@NonNull View itemView) {
            super(itemView);

            //Init view
            userDP = itemView.findViewById(R.id.UserDP);
            userName = itemView.findViewById(R.id.UserNameTXT);

        }
    }
}
