package com.example.jeyabookcentre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jeyabookcentre.Adapter.ChatListAdapter;
import com.example.jeyabookcentre.Adapter.PromAdapter;
import com.example.jeyabookcentre.Adapter.PromotionAdapter;
import com.example.jeyabookcentre.Adapter.SliderAdapter;
import com.example.jeyabookcentre.Models.Promotions;
import com.example.jeyabookcentre.Models.PublisherModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ViewAllProm_Customer extends AppCompatActivity {

    RecyclerView PromrecyclerView;
    ImageView promBackbtn;
    PromAdapter promAdapter;
    List<Promotions> promotionsModelList;

    private DatabaseReference databaseReference;
    private FirebaseDatabase ref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_prom_customer);

        ref = FirebaseDatabase.getInstance();

        PromrecyclerView = findViewById(R.id.promotionsRec);
        PromrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        promBackbtn = findViewById(R.id.PromBack);

        promBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAllProm_Customer.this, Home_Screen.class);
                startActivity(intent);
            }
        });

        promotionsModelList = new ArrayList<>();

        loadPromotionsforCustomer();

    }

    private void loadPromotionsforCustomer()
    {
        ref.getReference("promotions").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    for (DataSnapshot ds: task.getResult().getChildren())
                    {
                        Promotions promotionsModel = ds.getValue(Promotions.class);
                        promotionsModelList.add(promotionsModel);
                        promAdapter = new PromAdapter(ViewAllProm_Customer.this,promotionsModelList);
                        PromrecyclerView.setAdapter(promAdapter);
                    }
                }
                else
                {
                    Toast.makeText(ViewAllProm_Customer.this,"Something Wrong!...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}