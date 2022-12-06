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
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeyabookcentre.Constants.Constants;
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

public class AddStationery_Screen extends AppCompatActivity {

    private String Code, itemName, itemAuthor, itemPublish, OrigPrice, itemDescript, iQuantity, itemCategory,
            itemDiscPrice, itemDiscNote, CurrentTime, CurrentDate;
    private boolean iDiscAvailable = false;

    private ImageButton ItemBackBTN;
    private ImageView ItemIMG;
    private EditText codeED, itemNameED, itemAuthorED, itemPublishED, itemdescripED, itemquantityED, itempriceED
            , discPriceItem, disctNoteItem;
    private TextView Categoryitem, Clearitems;
    private AppCompatButton AddItemBTN;

    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 1;
    private static final int IMAGE_PICK_CAMERA_CODE = 2;

    //permission arrays
    private String[] CameraPermission;
    private String[] StoragePermission;
    //Image pick uri
    private Uri image_uri;

    private String StationeryRandomKey, downloadImageUrl;

    private StorageReference StationeryImageRef;
    private DatabaseReference StationeryDatabaseRef;

    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stationery_screen);

        ItemBackBTN = findViewById(R.id.AddStatBackBTN);

        ItemBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ItemIMG = findViewById(R.id.StationeryImg);

        ItemIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog to pick an image
                showImagePickDialog();
            }
        });

        codeED = findViewById(R.id.edItemID);
        itemNameED = findViewById(R.id.edItemname);
        itemAuthorED = findViewById(R.id.edItemauthor);
        itemPublishED = findViewById(R.id.edItempublisher);
        itemdescripED = findViewById(R.id.edItemdescription);
        itemquantityED = findViewById(R.id.edItemQuantity);
        Categoryitem = findViewById(R.id.TVItemCategory);

        Categoryitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick a category
                CategoryDialog();
            }
        });

        itempriceED = findViewById(R.id.edItemprice);
        discPriceItem = findViewById(R.id.ItemdiscPrice);
        disctNoteItem = findViewById(R.id.ItemdiscNote);
        Clearitems = findViewById(R.id.clearallTXT);

        Clearitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearData();
            }
        });

        AddItemBTN = findViewById(R.id.stationeryAddBTN);

        AddItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1) Input data
                //2) Validate data
                //3) Add data to db
                InputData();
            }
        });

        //Database data storing path
        StationeryImageRef = FirebaseStorage.getInstance().getReference().child("Stationery_img");
        StationeryDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Stationery");

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        //initialize array permission
        CameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        StoragePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


    }

    private void InputData()
    {
        Code = codeED.getText().toString().trim();
        itemName = itemNameED.getText().toString().trim();
        itemAuthor = itemAuthorED.getText().toString().trim();
        itemPublish = itemPublishED.getText().toString().trim();
        OrigPrice = itempriceED.getText().toString().trim();
        itemDescript = itemdescripED.getText().toString().trim();
        iQuantity = itemquantityED.getText().toString().trim();
        itemCategory = Categoryitem.getText().toString().trim();
        itemDiscPrice = discPriceItem.getText().toString().trim();
        itemDiscNote = disctNoteItem.getText().toString().trim();

        if (TextUtils.isEmpty(Code)) {
            Toast.makeText(this, "Item Code is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (TextUtils.isEmpty(itemName)) {
            Toast.makeText(this, "Item Name is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (TextUtils.isEmpty(itemPublish)) {
            Toast.makeText(this, "Item Publish is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (TextUtils.isEmpty(OrigPrice)) {
            Toast.makeText(this, "Price is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (TextUtils.isEmpty(iQuantity)) {
            Toast.makeText(this, "Quantity is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (TextUtils.isEmpty(itemCategory)) {
            Toast.makeText(this, "Item Category is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        else {

            StoreImginfo();
        }

    }

    private void StoreImginfo()
    {
        //Setup progress dialog
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

        StationeryRandomKey = CurrentDate + CurrentTime;

        StorageReference filePath = StationeryImageRef.child(image_uri.getLastPathSegment() + StationeryRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(image_uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddStationery_Screen.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(AddStationery_Screen.this, "Book Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                downloadImageUrl = filePath.getDownloadUrl().toString();
                                return filePath.getDownloadUrl();
                            }
                        })
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {

                                        if (task.isSuccessful()) {

                                            downloadImageUrl = task.getResult().toString();

                                            Toast.makeText(AddStationery_Screen.this, "Stationery Image Url got Successfully!", Toast.LENGTH_SHORT).show();

                                            addStationery();
                                        }
                                    }
                                });

                    }
                });

    }

    private void addStationery()
    {
        //3) Add data to db
        progressDialog.setMessage("Adding Book Details...");
        progressDialog.show();

        if (image_uri == null) {
            //Upload without image

            //setup data to upload
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("item_code", "" + Code);
            hashMap.put("itemname", "" + itemName);
            hashMap.put("itemauthor", "" + itemAuthor);
            hashMap.put("itempublisher", "" + itemPublish);
            hashMap.put("itemprice", "" + OrigPrice);
            hashMap.put("itemdesctript", "" + itemDescript);
            hashMap.put("quantity", "" + iQuantity);
            hashMap.put("itemStatus", "In-Stock");
            hashMap.put("itemcategory", "" + itemCategory);
            hashMap.put("commoncategory", "Stationery");
            hashMap.put("Itemimage", "");    //no image : set empty
            hashMap.put("discprice", "" + itemDiscPrice);
            hashMap.put("discnote", "" + itemDiscNote);
            hashMap.put("proid", StationeryRandomKey);
            hashMap.put("date", CurrentDate);
            hashMap.put("time", CurrentTime);


            StationeryDatabaseRef.child(StationeryRandomKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        Toast.makeText(AddStationery_Screen.this, "Stationery added Successfully...", Toast.LENGTH_SHORT).show();
                        ClearData();

                    } else {
                        //failed adding to db
                        progressDialog.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(AddStationery_Screen.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        ClearData();

                    }
                }
            });
        }
        else {
            //upload with an image

            //upload image to storage

            //setup data to upload
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("item_code", "" + Code);
            hashMap.put("itemname", "" + itemName);
            hashMap.put("itemauthor", "" + itemAuthor);
            hashMap.put("itempublisher", "" + itemPublish);
            hashMap.put("itemprice", "" + OrigPrice);
            hashMap.put("itemdesctript", "" + itemDescript);
            hashMap.put("quantity", "" + iQuantity);
            hashMap.put("itemcategory", "" + itemCategory);
            hashMap.put("commoncategory", "Stationery");
            hashMap.put("itemStatus", "In-Stock");
            hashMap.put("Itemimage", "" + downloadImageUrl);
            hashMap.put("discprice", "" + itemDiscPrice);
            hashMap.put("discnote", "" + itemDiscNote);
            hashMap.put("proid", StationeryRandomKey);
            hashMap.put("date", CurrentDate);
            hashMap.put("time", CurrentTime);

            StationeryDatabaseRef.child(StationeryRandomKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        Toast.makeText(AddStationery_Screen.this, "Stationery added Successfully...", Toast.LENGTH_SHORT).show();
                        ClearData();

                    } else {
                        //failed adding to db
                        progressDialog.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(AddStationery_Screen.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        ClearData();

                    }

                }
            });

        }
    }

    private void ClearData()
    {
        //clear data after uploading a book
        codeED.setText("");
        itemNameED.setText("");
        itemAuthorED.setText("");
        itemPublishED.setText("");
        itemdescripED.setText("");
        itemquantityED.setText("");
        Categoryitem.setText("");
        itempriceED.setText("");
        discPriceItem.setText("");
        disctNoteItem.setText("");
        ItemIMG.setImageResource(R.drawable.addnewbook);
        image_uri = null;
    }

    private void CategoryDialog() {

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stationery Category")
                .setItems(Constants.stationery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked category
                        String category = Constants.stationery[which];

                        //set picked category
                        Categoryitem.setText(category);
                    }
                })
                .show();

    }

    private void showImagePickDialog()
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
            ItemIMG.setImageURI(image_uri);
        }

        else if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == RESULT_OK && data !=null){

            image_uri = data.getData();
            ItemIMG.setImageURI(image_uri);
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
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }
}