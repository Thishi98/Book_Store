package com.example.jeyabookcentre.Publisher_Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jeyabookcentre.Eventbus.MyUpdateCartEvent;
import com.example.jeyabookcentre.Eventbus.MyUpdateChatEvent;
import com.example.jeyabookcentre.Listners.IChatLoadListner;
import com.example.jeyabookcentre.Login_Screen;
import com.example.jeyabookcentre.Models.CartModel;
import com.example.jeyabookcentre.Models.MessageModel;
import com.example.jeyabookcentre.Models.PublisherModel;
import com.example.jeyabookcentre.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Publisher_Items_Screen extends AppCompatActivity implements IChatLoadListner {

    private DatabaseReference reference;
    private FirebaseUser fUser;
    private FirebaseAuth mAuth;

    private TextView publisherName;
    private CardView chatCard,requestCard;
    private ImageView LogOut;
    private NotificationBadge badgeChat;
    private LinearLayout PubItemLayout;

    private String PID,PUBNAME,sendersUID,recieversUID;
    String senderRoom;

    //Chat item load listner
    private IChatLoadListner iChatLoadListner;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(MyUpdateChatEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateChatEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // countChatItems();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event)
    {
        //countChatItems();
    }

    //Chat item counting
  /*  private void countChatItems()
    {
        List<MessageModel> messageModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("chats")
                .child(senderRoom).child("messages")           //Unable to start activity ComponentInfo{com.example.jeyabookcentre/com.example.jeyabookcentre.Publisher_Screens.Publisher_Items_Screen}: java.lang.NullPointerException: Can't pass null for argument 'pathString' in child()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot chatsnapshop : snapshot.getChildren())
                        {MessageModel messageModel = chatsnapshop.getValue(MessageModel.class);
                            messageModel.setMsgID(chatsnapshop.getKey());
                            messageModels.add(messageModel);
                        }
                        iChatLoadListner.onItemChatLoadSuccess(messageModels);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iChatLoadListner.onItemChatLoadFailed(error.getMessage());
                    }
                });
    }*/


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        ViewAlertDialog();
    }

    private void ViewAlertDialog()
    {
        AlertDialog dialog = new AlertDialog.Builder(Publisher_Items_Screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
                .setTitle("LogOut")
                .setMessage("Are you sure to Logout?")
                .setNegativeButton("No", (dialog1, which) -> dialog1.dismiss())
                .setPositiveButton("Yes", (dialog2, which) -> {

                    mAuth.signOut();
                    SignOut();

                    dialog2.dismiss();
                }).create();
        dialog.show();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_items_screen);

        publisherName = findViewById(R.id.pubName);
        chatCard = findViewById(R.id.chatCard);
        requestCard = findViewById(R.id.requestCard);
        PubItemLayout = findViewById(R.id.mainPubLayout);

        requestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Publisher_Items_Screen.this, Requests_Screen.class);
                startActivity(intent);

            }
        });

        //getting current user uid
        //sendersUID = FirebaseAuth.getInstance().getUid();
        recieversUID = getIntent().getStringExtra("userID");   //senderid

        //countChatItems();
        ini();

        senderRoom = PID+recieversUID;

        //notification badge
        badgeChat = findViewById(R.id.Badgechats);

        //signout and clean the task
        mAuth = FirebaseAuth.getInstance();
        LogOut = findViewById(R.id.LogoutPublisher);
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(Publisher_Items_Screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
                        .setTitle("LogOut")
                        .setMessage("Are you sure to Logout?")
                        .setNegativeButton("No", (dialog1, which) -> dialog1.dismiss())
                        .setPositiveButton("Yes", (dialog2, which) -> {

                            mAuth.signOut();
                            SignOut();

                            dialog2.dismiss();
                        }).create();
                dialog.show();
            }
        });

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Publishers");
        PID = fUser.getUid();

        // Getting logged user name to the publisher_item_screen activity
        reference.child(PID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PublisherModel publishers = snapshot.getValue(PublisherModel.class);

                if (publishers != null)
                {
                    PUBNAME = publishers.getPubName();

                    publisherName.setText(PUBNAME);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Publisher_Items_Screen.this, Chat_Entrance_screen_2.class);
                startActivity(intent);
            }
        });

    }

    private void ini()
    {
        ButterKnife.bind(this);

        iChatLoadListner = this;
    }

    private void SignOut()
    {
        Intent signoutIntent = new Intent(Publisher_Items_Screen.this, Login_Screen.class);
        signoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signoutIntent);
        finish();
    }

    @Override
    public void onItemChatLoadSuccess(List<MessageModel> messageModelList) {
        int chatSum = 0;
        for (MessageModel messageModel: messageModelList)
            chatSum += messageModel.getTotalChats();
        badgeChat.setNumber(chatSum);
    }

    @Override
    public void onItemChatLoadFailed(String message) {
        Snackbar.make(PubItemLayout,message,Snackbar.LENGTH_LONG).show();
    }
}