package com.example.jeyabookcentre;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Adapter.ChatListAdapter;
import com.example.jeyabookcentre.Adapter.RecyclerItemAdapter;
import com.example.jeyabookcentre.Adapter.UserAdapter;
import com.example.jeyabookcentre.Models.Category_section;
import com.example.jeyabookcentre.Models.FavouriteModel;
import com.example.jeyabookcentre.Models.PublisherModel;
import com.example.jeyabookcentre.Models.Users;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Favourite_list extends AppCompatActivity {

    //Firebase initializing
    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userID;

    ImageButton favBack;
    RecyclerView favRecycler;

    RecyclerItemAdapter recyclerItemAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        favBack = findViewById(R.id.favBack);

        favBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Favourite_list.this, Home_Screen.class);
                startActivity(intent);
            }
        });

        favRecycler = findViewById(R.id.favRV);
        favRecycler.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<FavouriteModel> options = new FirebaseRecyclerOptions.Builder<FavouriteModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("FavList").child(userID),FavouriteModel.class).build();

        recyclerItemAdapter = new RecyclerItemAdapter(options);
        favRecycler.setAdapter(recyclerItemAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerItemAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerItemAdapter.stopListening();
    }
}
