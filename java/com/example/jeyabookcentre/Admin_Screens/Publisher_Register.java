package com.example.jeyabookcentre.Admin_Screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeyabookcentre.Models.PublisherModel;
import com.example.jeyabookcentre.Publisher_Screens.PublisherHome_Screen;
import com.example.jeyabookcentre.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Publisher_Register extends AppCompatActivity {

    private String id,pubName,pubStatus,email,Phone,paswrd,confpaswrd;

    private TextView HaveAcc,clearall;
    private EditText inputpubName,inputStatus,inputEmail,inputPhone,inputPasswrd,inputConfPasswrd;
    private ImageView pubDP;
    private AppCompatButton register;

    String CurrentDate,CurrentTime,publisherRandKey,downloadImgURL,publisherID;

    private ProgressDialog progressDialog;

    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 1;
    private static final int IMAGE_PICK_CAMERA_CODE = 2;

    //permission arrays
    private String[] CameraPermission;
    private String[] StoragePermission;
    //Image pick uri
    private Uri image_uri;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private StorageReference UserImageRef;
    private DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_register);

        //Firebase auth & database initializing
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //Initializing variables
        HaveAcc=findViewById(R.id.pub_haveAcc);
        clearall = findViewById(R.id.pub_clearall);

        inputpubName = findViewById(R.id.pub_edName);
        inputStatus = findViewById(R.id.pub_edStatus);
        inputEmail = findViewById(R.id.pub_edemail);
        inputPhone = findViewById(R.id.pub_edcontact);
        inputPasswrd = findViewById(R.id.pub_edPswrd);
        inputConfPasswrd = findViewById(R.id.pub_edConfPswrd);
        pubDP = findViewById(R.id.pub_img);
        register = findViewById(R.id.pub_btnRegister);
        progressDialog = new ProgressDialog(this);

        //Firebase auth & database initializing
        firebaseDatabase = FirebaseDatabase.getInstance();
        UserImageRef = FirebaseStorage.getInstance().getReference().child("publisher_dp");
        reference = FirebaseDatabase.getInstance().getReference().child("Publishers");

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        //initialize array permission
        CameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        StoragePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pubDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowimagePickDialog();
            }
        });

        clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearAllTxt();
            }
        });

        //Already logged in user
        HaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Publisher_Register.this, PublisherHome_Screen.class));
            }
        });

        //Performing customer registration
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuthforPub();
            }
        });

    }

    private void PerformAuthforPub()
    {
        pubName = inputpubName.getText().toString().trim();
        pubStatus = inputStatus.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        Phone = inputPhone.getText().toString().trim();
        paswrd = inputPasswrd.getText().toString().trim();
        confpaswrd = inputConfPasswrd.getText().toString().trim();

        if (pubName.isEmpty())
        {
            inputpubName.setError("Enter your Name");
            return;
        }
        else
        {
            inputpubName.setError(null);
        }

        if (pubStatus.isEmpty())
        {
            inputStatus.setError("Enter the company moto here");
            return;
        }
        else
        {
            inputStatus.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            inputEmail.setError("Enter valid email");
            inputEmail.requestFocus();
            return;
        }
        else
        {
            inputEmail.setError(null);
        }

        if (Phone.isEmpty() || Phone.length()<10)
        {
            inputPhone.setError("Enter your Phone number");
            inputPhone.requestFocus();
            return;
        }
        else
        {
            inputPhone.setError(null);
        }

        if (paswrd.isEmpty() || paswrd.length()<8)
        {
            inputPasswrd.setError("Enter a password within 8 characters");
            return;
        }
        else
        {
            inputPasswrd.setError(null);
        }

        if (!paswrd.equals(confpaswrd))
        {
            inputConfPasswrd.setError("Password not matched!");
            return;
        }
        else
        {
            inputConfPasswrd.setError(null);
        }

        if (pubName.isEmpty() || email.isEmpty() || paswrd.isEmpty() || confpaswrd.isEmpty())
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Something Wrong!");
            progressDialog.setMessage("Fill all fields before proceed....");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
        }
        else
        {

                storeImageInfo();


        }

    }

    private void storeImageInfo()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Make sure to connect to the Internet");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Product image link
        StorageReference filePath = UserImageRef.child(image_uri.getLastPathSegment() + publisherRandKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(image_uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(Publisher_Register.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Publisher_Register.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask =uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                downloadImgURL = filePath.getDownloadUrl().toString();
                                return filePath.getDownloadUrl();
                            }
                        })
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {

                                            downloadImgURL = task.getResult().toString();

                                            Toast.makeText(Publisher_Register.this, "Successfully added your DP!", Toast.LENGTH_SHORT).show();

                                            registerPublisher();
                                        }
                                    }
                                });
                    }
                });

    }

    private void registerPublisher()
    {
        progressDialog.setMessage("Registration....");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

       // id = publisherID;

            mAuth.createUserWithEmailAndPassword(email,paswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    id = FirebaseAuth.getInstance().getUid();

                    PublisherModel publisherModel = new PublisherModel(id,email,paswrd,confpaswrd,Phone,pubName,pubStatus,downloadImgURL);

                    reference.child(FirebaseAuth.getInstance().getUid()).setValue(publisherModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                progressDialog.dismiss();
                                Toast.makeText(Publisher_Register.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                                ClearAllTxt();
                                sendUserToNextScreen();

                            } else {
                                //failed adding to db
                                progressDialog.dismiss();
                                String message = task.getException().toString();
                                Toast.makeText(Publisher_Register.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                                ClearAllTxt();
                                sendUserToNextScreen();

                            }
                        }
                    });
                }
            });


    }

    private void sendUserToNextScreen()
    {
        Intent intent= new Intent(Publisher_Register.this, AdminHome_Screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void ClearAllTxt()
    {
        inputpubName.setText("");
        inputStatus.setText("");
        inputEmail.setText("");
        inputPhone.setText("");
        inputPasswrd.setText("");
        inputConfPasswrd.setText("");
        pubDP.setImageResource(R.drawable.profile);
        image_uri = null;
    }

    private void ShowimagePickDialog()
    {
        //option to display in dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Handle item clicks

                        if (which == 0) {
                            //camera clicked
                            if (checkCameraPermission()) {
                                //permission granted
                                pickFromCamera();
                            } else {
                                //permission not granted : request
                                requestCameraPermission();
                            }
                        } else {
                            //gallery clicked
                            if (checkStoragePermission()) {
                                //permission granted
                                pickFromGallery();
                            } else {
                                //permission not granted : request
                                requestStoragePermission();
                            }
                        }

                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_GALLERY_CODE && resultCode == RESULT_OK && data!=null){

            image_uri = data.getData();
            pubDP.setImageURI(image_uri);
        }

        else if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == RESULT_OK && data !=null){

            image_uri = data.getData();
            pubDP.setImageURI(image_uri);
        }

    }

    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(this, StoragePermission, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromGallery()
    {
        //intent to pick image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission()
    {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result; //return true or false
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(this, CameraPermission, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromCamera()
    {
        //intent to pick image from camera

        //using media store to pick high quality images
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkCameraPermission()
    {
        boolean result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }
}