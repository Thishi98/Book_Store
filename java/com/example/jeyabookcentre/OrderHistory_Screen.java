package com.example.jeyabookcentre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jeyabookcentre.Adapter.OrderAdapter;
import com.example.jeyabookcentre.Models.OrdHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrderHistory_Screen extends AppCompatActivity {

    RecyclerView orderRecycler;
    ImageButton back;

    private DatabaseReference databaseReference;
    private FirebaseDatabase ref;
    private FirebaseUser fUser;
    private String userID;

    OrderAdapter orderAdapter;
    List<OrdHistory> ordHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_screen);

        ref = FirebaseDatabase.getInstance();

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        back = findViewById(R.id.orderBackBTN);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        orderRecycler = findViewById(R.id.orderRV);
        orderRecycler.setLayoutManager(new LinearLayoutManager(this));

        ordHistoryList = new ArrayList<>();

        loadOrders();

    }

    private void loadOrders() {

        ref.getReference("OrderList").child(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (DataSnapshot ordersnapshot : task.getResult().getChildren())
                            {
                                OrdHistory ordHistory = ordersnapshot.getValue(OrdHistory.class);
                                ordHistoryList.add(ordHistory);
                                orderAdapter = new OrderAdapter(OrderHistory_Screen.this,ordHistoryList);
                                orderRecycler.setAdapter(orderAdapter);

                            }
                        }
                        else
                        {
                            Toast.makeText(OrderHistory_Screen.this,"Something Wrong!...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}