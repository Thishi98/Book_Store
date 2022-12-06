package com.example.jeyabookcentre.Admin_Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.jeyabookcentre.Adapter.PromotionAdapter;
import com.example.jeyabookcentre.Models.Promotions;
import com.example.jeyabookcentre.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class SeeAllPromotions_Screen extends AppCompatActivity {

    RecyclerView PromrecyclerView;
    ImageView promBackbtn;
    PromotionAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_promotions_screen);

        PromrecyclerView = findViewById(R.id.RVpromotions);
        PromrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Promotions> options = new FirebaseRecyclerOptions.Builder<Promotions>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("promotions"),Promotions.class).build();

        pAdapter = new PromotionAdapter(options);
        PromrecyclerView.setAdapter(pAdapter);

        promBackbtn = findViewById(R.id.PromBackBTN);

        promBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeAllPromotions_Screen.this, AdminHome_Screen.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        pAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pAdapter.stopListening();
    }
}