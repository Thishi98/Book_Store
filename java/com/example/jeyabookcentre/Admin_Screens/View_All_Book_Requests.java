package com.example.jeyabookcentre.Admin_Screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Adapter.RequestAdapter;
import com.example.jeyabookcentre.Models.RequestModel;
import com.example.jeyabookcentre.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class View_All_Book_Requests extends AppCompatActivity {

    private ImageButton backbutton;
    private RecyclerView requestRec;
    RequestAdapter requestAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_book_requests);


        backbutton = findViewById(R.id.requestBackBTN);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_All_Book_Requests.this, AdminHome_Screen.class);
                startActivity(intent);
            }
        });

        requestRec = findViewById(R.id.RVrequests);
        requestRec.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<RequestModel> context = new FirebaseRecyclerOptions.Builder<RequestModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("requests"),RequestModel.class).build();

        requestAdapter = new RequestAdapter(context,this);
        requestRec.setAdapter(requestAdapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        requestAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        requestAdapter.stopListening();
    }
}