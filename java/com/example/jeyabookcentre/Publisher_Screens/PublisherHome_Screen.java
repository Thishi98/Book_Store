package com.example.jeyabookcentre.Publisher_Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jeyabookcentre.Adapter.PublisherListAdapter;
import com.example.jeyabookcentre.Admin_Screens.AdminHome_Screen;
import com.example.jeyabookcentre.Login_Screen;
import com.example.jeyabookcentre.Models.PublisherModel;
import com.example.jeyabookcentre.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PublisherHome_Screen extends AppCompatActivity {

    RecyclerView publisherRec;
    //ImageView logOut;
    PublisherListAdapter publisherListAdapter;
    List<PublisherModel> publisherList;

    private DatabaseReference databaseReference;
    private FirebaseDatabase ref;
    private FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        ViewAlertDialog();
    }

    private void ViewAlertDialog()
    {
        AlertDialog dialog = new AlertDialog.Builder(PublisherHome_Screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
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
        setContentView(R.layout.activity_publisher_home_screen);

        publisherRec = findViewById(R.id.pubList_Recycler);
        publisherRec.setLayoutManager(new LinearLayoutManager(this));

        //signout from publisher main screen
        mAuth = FirebaseAuth.getInstance();
       // logOut = findViewById(R.id.LogoutPublisher);
        /*logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(PublisherHome_Screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
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
        });*/

        ref = FirebaseDatabase.getInstance();

        publisherList = new ArrayList<>();

        loadPublisherList();


    }

    private void SignOut()
    {
        Intent signoutIntent = new Intent(PublisherHome_Screen.this, Login_Screen.class);
        signoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signoutIntent);
        finish();
    }

    private void loadPublisherList()
    {
        ref.getReference("Publishers").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    for (DataSnapshot ds: task.getResult().getChildren())
                    {
                        PublisherModel publisherModel = ds.getValue(PublisherModel.class);
                        publisherList.add(publisherModel);
                        publisherListAdapter = new PublisherListAdapter(PublisherHome_Screen.this,publisherList);
                        publisherRec.setAdapter(publisherListAdapter);
                    }
                }
                else
                {
                    Toast.makeText(PublisherHome_Screen.this,"Something Wrong!...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}