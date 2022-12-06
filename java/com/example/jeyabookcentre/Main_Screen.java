package com.example.jeyabookcentre;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Main_Screen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will executed once the timer is over
                Intent intent = new Intent(Main_Screen.this,Login_Screen.class);
                startActivity(intent);
                finish();
            }
        },2500);

    }

    }