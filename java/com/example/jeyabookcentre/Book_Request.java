package com.example.jeyabookcentre;

import static com.google.firebase.database.core.RepoManager.clear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeyabookcentre.Admin_Screens.AddBook_Screen;
import com.example.jeyabookcentre.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Book_Request extends AppCompatActivity {

    private String reqBname,reqBauthor,reqBprice,userID,CurrentDate,CurrentTime,ReqID,uName,uPhone;

    ImageButton back;
    EditText reqBook,reqAuthor,reqPrice;
    TextView clear;
    AppCompatButton submit;

    private ProgressDialog progressDialog;
    private DatabaseReference ReqDatabaseRef,databaseReference;
    private FirebaseUser fUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        // Getting logged user name to the Book_Request activity
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userdata =snapshot.getValue(Users.class);

                if (userdata !=null) {
                    uName = userdata.getfName() + " " + userdata.getlName();
                    uPhone = userdata.getPhone();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back = findViewById(R.id.reqBackBTN);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reqBook = findViewById(R.id.reqBname);
        reqAuthor = findViewById(R.id.reqAname);
        reqPrice = findViewById(R.id.reqBprice);

        clear = findViewById(R.id.clearTxt);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearFields();
            }
        });

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookReqDataCheck();
            }
        });

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        ReqDatabaseRef = FirebaseDatabase.getInstance().getReference().child("requests");


    }

    private void ClearFields()
    {
        reqBook.setText("");
        reqAuthor.setText("");
        reqPrice.setText("");
    }

    private void bookReqDataCheck()
    {
        reqBname = reqBook.getText().toString().trim();
        reqBauthor = reqAuthor.getText().toString().trim();
        reqBprice = reqPrice.getText().toString().trim();

        //Validated data
        if (reqBname.isEmpty()) {
            Toast.makeText(this, "Please enter book name field", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (reqBauthor.isEmpty()) {
            Toast.makeText(this, "Please enter author name field", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        else
        {
            SubmitRequest();
        }

    }

    private void SubmitRequest()
    {
        //Add data to db
        progressDialog.setMessage("Submitting....");
        progressDialog.show();

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        CurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = currentTime.format(calendar.getTime());

        ReqID = CurrentDate + CurrentTime;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("requestBook", "" + reqBname);
        hashMap.put("author", "" + reqBauthor);
        hashMap.put("suggestprice", "" + reqBprice);
        hashMap.put("requestUser", "" + uName);
        hashMap.put("requestUID", "" + userID);
        hashMap.put("requestUserPhn", "" + uPhone);
        hashMap.put("Progress","InProgress");

        //add to db
        ReqDatabaseRef.child(ReqID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    progressDialog.dismiss();
                    Toast.makeText(Book_Request.this, "Request sent...", Toast.LENGTH_SHORT).show();
                    ClearFields();
                    Intent intent = new Intent(Book_Request.this,Home_Screen.class);
                    startActivity(intent);

                } else {
                    //failed adding to db
                    progressDialog.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(Book_Request.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                    ClearFields();

                }
            }
        });

    }
}