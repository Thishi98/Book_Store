package com.example.jeyabookcentre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jeyabookcentre.Adapter.ChatAdapter;
import com.example.jeyabookcentre.Adapter.ChatListAdapter;
import com.example.jeyabookcentre.Adapter.PublisherListAdapter;
import com.example.jeyabookcentre.Models.Category_section;
import com.example.jeyabookcentre.Models.PublisherModel;
import com.example.jeyabookcentre.Models.Users;
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

public class Chat_Main_Screen extends AppCompatActivity {

    RecyclerView ChatRec;
    ImageView backChat;
    ChatListAdapter listAdapter;
    List<PublisherModel> publisherModelList;

    private String uid;

    private DatabaseReference databaseReference;
    private FirebaseDatabase ref;
    private FirebaseUser fUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main_screen);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        uid = fUser.getUid();


        backChat = findViewById(R.id.Back_chat);

        backChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat_Main_Screen.this, Home_Screen.class);
                startActivity(intent);
            }
        });

        ChatRec = findViewById(R.id.chtMainRecycler);
        ChatRec.setLayoutManager(new LinearLayoutManager(this));

        ref = FirebaseDatabase.getInstance();

        publisherModelList = new ArrayList<>();

        loadPublisherchatList();

    }

    private void loadPublisherchatList()
    {
        ref.getReference("Publishers").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    for (DataSnapshot ds: task.getResult().getChildren())
                    {
                        PublisherModel publisherModel = ds.getValue(PublisherModel.class);
                        publisherModelList.add(publisherModel);
                        listAdapter = new ChatListAdapter(Chat_Main_Screen.this,publisherModelList);
                        ChatRec.setAdapter(listAdapter);
                    }
                }
                else
                {
                    Toast.makeText(Chat_Main_Screen.this,"Something Wrong!...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}