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
import com.example.jeyabookcentre.Home_Screen;
import com.example.jeyabookcentre.Models.Promotions;
import com.example.jeyabookcentre.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PromotionAdapter extends FirebaseRecyclerAdapter<Promotions,PromotionAdapter.promotionVH> {

    Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PromotionAdapter(@NonNull FirebaseRecyclerOptions<Promotions> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull promotionVH holder, @SuppressLint("RecyclerView") int position, @NonNull Promotions model) {

        holder.promNote.setText(model.getPromotion_note());
        Glide.with(holder.promImg.getContext()).load(model.getPromotion_img()).into(holder.promImg);

        holder.closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("promotions").child(getRef(position).getKey()).removeValue();
            }
        });

    }

    @NonNull
    @Override
    public promotionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promo_advertise_row,parent,false);
        return new PromotionAdapter.promotionVH(view);
    }

    class promotionVH extends RecyclerView.ViewHolder{

        ImageView closebtn, promImg;
        TextView promNote;

        public promotionVH(@NonNull View itemView) {
            super(itemView);

            closebtn = itemView.findViewById(R.id.closeBTN);
            promImg = itemView.findViewById(R.id.NoAdvertisementIMG);

            promNote = itemView.findViewById(R.id.promNoteTXT);

        }
    }

}
