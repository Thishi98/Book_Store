package com.example.jeyabookcentre.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Models.ReqSupplyModel;
import com.example.jeyabookcentre.Models.RequestModel;
import com.example.jeyabookcentre.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ViewRequestAdapter extends FirebaseRecyclerAdapter<RequestModel,ViewRequestAdapter.ViewRequestVH> {
    private Context context;

    private String CurrentDate,CurrentTime,RequestRandKey,Rname,RAname,Rsuggprice,status;
    private EditText type,Quntity,price;

    DialogPlus dialogPlus;
    private FirebaseUser fUser;
    private DatabaseReference ref;
    private String userID;

    ArrayList<ReqSupplyModel> reqSupplyModelList;

    public ViewRequestAdapter(@NonNull FirebaseRecyclerOptions<RequestModel> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewRequestVH holder, @SuppressLint("RecyclerView") int position, @NonNull RequestModel model) {

        Rname = model.getRequestBook();
        RAname = model.getAuthor();
        Rsuggprice = model.getSuggestprice();
        status = model.getProgress();

        holder.reqBname.setText(Rname);
        holder.reqAname.setText(RAname);
        holder.reqSuggprice.setText(Rsuggprice);
        holder.reqProgress.setText(status);

        holder.arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogPlus = DialogPlus.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(50,0,50,0)
                        .setContentHolder(new ViewHolder(R.layout.dialog_box_request_sent))
                        .setExpanded(false)
                        .create();

                View myView = (CardView) dialogPlus.getHolderView();

                type = myView.findViewById(R.id.AgreeTxt);
                Quntity = myView.findViewById(R.id.QuantityTxt);
                price = myView.findViewById(R.id.AmountTxt);
                type.setText("Agreed");

                AppCompatButton submit = myView.findViewById(R.id.Submit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Calendar calendar = Calendar.getInstance();

                        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
                        CurrentDate = currentDate.format(calendar.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        CurrentTime = currentTime.format(calendar.getTime());

                        RequestRandKey = CurrentDate + CurrentTime;

                        fUser = FirebaseAuth.getInstance().getCurrentUser();
                        ref = FirebaseDatabase.getInstance().getReference("Publishers");
                        userID = fUser.getUid();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("requestBook", "" + Rname);
                        hashMap.put("requestBookAuthor", "" + RAname);
                        hashMap.put("requestSuggPrice", "" + Rsuggprice);
                        hashMap.put("agreement", "" + type);
                        hashMap.put("supplyQuntity", "" + Quntity);
                        hashMap.put("supplyPrice", "" + price);
                        hashMap.put("supplier", "" + userID);

                        DatabaseReference suppyreference = FirebaseDatabase.getInstance().getReference("RequestSupply");

                        suppyreference.child(RequestRandKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(context,"Supply approval will send shortly!",Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(context,"Something wrong!",Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }
                            }
                        });
                        dialogPlus.show();

                    }
                });

            }
        });

    }

    /*private void RequestCompleteFirebase(int position)
    {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        CurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = currentTime.format(calendar.getTime());

        RequestRandKey = CurrentDate + CurrentTime;

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Publishers");
        userID = fUser.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("requestBook", "" + Rname);
        hashMap.put("requestBookAuthor", "" + RAname);
        hashMap.put("requestSuggPrice", "" + Rsuggprice);
        hashMap.put("agreement", "" + type);
        hashMap.put("supplyQuntity", "" + Quntity);
        hashMap.put("supplyPrice", "" + price);
        hashMap.put("supplier", "" + userID);

        DatabaseReference suppyreference = FirebaseDatabase.getInstance().getReference("RequestSupply");

        suppyreference.child(RequestRandKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(context,"Supply approval will send shortly!",Toast.LENGTH_SHORT).show();
                    dialogPlus.dismiss();
                }
                else
                {
                    Toast.makeText(context,"Something wrong!",Toast.LENGTH_SHORT).show();
                    dialogPlus.dismiss();
                }
            }
        });

        dialogPlus.show();
    }*/

    @NonNull
    @Override
    public ViewRequestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_request_row,parent,false);
        return new ViewRequestVH(view);

    }

    public class ViewRequestVH extends RecyclerView.ViewHolder {

        TextView reqBname,reqAname,reqSuggprice,reqProgress;
        ImageView arrow_right;

        public ViewRequestVH(@NonNull View itemView) {
            super(itemView);

            reqBname = itemView.findViewById(R.id.BookNameReq);
            reqAname = itemView.findViewById(R.id.BookAuthorReq);
            reqSuggprice = itemView.findViewById(R.id.SuggestPriceReq);
            reqProgress = itemView.findViewById(R.id.ProgressReq);

            arrow_right = itemView.findViewById(R.id.arrow_right);

        }
    }
}
