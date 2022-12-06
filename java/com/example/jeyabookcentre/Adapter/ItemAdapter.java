package com.example.jeyabookcentre.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Eventbus.MyUpdateItemEvent;
import com.example.jeyabookcentre.Item_View_Screen;
import com.example.jeyabookcentre.Listners.IRecyclerViewClickListner;
import com.example.jeyabookcentre.Models.Category_section;
import com.example.jeyabookcentre.R;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.itemViewHolder> {

    private FirebaseUser fUser;
    private DatabaseReference ref;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String userID;

    private Context context;
    private List<Category_section> category_sectionList;

    public ItemAdapter(Context context, List<Category_section> category_sectionList) {
        this.context = context;
        this.category_sectionList = category_sectionList;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rows,parent,false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(category_sectionList.get(position).getBookimage()).into(holder.bImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Item_View_Screen.class);
                intent.putExtra("info",category_sectionList.get(position));
                context.startActivity(intent);

            }
        });

        holder.heartclick.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {

                if (holder.heartclick.callOnClick())
                {
                    addtoFavourite(holder,category_sectionList.get(position));
                }
                else
                {
                    Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                }
                //add to favourite list
            }
        });

      /*  holder.heartclick.setOnFavoriteAnimationEndListener(new MaterialFavoriteButton.OnFavoriteAnimationEndListener() {
            @Override
            public void onAnimationEnd(MaterialFavoriteButton buttonView, boolean favorite) {

                //remove from favourite list
                if (holder.heartclick.callOnClick())
                {
                    removefromFavorite(holder,category_sectionList.get(position));
                    Toast.makeText(context, "Removed from Favourites...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }

   /* private void removefromFavorite(itemViewHolder holder, Category_section category_section) {

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        FirebaseDatabase.getInstance()
                .getReference("FavList")
                .child(userID)
                .child(category_section.getKey())
                .removeValue()
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateItemEvent()));

    }*/

    private void addtoFavourite(itemViewHolder holder, Category_section category_section) {

        String saveCurrentDate,saveCurrentTime;

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String favRandomKey = saveCurrentDate + saveCurrentTime;

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        final HashMap<String,Object> favMap = new HashMap<>();

        DatabaseReference FavList = FirebaseDatabase.getInstance().getReference("FavList").child(userID).child(favRandomKey);

        FavList.child(category_section.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    /*FirebaseDatabase.getInstance()
                            .getReference("FavList")
                            .child(userID)
                            .child(category_section.getKey())
                            .removeValue()
                            .addOnFailureListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateItemEvent()));*/

                    Toast.makeText(context,"Item already added to favourites...",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    favMap.put("Fisbn",category_section.getIsbn());
                    favMap.put("Fbname",category_section.getBookname());
                    favMap.put("Fauthor",category_section.getBookauthor());
                    favMap.put("Fpublish",category_section.getBookpublisher());
                    favMap.put("Fprice",category_section.getBookprice());
                    favMap.put("Fsubcat",category_section.getSubbookcategory());
                    favMap.put("Fimg",category_section.getBookimage());

                    FavList.updateChildren(favMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context,"Added as Favourite...",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });

       /* FirebaseDatabase.getInstance().getReference("FavList").child(userID)
                .child(category_section.getKey())
                .setValue(category_section.getIsbn(),category_section.getBookname())
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateItemEvent()));*/


    }

    @Override
    public int getItemCount() {
        return category_sectionList.size();
    }

    public class itemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.itemImg)
        ImageView bImage;
        @BindView(R.id.hartbtn)
        MaterialFavoriteButton heartclick;

        IRecyclerViewClickListner listner;

        public void setListner(IRecyclerViewClickListner listner) {
            this.listner = listner;
        }

        private Unbinder unbinder;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);

            unbinder = ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listner.onRecyclerClick(v,getAdapterPosition());
        }
    }
}
