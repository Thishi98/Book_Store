package com.example.jeyabookcentre.Publisher_Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.jeyabookcentre.R;

public class Chat_Entrance_screen_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_entrance_screen_2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will executed once the timer is over
                Intent i = new Intent(Chat_Entrance_screen_2.this, Publisher_UserList_Screen.class);
                startActivity(i);
                finish();
            }
        },2500);

    }
}