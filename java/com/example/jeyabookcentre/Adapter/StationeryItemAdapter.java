package com.example.jeyabookcentre.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Models.StationeryModel;
import com.example.jeyabookcentre.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationeryItemAdapter extends RecyclerView.Adapter<StationeryItemAdapter.stationeryViewHolder>{

    private Context context;
    private List<StationeryModel> modelList;

    public StationeryItemAdapter(Context context, List<StationeryModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public stationeryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_new,parent,false);
        return new stationeryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull stationeryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(modelList.get(position).getItemimage()).into(holder.Iimg);
        holder.Iname.setText(modelList.get(position).getItemname());
        holder.Iprice.setText(modelList.get(position).getItemprice());
        holder.IQty.setText(modelList.get(position).getQuantity());

        holder.EditImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DialogPlus dialogPlus = com.orhanobut.dialogplus.DialogPlus.newDialog(holder.Iimg.getContext()).setContentHolder(new ViewHolder(R.layout.dialog_content))
                        .setExpanded(true,900).create();    //dialog start

                View myView = dialogPlus.getHolderView();
                EditText itemName = myView.findViewById(R.id.INameET);
                EditText itemCode = myView.findViewById(R.id.IidET);
                EditText itemprice = myView.findViewById(R.id.IPriceET);
                EditText itemqty = myView.findViewById(R.id.IQtyET);
                EditText itemdiscnote = myView.findViewById(R.id.NoteDisc);
                EditText itemdiscprice = myView.findViewById(R.id.PriceDisc);

                Button save = myView.findViewById(R.id.saveBtn);

                itemName.setText(modelList.get(position).getItemname());
                itemCode.setText(modelList.get(position).getItem_code());
                itemprice.setText(modelList.get(position).getItemprice());
                itemqty.setText(modelList.get(position).getQuantity());
                itemdiscnote.setText(modelList.get(position).getDiscnote());
                itemdiscprice.setText(modelList.get(position).getDiscprice());

                dialogPlus.show();

                //save btn onclick
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String,Object> map = new HashMap<>();
                        map.put("itemname",itemName.getText().toString());
                        map.put("item_code",itemName.getText().toString());
                        map.put("itemprice",itemCode.getText().toString());
                        map.put("quantity",itemprice.getText().toString());
                        map.put("discnote",itemqty.getText().toString());
                        map.put("discprice",itemdiscprice.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Stationery").child(modelList.get(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(context, "Something Wrong...", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });

                    }
                });

            }
        });

        holder.DelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.Iimg.getContext(),R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure to DELETE this Item detail ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Stationery").child(modelList.get(position).getKey())
                                .removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class stationeryViewHolder extends RecyclerView.ViewHolder {

        TextView Iname,Iprice,IQty;
        ImageView Iimg,DelImg,EditImag;

        public stationeryViewHolder(@NonNull View itemView) {
            super(itemView);

            Iname = itemView.findViewById(R.id.bookNameTXT);
            Iprice = itemView.findViewById(R.id.bookPriceTXT);
            IQty = itemView.findViewById(R.id.bookQtyTXT);

            Iimg = itemView.findViewById(R.id.nobookimg);
            DelImg = itemView.findViewById(R.id.deleteImg);
            EditImag = itemView.findViewById(R.id.editImg);

        }
    }
}
