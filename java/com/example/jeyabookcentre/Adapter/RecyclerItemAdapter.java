package com.example.jeyabookcentre.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Models.FavouriteModel;
import com.example.jeyabookcentre.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecyclerItemAdapter extends FirebaseRecyclerAdapter<FavouriteModel,RecyclerItemAdapter.RecyclerVH> {

    private Context context;
   // private List<FavouriteModel> favouriteModelList;

    private String userID;
    private FirebaseUser fUser;
    private DatabaseReference ref;

    public RecyclerItemAdapter(@NonNull FirebaseRecyclerOptions<FavouriteModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public RecyclerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_row, parent, false);
        return new RecyclerVH(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerVH holder, @SuppressLint("RecyclerView") int position, @NonNull FavouriteModel model) {

        Glide.with(holder.favBookImg.getContext()).load(model.getFimg()).into(holder.favBookImg);
        holder.favBname.setText(model.getFbname());
        holder.favBprice.setText(model.getFprice());
        holder.favBauhtor.setText(model.getFauthor());

        holder.closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fUser = FirebaseAuth.getInstance().getCurrentUser();
                ref = FirebaseDatabase.getInstance().getReference("Users");
                userID = fUser.getUid();

                FirebaseDatabase.getInstance().getReference().child("FavList").child(userID)
                        .child(getRef(position).getKey()).removeValue();
            }
        });

    }


    public class RecyclerVH extends RecyclerView.ViewHolder {

        ImageView favBookImg,closeImg;
        TextView favBname,favBprice,favBauhtor;

        public RecyclerVH(@NonNull View itemView) {
            super(itemView);

            favBookImg = itemView.findViewById(R.id.favImg);
            closeImg = itemView.findViewById(R.id.closeImg);
            favBname = itemView.findViewById(R.id.favBook);
            favBprice = itemView.findViewById(R.id.favBookprice);
            favBauhtor = itemView.findViewById(R.id.favBookauthor);
        }
    }
}
