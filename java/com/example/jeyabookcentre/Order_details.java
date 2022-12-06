package com.example.jeyabookcentre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeyabookcentre.Adapter.OrderListAdapter;
import com.example.jeyabookcentre.Models.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Order_details extends AppCompatActivity {

    RecyclerView OrderListRecyler;

    private DatabaseReference databaseReference;
    private FirebaseDatabase ref;
    private FirebaseUser fUser;
    private String userID,orderID;
    private TextView orderid,date,contact,total,status;
    private ImageView Info;
    private ImageButton backorder;
    AlertDialog dialog;

    OrderListAdapter orderListAdapter;
    List<OrderModel> orderlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        //init
        orderid = findViewById(R.id.OrderID);
        date = findViewById(R.id.Date);
        contact = findViewById(R.id.phoneNumber);
        total = findViewById(R.id.Total);
        status = findViewById(R.id.Status);
        //back button init
        backorder = findViewById(R.id.ordListBackBTN);
        backorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //info icon function
        Info = findViewById(R.id.infoIcon);
        Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Order_details.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);  //display screen init
                builder.setTitle("Info");   //dialog box title
                builder.setMessage("Contact number can changed by updating your profile");    //dialog message
                builder.setCancelable(false);   //setting background touch cancel

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {  //setting positive button for alert dialog
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Order_details.this,User_Profile.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {   //setting positive button for alert dialog
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        Intent intent = new Intent(Order_details.this,OrderHistory_Screen.class);
                        startActivity(intent);
                    }
                });
                builder.create().show();   //dialog box show
            }
        });


        //firebase init
        ref = FirebaseDatabase.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        //recyclerview layout setting
        OrderListRecyler = findViewById(R.id.orderListRV);
        OrderListRecyler.setLayoutManager(new LinearLayoutManager(this));

        orderlist = new ArrayList<>();

        //getting order id from order list
        Intent intent = getIntent();
        orderID = intent.getStringExtra("orderid");

        loadOrderList();
        loadOrderDetails();

    }

    //setting order common details by getting data from firebase orderlist table
    private void loadOrderDetails()
    {

        ref.getReference("OrderList").child(userID).child(orderID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get data
                        String OrderID = " "+snapshot.child("order_id").getValue();
                        String OrderedDate = " "+snapshot.child("order_date").getValue();
                        String Contact = " "+snapshot.child("cust_phone").getValue();
                        String Total = " "+snapshot.child("Total_price").getValue();
                        String Status = " "+snapshot.child("order_status").getValue();

                        if (Status != null && Status.equalsIgnoreCase("InProgress"))
                        {
                            status.setTextColor(getResources().getColor(R.color.red));
                        }
                        else if (Status != null && Status.equalsIgnoreCase("Completed"))
                        {
                            status.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if (Status != null && Status.equalsIgnoreCase("Canceled"))
                        {
                            status.setTextColor(getResources().getColor(R.color.purple_700));
                        }

                        //set data
                        orderid.setText(OrderID);
                        date.setText(OrderedDate);
                        contact.setText(Contact);
                        total.setText(Total);
                        status.setText(Status);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    //getting ordered items to the recylerview from firebase ordedrlist table
    private void loadOrderList() {

        ref.getReference("OrderList").child(userID).child(orderID).child("CartList_Items")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (DataSnapshot orderlistsnapshot : task.getResult().getChildren())
                            {
                                OrderModel orderModel = orderlistsnapshot.getValue(OrderModel.class);
                                orderlist.add(orderModel);
                                orderListAdapter = new OrderListAdapter(Order_details.this,orderlist);
                                OrderListRecyler.setAdapter(orderListAdapter);
                            }
                        }
                        else
                        {
                            Toast.makeText(Order_details.this,"Something Wrong!...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}