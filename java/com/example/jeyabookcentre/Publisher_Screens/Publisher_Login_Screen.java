package com.example.jeyabookcentre.Publisher_Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeyabookcentre.Login_Screen;
import com.example.jeyabookcentre.Models.PublisherModel;
import com.example.jeyabookcentre.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Publisher_Login_Screen extends AppCompatActivity {

    private EditText inputEmail, inputPasswrd;
    private AppCompatButton loginBTN;

    public static String PREFS_NAME="MyPrefsFile";

    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference firebaseDatabase;

    List<PublisherModel> publisherModelList;
    
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_login_screen);
        
        //initializing activity_publisher_login_screen.xml 
        inputEmail = findViewById(R.id.pubEmail);
        inputPasswrd = findViewById(R.id.pubPass);
        loginBTN = findViewById(R.id.pubbtnLogin);
        progressDialog = new ProgressDialog(this);

        //Firebase auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Initializing the Login button
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(Publisher_Login_Screen.PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("hasLoggedIn",true);
                editor.commit();

                PerformLogin();
            }
        });
    }

    private void PerformLogin()
    {
        String email = inputEmail.getText().toString();
        String paswrd = inputPasswrd.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Please enter a valid email!");
            inputEmail.requestFocus();
            return;
        }

        if (paswrd.isEmpty() || paswrd.length() < 8) {
            inputPasswrd.setError("Enter password within 8 alphanumeric characters");
            inputPasswrd.requestFocus();
            return;
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email,paswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        //redirect to user profile
                        startActivity(new Intent(Publisher_Login_Screen.this, Publisher_Items_Screen.class));
                        Toast.makeText(Publisher_Login_Screen.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Toast.makeText(Publisher_Login_Screen.this,"Login Failed! Please check your Credentials",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}