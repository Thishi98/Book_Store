package com.example.jeyabookcentre.Admin_Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeyabookcentre.Adapter.OrderViewListAdapter;
import com.example.jeyabookcentre.Models.Category_section;
import com.example.jeyabookcentre.Models.OrdHistory;
import com.example.jeyabookcentre.Models.OrderModel;
import com.example.jeyabookcentre.R;
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

public class Customer_Order_Details extends AppCompatActivity {

    RecyclerView CustOrderRecyler;

    private DatabaseReference databaseReference;
    private FirebaseDatabase ref;
    private FirebaseUser fUser;
    private String userID,orderedID,UserID;
    private TextView orderedid,Date,Contact,Total,Status;
    private EditText saveStatuses;
    private ImageView saveicon;
    private ImageButton backcustOrder;
    ImageButton custOrdBack;

    OrdHistory order_model = null;

    List<OrderModel> orderModelList;
    OrderViewListAdapter orderViewListAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_details);

        //firebase init
        ref = FirebaseDatabase.getInstance();
        //getting current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();
        //init
        custOrdBack = findViewById(R.id.CustordBackBTN);
        orderedid = findViewById(R.id.OrdID);
        Date = findViewById(R.id.Datetxt);
        Contact = findViewById(R.id.phonetxt);
        Total = findViewById(R.id.Totaltxt);
        Status = findViewById(R.id.Statustxt);
        saveStatuses = findViewById(R.id.StatusChange);
        saveicon = findViewById(R.id.saveIcon);

        saveicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (saveStatuses == null) {

                    saveStatuses.setError("Status type is empty!");
                }
                else
                {
                    ref.getReference("OrderList").child(UserID).child(orderedID).child("order_status")
                            .setValue(saveStatuses.getText().toString());
                    saveStatuses.setText("");
                }

            }
        });

        custOrdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        CustOrderRecyler = findViewById(R.id.ordersRV);
        CustOrderRecyler.setLayoutManager(new LinearLayoutManager(this));

        orderModelList = new ArrayList<>();

        //capture the passing data
        /*final Object object = getIntent().getSerializableExtra("order_id");
        if (object instanceof OrdHistory){
            order_model = (OrdHistory) object;
        }

        if (order_model != null)
        {
            orderedid.setText(order_model.getOrder_id());
            Date.setText(order_model.getOrder_date());
            Contact.setText(order_model.getCust_phone());
            Total.setText(order_model.getTotal_price());
            Status.setText(order_model.getOrder_id());
        }*/

        Intent intent = getIntent();
        orderedID = intent.getStringExtra("orderid");
        UserID = intent.getStringExtra("orderedby");

        loadCustOrderlist();
        loadCustOrderDetails();

    }

    private void loadCustOrderDetails()
    {

        ref.getReference("OrderList").child(UserID).child(orderedID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get data
                        String Order_ID = " "+snapshot.child("order_id").getValue();
                        String Ordered_Date = " "+snapshot.child("order_date").getValue();
                        String Phone = " "+snapshot.child("cust_phone").getValue();
                        String Total_Price = " "+snapshot.child("Total_price").getValue();
                        String Progress = " "+snapshot.child("order_status").getValue();

                        if (Progress != null && Progress.equalsIgnoreCase("InProgress"))
                        {
                            Status.setTextColor(getResources().getColor(R.color.red));
                        }
                        else if (Progress != null && Progress.equalsIgnoreCase("Completed"))
                        {
                            Status.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if (Progress != null && Progress.equalsIgnoreCase("Canceled"))
                        {
                            Status.setTextColor(getResources().getColor(R.color.purple_700));
                        }

                        //set data
                        orderedid.setText(Order_ID);
                        Date.setText(Ordered_Date);
                        Contact.setText(Phone);
                        Total.setText(Total_Price);
                        Status.setText(Progress);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadCustOrderlist()
    {
        ref.getReference("OrderList").child(UserID).child(orderedID).child("CartList_Items")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (DataSnapshot orderlistsnapshot : task.getResult().getChildren())
                            {
                                OrderModel orderModel = orderlistsnapshot.getValue(OrderModel.class);
                                orderModelList.add(orderModel);
                                orderViewListAdapter = new OrderViewListAdapter(Customer_Order_Details.this,orderModelList);
                                CustOrderRecyler.setAdapter(orderViewListAdapter);
                            }
                        }
                        else
                        {
                            Toast.makeText(Customer_Order_Details.this,"Something Wrong!...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}