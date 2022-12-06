package com.example.jeyabookcentre.Admin_Screens;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.jeyabookcentre.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddPromotion_Screen extends AppCompatActivity {

    private String promNote,CurrentTime, CurrentDate;

    private ImageButton PromBackBTN;
    private ImageView PromIMG;
    private AppCompatButton PromBTN,SeeAllPromBTN;
    private EditText AdvertiseNote;

    //permission for constants
    //private static final int CAMERA_REQUEST_CODE = 200;
    //private static final int STORAGE_REQUEST_CODE = 300;

    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 1;
    private static final int IMAGE_PICK_CAMERA_CODE = 2;

    //permission arrays
    private String[] CameraPermission;
    private String[] StoragePermission;
    //Image pick uri
    private Uri image_uri;

    private String advertiseRandomKey, downloadImageUrl;

    private StorageReference AdvertiseImageRef;
    private DatabaseReference PromDatabaseRef;

    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promotion_screen);

        PromBackBTN = findViewById(R.id.AddpromBackBTN);
        PromIMG = findViewById(R.id.addpromotion);
        PromBTN = findViewById(R.id.addpromBTN);
        SeeAllPromBTN = findViewById(R.id.SeeAllProm);
        AdvertiseNote = findViewById(R.id.advertiseNote);

        //Database data storing path
        AdvertiseImageRef = FirebaseStorage.getInstance().getReference().child("prom_imgs");
        PromDatabaseRef = FirebaseDatabase.getInstance().getReference().child("promotions");

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        //Init permission arrays
        CameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        StoragePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        PromBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        PromIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        PromBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1) Input data
                //2) Validate data
                //3) Add data to db
                InputPromotion();
            }
        });

        SeeAllPromBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPromotion_Screen.this, SeeAllPromotions_Screen.class);
                startActivity(intent);
            }
        });

    }

    private void showImagePickDialog()
    {
        //option to display in dialog
        String[] options = {"Camera","Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Handle item clicks

                        if (which==0)  //if select 1st option
                        {
                            //camera clicked
                            if (checkCameraPermission())
                            {
                                //permission granted
                                pickFromCamera();
                            }
                            else
                            {
                                //permission not granted : request
                                requestCameraPermission();
                            }
                        }
                        else
                        {
                            //gallery clicked
                            if (checkStoragePermission())
                            {
                                //permission granted
                                pickFromGallery();
                            }
                            else
                            {
                                //permission not granted : request
                                requestStoragePermission();
                            }
                        }

                    }
                })
                .show();
    }

    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(this, StoragePermission, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromGallery()
    {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission()
    {
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
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
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    //handle image pick results


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_GALLERY_CODE && resultCode == RESULT_OK && data!=null)
        {
            //image picked from gallery
            //save picked image uri
            image_uri = data.getData();
            //set image
            PromIMG.setImageURI(image_uri);
        }
        else if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == RESULT_OK && data!=null)
        {
            //image pick from camera
            //save picked image uri
            image_uri = data.getData();
            //set image
            PromIMG.setImageURI(image_uri);
        }

    }

    private void InputPromotion() {

      promNote = AdvertiseNote.getText().toString().trim();

      if (TextUtils.isEmpty(promNote)){
          Toast.makeText(this,"Promotion Note required!...",Toast.LENGTH_SHORT).show();
      }
      else {
          storePromImgInfo();
      }

    }

    private void storePromImgInfo()
    {
        //3) Add data to db
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Make sure to connect to the Internet");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        CurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = currentTime.format(calendar.getTime());

        advertiseRandomKey = CurrentDate + CurrentTime;

        //Promotion image link
        StorageReference filepath = AdvertiseImageRef.child(image_uri.getLastPathSegment() + advertiseRandomKey + ".jpg");

        final UploadTask uploadTask = filepath.putFile(image_uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddPromotion_Screen.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddPromotion_Screen.this, "Advertisement Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                                if (!task.isSuccessful()){
                                    throw task.getException();

                                }
                                downloadImageUrl = filepath.getDownloadUrl().toString();
                                return filepath.getDownloadUrl();
                            }
                        })
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()){

                                            downloadImageUrl = task.getResult().toString();

                                            Toast.makeText(AddPromotion_Screen.this, "Advertise Image Url got Successfully!", Toast.LENGTH_SHORT).show();

                                            addAdvertisement();

                                        }
                                    }
                                });
                    }
                });

    }

    private void ClearData()
    {
        //clear data after uploading a promotion
        AdvertiseNote.setText("");
        PromIMG.setImageResource(R.drawable.ic_baseline_plus_mark);
        image_uri = null;

    }

    private void addAdvertisement()
    {
        //3) Add data to db
        progressDialog.setMessage("Posting....");
        progressDialog.show();

        //upload with an image

        //upload image to storage

        if (image_uri != null) {

            //setup data to upload
            HashMap<String, Object> promhashMap = new HashMap<>();
            promhashMap.put("promotion_id", "" + advertiseRandomKey);
            promhashMap.put("promotion_note", "" + promNote);
            promhashMap.put("promotion_img", "" + downloadImageUrl);

            //add to db
            PromDatabaseRef.child(advertiseRandomKey).updateChildren(promhashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        Toast.makeText(AddPromotion_Screen.this, "Advertisement added Successfully...", Toast.LENGTH_SHORT).show();
                        ClearData();
                    } else {
                        //failed adding to db
                        progressDialog.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(AddPromotion_Screen.this, "Error : " + message, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else
        {
            Toast.makeText(AddPromotion_Screen.this, "Please select Promotion image to proceed...", Toast.LENGTH_SHORT).show();
        }
    }


}

