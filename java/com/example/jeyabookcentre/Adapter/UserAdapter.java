package com.example.jeyabookcentre.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Models.Users;
import com.example.jeyabookcentre.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class UserAdapter extends FirebaseRecyclerAdapter <Users,UserAdapter.UserViewHolder>
{

    Context context;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserAdapter(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Users model) {

        String firstname = model.getfName();
        String lastname = model.getlName();
        String prefix = firstname.charAt(0)+ ""+lastname.charAt(0);
        String customername = firstname+" "+lastname;

        holder.CustName.setText(customername);
        holder.CustEmail.setText(model.getEmail());
        holder.Prefix.setText(prefix);

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(getRef(position).getKey()).removeValue();

            }
        });

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row,parent,false);
        return new UserViewHolder(view);
    }

    class UserViewHolder extends RecyclerView.ViewHolder
   {
       private TextView CustName,CustEmail,Prefix;
       private ImageView deletebtn;


       public UserViewHolder(@NonNull View itemView) {
           super(itemView);

           CustName = (TextView)itemView.findViewById(R.id.cusNameTXT);
           CustEmail = (TextView)itemView.findViewById(R.id.cusMailTXT);
           Prefix = (TextView)itemView.findViewById(R.id.prefixTV);

           deletebtn = (ImageView)itemView.findViewById(R.id.deleteImg);

       }
   }
}
