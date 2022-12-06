package com.example.jeyabookcentre.Publisher_Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.jeyabookcentre.Adapter.RequestAdapter;
import com.example.jeyabookcentre.Adapter.ViewRequestAdapter;
import com.example.jeyabookcentre.Admin_Screens.AdminHome_Screen;
import com.example.jeyabookcentre.Admin_Screens.View_All_Book_Requests;
import com.example.jeyabookcentre.Models.RequestModel;
import com.example.jeyabookcentre.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Requests_Screen extends AppCompatActivity {

    private ImageButton backbutton;
    private RecyclerView requestRec;
    ViewRequestAdapter viewRequestAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_screen);

        backbutton = findViewById(R.id.BackBTNrequest);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Requests_Screen.this, Publisher_Items_Screen.class);
                startActivity(intent);
            }
        });

        requestRec = findViewById(R.id.requestsRV);
        requestRec.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<RequestModel> context = new FirebaseRecyclerOptions.Builder<RequestModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("requests"),RequestModel.class).build();

        viewRequestAdapter = new ViewRequestAdapter(context,this);
        requestRec.setAdapter(viewRequestAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        viewRequestAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewRequestAdapter.stopListening();
    }
}