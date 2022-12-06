package com.example.jeyabookcentre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Models.Users;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Profile extends AppCompatActivity {

    private FirebaseUser fUser;
    private DatabaseReference ref;
    private String userID;

    String NameFProfile,NameLProfile,contactProfile,eMailProfile;

    private ImageButton backProfile;
    private CircleImageView ProfileImg;
    private TextInputEditText userFProfile,userLProfile,mobileProfile,mailProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //firebase init
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        backProfile = findViewById(R.id.ProfileBack);

        backProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //init
        ProfileImg = findViewById(R.id.profile);
        userFProfile = findViewById(R.id.TextFName);
        userLProfile = findViewById(R.id.TextLName);
        mobileProfile = findViewById(R.id.TextContact);
        mailProfile = findViewById(R.id.TextEmail);

        //fetching logged user data to the profile view
        ref.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //getting reference from Users model class
                Users userprof = snapshot.getValue(Users.class);

                if (userprof != null){
                    NameFProfile = userprof.getfName();
                    NameLProfile = userprof.getlName();
                    contactProfile = userprof.getPhone();
                    eMailProfile = userprof.getEmail();
                    //imgProfile = userprof.getDp();

                    //set user data to the view
                    userFProfile.setText(NameFProfile);
                    userLProfile.setText(NameLProfile);
                    mobileProfile.setText(contactProfile);
                    mailProfile.setText(eMailProfile);

                    //setup imageview
                    Glide.with(User_Profile.this).load(userprof.getDp()).into(ProfileImg);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Intent intent = new Intent(User_Profile.this, Home_Screen.class);
                startActivity(intent);
                Toast.makeText(User_Profile.this,"Something wrong!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Delete user profile from firebase when user click in delete profile button
    public  void DeleteProfile(View view)
    {
        //ask permission to delete the user profile
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Profile...");
        builder.setMessage("Are you sure to DELETE your profile?");

        //if user confirm to delete the profile, will be deleted
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.child(userID).removeValue();
                Intent intent = new Intent(User_Profile.this, Login_Screen.class);
                startActivity(intent);
                Toast.makeText(User_Profile.this,"Profile Deleted!..",Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();

    }


    //Update when user click in update profile button
    public void updateBtn(View view)
    {
        if (isuserFNameChanged() || isuserLNameChanged() || isuserContactChanged() || isuserEmailChanged()){
            Toast.makeText(User_Profile.this,"User Profile has been Updated!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(User_Profile.this, Login_Screen.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(User_Profile.this,"Same data and cannot be Updated!",Toast.LENGTH_SHORT).show();
        }
    }

    //change the user first name is user update
    private boolean isuserFNameChanged()
    {
        //check the user first name is equal to profile text
        //if it's not same update the firebase database value
        if (!NameFProfile.equals(userFProfile.getText().toString())){

            ref.child(userID).child("fName").setValue(userFProfile.getText().toString());
            return true;

        }
        else {
            //if it's same do not update the firebase database value
            return false;

        }
    }
    //change the user last name is user update
    private boolean isuserLNameChanged()
    {
        //check the user last name is equal to profile text
        //if it's not same update the firebase database value
        if (!NameLProfile.equals(userLProfile.getText().toString())){

            ref.child(userID).child("lName").setValue(userLProfile.getText().toString());
            return true;

        }
        else {
            //if it's same do not update the firebase database value
            return false;

        }
    }
    //change the user contact is user update
    private boolean isuserContactChanged()
    {
        //check the user contact is equal to profile text
        //if it's not same update the firebase database value
        if (!contactProfile.equals(mobileProfile.getText().toString())){

            ref.child(userID).child("phone").setValue(mobileProfile.getText().toString());
            return true;

        }
        else {
            //if it's same do not update the firebase database value
            return false;

        }
    }
    //change the user email is user update
    private boolean isuserEmailChanged()
    {
        //check the user email is equal to profile text
        //if it's not same update the firebase database value
        if (!eMailProfile.equals(mailProfile.getText().toString())){

            ref.child(userID).child("email").setValue(mailProfile.getText().toString());
            return true;

        }
        else {
            //if it's same do not update the firebase database value
            return false;

        }
    }

}