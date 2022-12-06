package com.example.jeyabookcentre.Admin_Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Adapter.UserAdapter;
import com.example.jeyabookcentre.Models.Users;
import com.example.jeyabookcentre.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ManageCustomer_Screen extends AppCompatActivity {

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer_screen);

        recyclerView = findViewById(R.id.RVusers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backbtn = findViewById(R.id.custmanBackBTN);

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"),Users.class).build();

        userAdapter = new UserAdapter(options);
        recyclerView.setAdapter(userAdapter);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageCustomer_Screen.this, AdminHome_Screen.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        userAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userAdapter.stopListening();
    }
}