package com.example.jeyabookcentre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPassword extends AppCompatActivity {

    EditText pwd1,pwd2;
    AppCompatButton forgotbtn;
    private FirebaseDatabase ref;
    private FirebaseUser fUser;
    private DatabaseReference databaseReference;
    private String userID;

    String paswrd1,paswrd2;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotPassword.this, Login_Screen.class));
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //firebase init
        ref = FirebaseDatabase.getInstance();
        //getting current user
        //fUser = FirebaseAuth.getInstance().getCurrentUser();
        //databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        //userID = fUser.getUid();

        pwd1 = findViewById(R.id.pwd1);
        pwd2 = findViewById(R.id.pwd2);
        forgotbtn = findViewById(R.id.forgotbtn);

        forgotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkinputs();
            }
        });

    }

    private void checkinputs()
    {
        paswrd1 = pwd1.getText().toString().trim();
        paswrd2 = pwd2.getText().toString().trim();

        if (paswrd1.isEmpty() || paswrd1.length()<8)
        {
            pwd1.setError("Enter a password within 8 characters");
            return;
        }
        else
        {
            pwd1.setError(null);
        }

        if (!pwd1.equals(paswrd2))
        {
            pwd2.setError("Password not matched!");
            return;
        }
        else
        {
            pwd2.setError(null);
        }
        if (paswrd1.isEmpty() || paswrd2.isEmpty())
        {
            Toast.makeText(ForgotPassword.this,"Can't proceed further...",Toast.LENGTH_SHORT).show();

        }
        else
        {
            performPasswdchang();
        }

    }

    private void performPasswdchang()
    {
    }
}