package com.example.jeyabookcentre.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Models.RequestModel;
import com.example.jeyabookcentre.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class RequestAdapter extends FirebaseRecyclerAdapter<RequestModel,RequestAdapter.RequestVH> {

    private Context context;

    public RequestAdapter(@NonNull FirebaseRecyclerOptions<RequestModel> options, Context context) {
        super(options);
        this.context = context;
    }


    @NonNull
    @Override
    public RequestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_row,parent,false);
        return new RequestVH(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestVH holder, @SuppressLint("RecyclerView") int position, @NonNull RequestModel model) {

        String Rname = model.getRequestBook();
        String RAname = model.getAuthor();
        String Rsuggprice = model.getSuggestprice();
        String status = model.getProgress();

        holder.reqBname.setText(Rname);
        holder.reqAname.setText(RAname);
        holder.reqSuggprice.setText(Rsuggprice);
        holder.reqProgress.setText(status);

        //Changing status text color
        if (status != null && status.equalsIgnoreCase("InProgress"))
        {
            holder.reqProgress.setTextColor(context.getResources().getColor(R.color.red));
        }
        else if (status != null && status.equalsIgnoreCase("Completed"))
        {
            holder.reqProgress.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if (status != null && status.equalsIgnoreCase("Canceled"))
        {
            holder.reqProgress.setTextColor(context.getResources().getColor(R.color.purple_700));
        }

        holder.reqEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogPlus dialogPlus = DialogPlus.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(50,0,50,0)
                        .setContentHolder(new ViewHolder(R.layout.dialog_box_progress))
                        .setExpanded(false)
                        .create();

                View myView = (CardView) dialogPlus.getHolderView();

                EditText status = myView.findViewById(R.id.statusTxt);

                status.setText(model.getProgress());

                AppCompatButton update = myView.findViewById(R.id.btnSubmit);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String,Object> map = new HashMap<>();

                        map.put("Progress",status.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("requests")
                                .child(getRef(position).getKey())
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialogPlus.dismiss();
                                    }
                                });

                    }
                });
                dialogPlus.show();
            }
        });

    }

    public class RequestVH extends RecyclerView.ViewHolder {

        TextView reqBname,reqAname,reqSuggprice,reqProgress;
        ImageView reqEdit;

        public RequestVH(@NonNull View itemView) {
            super(itemView);

            reqBname = itemView.findViewById(R.id.ReqBookName);
            reqAname = itemView.findViewById(R.id.ReqBookAuthor);
            reqSuggprice = itemView.findViewById(R.id.ReqSuggestPrice);
            reqProgress = itemView.findViewById(R.id.ReqProgress);

            reqEdit = itemView.findViewById(R.id.arrow_edit);

        }
    }
}
