package com.example.jeyabookcentre.Publisher_Screens;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Adapter.RecieverListAdapter;
import com.example.jeyabookcentre.Models.MessageModel;
import com.example.jeyabookcentre.Models.Users;
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

public class Publisher_UserList_Screen extends AppCompatActivity {

    private String puid,userid;
    String sendersroom;

    private RecyclerView ChatPubRec;
    RecieverListAdapter listAdapter;
    private List<Users> usersList;
   // private List<Users> mUser;

    private DatabaseReference databaseReference;
    private FirebaseDatabase ref;
    private FirebaseUser fUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_userlist_screen);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        //databaseReference = FirebaseDatabase.getInstance().getReference("Publishers");
        //puid = fUser.getUid();

        ChatPubRec = findViewById(R.id.chtPublisherRecycler);
        ChatPubRec.setHasFixedSize(true);
        ChatPubRec.setLayoutManager(new LinearLayoutManager(this));

        usersList = new ArrayList<>();

        //getting user data from RecieverListAdapter
        userid = getIntent().getStringExtra("usersID");
        sendersroom = userid+puid;

        ref = FirebaseDatabase.getInstance();

        userReqChats();


    }


   private void userReqChats()
    {
        ref.getReference("Users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {

                            if (task.isSuccessful())
                            {
                                for (DataSnapshot ds : task.getResult().getChildren()) {
                                    Users users = ds.getValue(Users.class);
                                    usersList.add(users);
                                    listAdapter = new RecieverListAdapter(Publisher_UserList_Screen.this, usersList);
                                    ChatPubRec.setAdapter(listAdapter);
                                }
                            }
                            else
                            {
                                Toast.makeText(Publisher_UserList_Screen.this,"Something Wrong!...",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


            }
}