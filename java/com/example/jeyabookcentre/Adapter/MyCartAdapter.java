package com.example.jeyabookcentre.Adapter;

import static java.lang.Float.parseFloat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Cart_Screen;
import com.example.jeyabookcentre.Eventbus.MyUpdateCartEvent;
import com.example.jeyabookcentre.Models.CartModel;
import com.example.jeyabookcentre.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

    private Context context;
    private List<CartModel> cartModelList;

    int totalPrice = 0;
    int totalDiscount = 0;
    int subTotalPrice = 0;

    private String saveCurrentDate,saveCurrentTime,userID;
    private FirebaseUser fUser;
    private DatabaseReference ref;


    public MyCartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;

    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (holder.cartitemImg == null)
        {
            holder.cartitemImg.setImageResource(R.drawable.no_book);
        }
        else
        {
            Glide.with(context).load(cartModelList.get(position).getBookimg()).into(holder.cartitemImg);
        }

        holder.bookName.setText(cartModelList.get(position).getBookname());
        holder.bookAuthor.setText(cartModelList.get(position).getBookauthor());
        holder.totalQty.setText(String.valueOf(cartModelList.get(position).getTotal_quantity()));
        holder.totalPrice.setText(String.valueOf(cartModelList.get(position).getTotal_price()));
        holder.Discounttxt.setText(String.valueOf("- " + cartModelList.get(position).getDiscount() + ".00 Offer"));
        holder.totalQTY.setText(String.valueOf(cartModelList.get(position).getTotal_quantity()));
        //holder.priceTxt.setText(new StringBuilder("Rs. ").append(cartModelList.get(position).getPrice()));

        holder.IncButton.setOnClickListener(view -> {
            plusCartItem(holder,cartModelList.get(position));
        });

        holder.DecButton.setOnClickListener(view -> {
            minusCartItem(holder,cartModelList.get(position));
        });

        holder.deleteImg.setOnClickListener(v-> {
            AlertDialog dialog = new AlertDialog.Builder(context,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
                    .setTitle("Delete Item")
                    .setMessage("Do you want to DELETE this Item?")
                    .setNegativeButton("CANCEL", (dialog1, which) -> dialog1.dismiss())
                    .setPositiveButton("OK", (dialog2, which) -> {

                        //Temp remove
                        notifyItemRemoved(position);

                        deleteFromFirebase(cartModelList.get(position));
                        dialog2.dismiss();
                    }).create();
            dialog.show();
        });



            /*AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Item will be DELETED from the card...")
                    .setPositiveButton("Delete",(dialog1,which) -> dialog1.dismiss())
                    .setNegativeButton("Cancel",(dialog2,which) -> {

                        //Temp remove
                        notifyItemRemoved(position);

                        deletefromFirebase(cartModelList.get(position));
                        dialog2.dismiss();

                    }).create();
            dialog.show();*/

        //Pass total price to cart activity
        totalPrice = totalPrice + cartModelList.get(position).getTotal_price();
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalprice",totalPrice);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        //Pass total quantity to cart activity
       /* totalQty = totalQty + cartModelList.get(position).getTotal_quantity();
        Intent intent1 = new Intent("MyTotalCount");
        intent.putExtra("totalcount",totalQty);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);*/

        totalDiscount = totalDiscount + cartModelList.get(position).getDiscount();
        Intent intent1 = new Intent("MyTotalDiscount");
        intent1.putExtra("totaldiscount",totalDiscount);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

        subTotalPrice = totalPrice - totalDiscount;
        Intent intent2 = new Intent("MySubTotal");
        intent2.putExtra("subtotal",subTotalPrice);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);

    }

    private void minusCartItem(MyCartViewHolder holder, CartModel cartModel) {

        if (cartModel.getTotal_quantity() > 1){
            cartModel.setTotal_quantity(cartModel.getTotal_quantity()-1);
            cartModel.setTotal_price((int) (cartModel.getTotal_quantity()* parseFloat(String.valueOf(cartModel.getBookprice()))));

            //update quantity,price
            holder.totalQTY.setText(new StringBuilder().append(cartModel.getTotal_quantity()));
            holder.totalPrice.setText(new StringBuilder().append(cartModel.getTotal_price()));
            updateFirebase(cartModel);
        }

    }

    private void plusCartItem(MyCartViewHolder holder, CartModel cartModel) {

        cartModel.setTotal_quantity(cartModel.getTotal_quantity()+1);
        cartModel.setTotal_price((int) (cartModel.getTotal_quantity()*Float.parseFloat(String.valueOf(cartModel.getBookprice()))));

        //update quantity,Price
        holder.totalQTY.setText(new StringBuilder().append(cartModel.getTotal_quantity()));
        holder.totalPrice.setText(new StringBuilder().append(cartModel.getTotal_price()));
        updateFirebase(cartModel);

    }

    private void deleteFromFirebase(CartModel cartModel) {

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        FirebaseDatabase.getInstance()
                .getReference("CartList")
                .child(userID)
                .child(cartModel.getKey())
                .removeValue()
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));

    }

    private void updateFirebase(CartModel cartModel)
    {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        FirebaseDatabase.getInstance()
                .getReference("CartList")
                .child(userID)
                .child(cartModel.getKey())
                .setValue(cartModel)
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

   /* private void deletefromFirebase(CartModel cartModel) {

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        FirebaseDatabase.getInstance().getReference("CartList").child(userID).child(cartModel.getBookisbn()).removeValue()
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }*/

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder {

        ImageView cartitemImg,deleteImg,IncButton,DecButton;
        TextView bookName,bookAuthor,totalQty,totalPrice,Discounttxt,totalQTY;

        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);

            cartitemImg = itemView.findViewById(R.id.CartImageView);
            deleteImg = itemView.findViewById(R.id.DeleteBtn);
            DecButton = itemView.findViewById(R.id.decrementBtn1);
            IncButton = itemView.findViewById(R.id.incrementBtn1);

            bookName = itemView.findViewById(R.id.txtbName);
            bookAuthor = itemView.findViewById(R.id.txtbAuthor);
            totalQty = itemView.findViewById(R.id.totalQTY);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            Discounttxt = itemView.findViewById(R.id.discTotPrice);
            totalQTY = itemView.findViewById(R.id.QtyTxt1);

        }
    }
}
