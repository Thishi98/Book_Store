package com.example.jeyabookcentre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeyabookcentre.Admin_Screens.AdminHome_Screen;
import com.example.jeyabookcentre.Models.Users;
import com.example.jeyabookcentre.Publisher_Screens.PublisherHome_Screen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.List;

public class Login_Screen extends AppCompatActivity {

    private TextView createnewAcc,forgotPass;
    private EditText inputEmail, inputPasswrd;
    private AppCompatButton loginBTN;

    public static String PREFS_NAME="MyPrefsFile";

    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference firebaseDatabase;

    List<Users> usersList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //initializing variables
        createnewAcc = findViewById(R.id.createNewAcc);

        //Switching login screen to register screen
        createnewAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Screen.this, Register_Screen.class));

            }
        });

        forgotPass = findViewById(R.id.forgotPswrd);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Login_Screen.this, ForgotPassword.class));

            }
        });

        inputEmail = findViewById(R.id.edEmail);
        inputPasswrd = findViewById(R.id.edPass);
        loginBTN = findViewById(R.id.btnLogin);
        progressDialog = new ProgressDialog(this);

        //Firebase auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Initializing the Login button
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saving logged user data to show in other pages to display logged user data
                SharedPreferences sharedPreferences = getSharedPreferences(Login_Screen.PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("hasLoggedIn",true);
                editor.commit();

                PerformLogin();
            }
        });
    }

    //Check and performing login
    private void PerformLogin() {
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

        if(email.equals("publisher@gmail.com") && paswrd.equals("publisher123")){
            startActivity(new Intent(Login_Screen.this, PublisherHome_Screen.class));
            Toast.makeText(Login_Screen.this,"Login Successfully!",Toast.LENGTH_SHORT).show();
        }
        else {

            mAuth.signInWithEmailAndPassword(email, paswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        //redirect to user profile
                        if(email.equals("admin@gmail.com") && paswrd.equals("admin123")){
                            startActivity(new Intent(Login_Screen.this, AdminHome_Screen.class));
                            Toast.makeText(Login_Screen.this,"Admin Logged in Successfully!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            startActivity(new Intent(Login_Screen.this, Home_Screen.class));
                            Toast.makeText(Login_Screen.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(Login_Screen.this,"Login Failed! Please check your Credentials",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        }

}