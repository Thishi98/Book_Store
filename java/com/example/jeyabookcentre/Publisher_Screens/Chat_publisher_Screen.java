package com.example.jeyabookcentre.Publisher_Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeyabookcentre.Adapter.ChatAdapter;
import com.example.jeyabookcentre.Models.MessageModel;
import com.example.jeyabookcentre.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_publisher_Screen extends AppCompatActivity {

    String recieversName,recieversImg,recieversUID,sendersUID;
    int totalchats = 1;

    private FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    ChatAdapter chatAdapter;

    RecyclerView chatRecyclerview;
    ImageView SendImvView;
    EditText messagetxtED;
    CircleImageView profileIV;
    TextView profileNametxt;

    String SendersRoom,RecieversRoom;
    public static String sImage;
    public static String rImage;

    ArrayList<MessageModel> messageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_publisher_screen);

        //Firebase database & firebaseAuth initiating
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //Chat retrieving recyclerview and chat adapter init
        chatRecyclerview = findViewById(R.id.chatRecyclerV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);   //recyclerview layout established as linearlayout
        linearLayoutManager.setStackFromEnd(true);    //chat layout order begin from the end
        chatRecyclerview.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(Chat_publisher_Screen.this,messageArrayList);    //adapter setting for the arraylist
        chatRecyclerview.setAdapter(chatAdapter);     //setting adapter to the recyclerview

        //chat_screen.xml item init
        SendImvView = findViewById(R.id.sendMessageIV);
        messagetxtED = findViewById(R.id.messagetxtEd);
        profileIV = findViewById(R.id.RecieverProf_IV);
        profileNametxt = findViewById(R.id.RecieverNametxt);

        //getting user data from publisher_userlist_screen through RecieverListAdapter
        recieversImg = getIntent().getStringExtra("userImg");
        recieversName = getIntent().getStringExtra("userName");
        recieversUID = getIntent().getStringExtra("userID");

        //initializing the arraylist
        messageArrayList = new ArrayList<>();

        //set retrieved data from the chat_main_screen
        Picasso.get().load(recieversImg).into(profileIV);
        profileNametxt.setText("" + recieversName);

        //getting current user uid
        sendersUID = firebaseAuth.getUid();

        SendersRoom = recieversUID+sendersUID;
        RecieversRoom = sendersUID+recieversUID;

        DatabaseReference usersference = database.getReference().child("Users").child(firebaseAuth.getUid());  //getting user info from firebase user table
        DatabaseReference chatsreference = database.getReference().child("chats").child(SendersRoom).child("messages");   // getting chatting info from firebase chat table

        //Retrieving firebase chats table data to the recyclerview - sender
        chatsreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messageArrayList.add(messageModel);
                    chatAdapter = new ChatAdapter(Chat_publisher_Screen.this,messageArrayList);    //adapter setting for the arraylist
                    chatRecyclerview.setAdapter(chatAdapter);     //setting adapter to the recyclerview
                }
                //Notifies the attached observers that the underlying data has been changed and any View reflecting
                // the data set should refresh itself.
                //  chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //to get the reciever and sender dp
        usersference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                sImage = String.valueOf(snapshot.child("dp").getValue());
                rImage = recieversImg;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SendImvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messagetxtED.getText().toString();

                if (message.isEmpty())
                {
                    Toast.makeText(Chat_publisher_Screen.this,"Empty message...",Toast.LENGTH_SHORT).show();
                    return;
                }

                messagetxtED.setText("");
                Date date = new Date();
                String messageID = UUID.randomUUID().toString();

                MessageModel messageModel = new MessageModel(messageID,sendersUID,recieversUID,message,date.getTime(),totalchats);

                database = FirebaseDatabase.getInstance();
                database.getReference("chats")
                        .child(SendersRoom)
                        .child("messages")
                        .push()
                        .setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference("chats")
                                        .child(RecieversRoom)
                                        .child("messages")
                                        .push()
                                        .setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });

            }
        });

    }
}