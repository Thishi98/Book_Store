package com.example.jeyabookcentre.Admin_Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Adapter.OrderViewAdapter;
import com.example.jeyabookcentre.Models.OrdHistory;
import com.example.jeyabookcentre.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Customer_orderlist_Screen extends AppCompatActivity {

    RecyclerView orderRecycler;
    ImageButton backadmin;

    private DatabaseReference databaseReference;
    private FirebaseDatabase ref;
    private FirebaseUser fUser;
    private String userID,orderID;

    OrderViewAdapter orderViewAdapter;
    List<OrdHistory> ordHistories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orderlist_screen);

        ref = FirebaseDatabase.getInstance();

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        backadmin = findViewById(R.id.orderBackadmin);

        backadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        orderRecycler = findViewById(R.id.orderRVadmin);
        orderRecycler.setLayoutManager(new LinearLayoutManager(this));

        ordHistories = new ArrayList<>();

        loadOrdersfromAdmin();

    }

    private void loadOrdersfromAdmin()
    {
        ref.getReference("Orders").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (task.isSuccessful())
                        {
                            for (DataSnapshot ordersnapshot : task.getResult().getChildren())
                            {
                                OrdHistory ordHistory = ordersnapshot.getValue(OrdHistory.class);
                                ordHistories.add(ordHistory);
                                orderViewAdapter = new OrderViewAdapter(Customer_orderlist_Screen.this,ordHistories);
                                orderRecycler.setAdapter(orderViewAdapter);

                            }
                        }
                        else
                        {
                            Toast.makeText(Customer_orderlist_Screen.this,"Something Wrong!...",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}