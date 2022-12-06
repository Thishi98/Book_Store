package com.example.jeyabookcentre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Chat_Entrance_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_entrance_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will executed once the timer is over
                Intent i = new Intent(Chat_Entrance_screen.this,Chat_Main_Screen.class);
                startActivity(i);
                finish();
            }
        },2500);

    }
}