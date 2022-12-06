package com.example.jeyabookcentre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeyabookcentre.Adapter.ChatAdapter;
import com.example.jeyabookcentre.Models.MessageModel;
import com.example.jeyabookcentre.Publisher_Screens.Chat_publisher_Screen;
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
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Screen extends AppCompatActivity {

    private FirebaseDatabase database;
    FirebaseAuth firebaseAuth;

    String recieverName,recieverImg,senderUID,recieverUUID;
    //DatabaseReference databaseReferenceSender,databaseReferenceReciever;
    //String senderRoom,recieverRoom;
    ChatAdapter chatAdapter;
    RecyclerView chatRecycler;
    ImageView SendIV;
    EditText messageED;
    CircleImageView profileImage;
    TextView profileName;
    int totalChats = 1;

    public static String sImage;
    public static String rImage;
    String senderRoom,receiverRoom;

    ArrayList<MessageModel> messageArrayList;
    //List<MessageModel> messageModelList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        //Firebase database & firebaseAuth initiating
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //Chat retrieving recyclerview and chat adapter init
        chatRecycler = findViewById(R.id.chatRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);   //recyclerview layout established as linearlayout
        linearLayoutManager.setStackFromEnd(true);    //chat layout order begin from the end
        chatRecycler.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(Chat_Screen.this,messageArrayList);    //adapter setting for the arraylist
        chatRecycler.setAdapter(chatAdapter);     //setting adapter to the recyclerview

        //chat_screen.xml item init
        SendIV = findViewById(R.id.sendMessage);
        messageED = findViewById(R.id.messageEd);
        profileImage = findViewById(R.id.RecieverProf_Img);
        profileName = findViewById(R.id.RecieverName);

        //getting publisher data from chat_main_screen through publisherlistadapter
        recieverImg = getIntent().getStringExtra("pubImg");
        recieverName = getIntent().getStringExtra("pubName");
        recieverUUID = getIntent().getStringExtra("pubID");

        //initializing the arraylist
        messageArrayList = new ArrayList<>();

        //set retrieved data from the chat_main_screen
        Picasso.get().load(recieverImg).into(profileImage);
        profileName.setText("" + recieverName);

        //getting current user uid
        senderUID = firebaseAuth.getUid();

        //creating two rooms in one table for the sender and receiver in firebase
        senderRoom = senderUID+recieverUUID;
        receiverRoom = recieverUUID+senderUID;

        //DatabaseReference pubreference = database.getReference().child("Publishers").child(firebaseAuth.getUid());
        DatabaseReference usreference = database.getReference().child("Users").child(firebaseAuth.getUid());  //getting user info from firebase user table
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");   // getting chatting info from firebase chat table

        //Retrieving firebase chats table data to the recyclerview - sender
        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messageArrayList.add(messageModel);
                    chatAdapter = new ChatAdapter(Chat_Screen.this,messageArrayList);    //adapter setting for the arraylist
                    chatRecycler.setAdapter(chatAdapter);     //setting adapter to the recyclerview
                }
                //Notifies the attached observers that the underlying data has been changed and any View reflecting
                // the data set should refresh itself.
              //chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //to get the reciever and sender dp
        usreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                sImage = String.valueOf(snapshot.child("dp").getValue());
                rImage = recieverImg;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageED.getText().toString();

                if (message.isEmpty())
                {
                    Toast.makeText(Chat_Screen.this,"Empty message...",Toast.LENGTH_SHORT).show();
                    return;
                }

                messageED.setText("");
                Date date = new Date();
                String messageID = UUID.randomUUID().toString();

                MessageModel messageModel = new MessageModel(messageID,senderUID,recieverUUID,message,date.getTime(),totalChats);

                database = FirebaseDatabase.getInstance();
                database.getReference("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference("chats")
                                        .child(receiverRoom)
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