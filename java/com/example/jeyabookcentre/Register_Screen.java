package com.example.jeyabookcentre;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeyabookcentre.Admin_Screens.AddBook_Screen;
import com.example.jeyabookcentre.Models.Users;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Register_Screen extends AppCompatActivity {

    private String id,Fname,Lname,email,Phone,paswrd,confpaswrd;

    private TextView alreadyHaveAcc,clearTxt;
    private EditText inputFName,inputLName,inputEmail,inputphone,inputPasswrd,inputConfPasswrd;
    private ImageView img;
    private AppCompatButton registerBTN;

    String CurrentDate,CurrentTime,userRandKey,downloadImgURL;

    private ProgressDialog progressDialog;

    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 1;
    private static final int IMAGE_PICK_CAMERA_CODE = 2;

    //permission arrays
    private String[] CameraPermission;
    private String[] StoragePermission;
    //Image pick uri
    private Uri image_uri;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase firebaseDatabase;
    private StorageReference UserImageRef;
    private DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        //Initializing variables
        alreadyHaveAcc=findViewById(R.id.haveAcc);
        clearTxt = findViewById(R.id.clearall);

        inputFName = findViewById(R.id.edFName);
        inputLName = findViewById(R.id.edLName);
        inputEmail = findViewById(R.id.edUser);
        inputphone = findViewById(R.id.edcontact);
        inputPasswrd = findViewById(R.id.edPswrd);
        inputConfPasswrd = findViewById(R.id.edConfPswrd);
        img = findViewById(R.id.user_img);
        registerBTN = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);

        //Firebase auth & database initializing
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        UserImageRef = FirebaseStorage.getInstance().getReference().child("user_dp");
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        //initialize array permission
        CameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        StoragePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //show image pick dialog
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowimagePickDialog();
            }
        });

        clearTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearAll();
            }
        });

        //Already logged in user
        alreadyHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_Screen.this,Login_Screen.class));

            }
        });

        //Performing customer registration
        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();

               /* Intent intent = new Intent(Register_Screen.this,Home_Screen.class);
                intent.putExtra("userName", String.valueOf(inputFName));
                intent.putExtra("userPhone", String.valueOf(inputphone));
                startActivity(intent);*/

            }
        });
    }

    private void ClearAll()
    {
        inputFName.setText("");
        inputLName.setText("");
        inputEmail.setText("");
        inputphone.setText("");
        inputPasswrd.setText("");
        inputConfPasswrd.setText("");
        img.setImageResource(R.drawable.profile);
        image_uri = null;
    }

    private void ShowimagePickDialog()
    {//option to display in dialog
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
            img.setImageURI(image_uri);
        }

        else if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == RESULT_OK && data !=null){

            image_uri = data.getData();
            img.setImageURI(image_uri);
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

    //Inout customer details to the User db
    private void PerformAuth()
    {
        Fname = inputFName.getText().toString().trim();
        Lname = inputLName.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        Phone = inputphone.getText().toString().trim();
        paswrd = inputPasswrd.getText().toString().trim();
        confpaswrd = inputConfPasswrd.getText().toString().trim();

        if (Fname.isEmpty() || Fname.length()<4)
        {
            inputFName.setError("Enter your Name withing 4 characters");
            return;
        }
        else
        {
            inputFName.setError(null);
        }

        if (Lname.isEmpty() || Lname.length()<4)
        {
            inputLName.setError("Enter your Name withing 4 characters");
            return;
        }
        else
        {
            inputLName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            inputEmail.setError("Enter your email");
            inputEmail.requestFocus();
            return;
        }
        else
        {
            inputEmail.setError(null);
        }

        if (Phone.isEmpty() || Phone.length()<10)
        {
            inputphone.setError("Enter valid Phone number");
            inputphone.requestFocus();
            return;
        }
        else
        {
            inputphone.setError(null);
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

        if (Fname.isEmpty() || Lname.isEmpty() || email.isEmpty() || paswrd.isEmpty() || confpaswrd.isEmpty())
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

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        CurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = currentTime.format(calendar.getTime());

        userRandKey = CurrentDate+CurrentTime;

        //Product image link
        StorageReference filePath = UserImageRef.child(image_uri.getLastPathSegment() + userRandKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(image_uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(Register_Screen.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Register_Screen.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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

                                            Toast.makeText(Register_Screen.this, "Successfully added your DP!", Toast.LENGTH_SHORT).show();

                                            registerusers();
                                        }
                                    }
                                });

                    }
                });

    }



    private void registerusers()
    {
        progressDialog.setMessage("Registration....");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,paswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        id = FirebaseAuth.getInstance().getUid();
                        Users users = new Users(id,downloadImgURL,Fname,Lname,email,Phone,paswrd,confpaswrd);

                        reference.child(FirebaseAuth.getInstance().getUid()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    progressDialog.dismiss();
                                    Toast.makeText(Register_Screen.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                                    ClearAll();
                                    sendUserToNextScreen();

                                } else {
                                    //failed adding to db
                                    progressDialog.dismiss();
                                    Toast.makeText(Register_Screen.this, "Can't proceed without a dp ", Toast.LENGTH_SHORT).show();
                                    ClearAll();
                                    sendUserToNextScreen();

                                }
                            }
                        });
                    }
                }
            });


    }

    //This will send newly registered customers to the login screen
    private void sendUserToNextScreen()
    {
        Intent intent= new Intent(Register_Screen.this,Login_Screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}