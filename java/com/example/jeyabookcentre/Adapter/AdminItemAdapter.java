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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Models.Category_section;
import com.example.jeyabookcentre.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminItemAdapter extends RecyclerView.Adapter<AdminItemAdapter.AdminItemVH> {

    private Context context;
    private List<Category_section> category_sectionList;

    public AdminItemAdapter(Context context, List<Category_section> category_sectionList) {
        this.context = context;
        this.category_sectionList = category_sectionList;
    }

    @NonNull
    @Override
    public AdminItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_new,parent,false);
        return new AdminItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminItemVH holder, @SuppressLint("RecyclerView") int position) {

        String bookStatus = category_sectionList.get(position).getBookStatus();

        Glide.with(context).load(category_sectionList.get(position).getBookimage()).into(holder.Bimg);
        holder.Bname.setText(category_sectionList.get(position).getBookname());
        holder.Bprice.setText("Rs. " + category_sectionList.get(position).getBookprice() + ".00");
        holder.BQty.setText("Quantity: " + category_sectionList.get(position).getQuantity());
        holder.Bstatus.setText(bookStatus);

        //Changing status text color
        if (bookStatus != null && bookStatus.equalsIgnoreCase("Out-Stock"))
        {
            holder.Bstatus.setTextColor(context.getResources().getColor(R.color.red));
        }
        else if (bookStatus != null && bookStatus.equalsIgnoreCase("In-Stock"))
        {
            holder.Bstatus.setTextColor(context.getResources().getColor(R.color.green));
        }

        holder.EditImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DialogPlus dialogPlus = com.orhanobut.dialogplus.DialogPlus.newDialog(holder.Bimg.getContext()).setContentHolder(new ViewHolder(R.layout.dialog_content))
                        .setExpanded(true,900).create();    //dialog start

                View myView = dialogPlus.getHolderView();
                EditText itemName = myView.findViewById(R.id.INameET);
                EditText ISBN = myView.findViewById(R.id.IidET);
                EditText price = myView.findViewById(R.id.IPriceET);
                EditText qty = myView.findViewById(R.id.IQtyET);
                EditText status = myView.findViewById(R.id.IStatusET);
                EditText discnote = myView.findViewById(R.id.NoteDisc);
                EditText discprice = myView.findViewById(R.id.PriceDisc);

                AppCompatButton save = myView.findViewById(R.id.saveBtn);

                itemName.setText(category_sectionList.get(position).getBookname());
                ISBN.setText(category_sectionList.get(position).getIsbn());
                price.setText(category_sectionList.get(position).getBookprice());
                qty.setText(category_sectionList.get(position).getQuantity());
                status.setText(category_sectionList.get(position).getBookStatus());
                discnote.setText(category_sectionList.get(position).getDiscnote());
                discprice.setText(category_sectionList.get(position).getDiscprice());

                dialogPlus.show();

                //save btn onclick
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String,Object> map = new HashMap<>();
                        map.put("bookname",itemName.getText().toString());
                        map.put("isbn",ISBN.getText().toString());
                        map.put("bookprice",price.getText().toString());
                        map.put("quantity",qty.getText().toString());
                        map.put("bookStatus",status.getText().toString());
                        map.put("discnote",discnote.getText().toString());
                        map.put("discprice",discprice.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("books").child(category_sectionList.get(position).getKey()).updateChildren(map)
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

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.Bimg.getContext(),R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure to DELETE this Item detail ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("books").child(category_sectionList.get(position).getKey())
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
        return category_sectionList.size();
    }

    public class AdminItemVH extends RecyclerView.ViewHolder {

        TextView Bname,Bprice,BQty,Bstatus;
        ImageView Bimg,DelImg,EditImag;

        public AdminItemVH(@NonNull View itemView) {
            super(itemView);

            Bname = itemView.findViewById(R.id.bookNameTXT);
            Bprice = itemView.findViewById(R.id.bookPriceTXT);
            BQty = itemView.findViewById(R.id.bookQtyTXT);
            Bstatus = itemView.findViewById(R.id.bookstatusTXT);

            Bimg = itemView.findViewById(R.id.nobookimg);
            DelImg = itemView.findViewById(R.id.deleteImg);
            EditImag = itemView.findViewById(R.id.editImg);

        }
    }
}
