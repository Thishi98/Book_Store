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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

public class AddBook_Screen extends AppCompatActivity {

    private String ISBN, BookName, BookAuthor, BookPublish, OriPrice, BookDescript, BQuantity, BookCategory, BooksubCategory,DiscPrice
            , DiscNote, CurrentTime, CurrentDate;
    private boolean DiscAvailable = false;

    private ImageButton BookBackBTN;
    private ImageView BookIMG;
    private EditText ISBNed, BNameed, BAuthored, BPublished, Bdescriped, Bquantity, Bprice, discPriceTV, disctNoteTV;
    private TextView CategoryTV, subCategoryTV, ClearTV;
    private AppCompatButton AddBookBTN;

    //permission for constants
    // private static final int CAMERA_REQUEST_CODE = 200;
    // private static final int STORAGE_REQUEST_CODE = 300;

    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 1;
    private static final int IMAGE_PICK_CAMERA_CODE = 2;

    //permission arrays
    private String[] CameraPermission;
    private String[] StoragePermission;
    //Image pick uri
    private Uri image_uri;

    private String ProRandomKey, downloadImageUrl;

    private StorageReference ProductImageRef;
    private DatabaseReference ProDatabaseRef;

    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books_screen);

        BookBackBTN = findViewById(R.id.AddbookBackBTN);
        BookIMG = findViewById(R.id.BookImg);
        ISBNed = findViewById(R.id.edISBN);
        BNameed = findViewById(R.id.edBname);
        BAuthored = findViewById(R.id.edBauthor);
        BPublished = findViewById(R.id.edBpublisher);
        Bdescriped = findViewById(R.id.edBdescription);
        Bquantity = findViewById(R.id.edQuantity);
        CategoryTV = findViewById(R.id.TVCategory);
        subCategoryTV = findViewById(R.id.TVsubCategory);
        Bprice = findViewById(R.id.edBprice);
        discPriceTV = findViewById(R.id.discPrice);
        disctNoteTV = findViewById(R.id.discNote);
        ClearTV = findViewById(R.id.clearallTXT);
        AddBookBTN = findViewById(R.id.bookAddBTN);

        //Database data storing path
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("book_img");
        ProDatabaseRef = FirebaseDatabase.getInstance().getReference().child("books");

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        //initialize array permission
        CameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        StoragePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        BookBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        BookIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog to pick an image
                showImagePickDialog();
            }
        });

        CategoryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick a category
                CategoryDialog();
            }
        });

        subCategoryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCategoryDialog();
            }
        });

        AddBookBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1) Input data
                //2) Validate data
                //3) Add data to db
                InputData();
            }
        });

        ClearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearData();
            }
        });

    }

    private void ClearData() {
        //clear data after uploading a book
        ISBNed.setText("");
        BNameed.setText("");
        BAuthored.setText("");
        BPublished.setText("");
        Bdescriped.setText("");
        Bquantity.setText("");
        CategoryTV.setText("");
        subCategoryTV.setText("");
        Bprice.setText("");
        discPriceTV.setText("");
        disctNoteTV.setText("");
        BookIMG.setImageResource(R.drawable.addnewbook);
        image_uri = null;

    }

    private void CategoryDialog() {
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Book Category")
                .setItems(Constants.books, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked category
                        String category = Constants.books[which];

                        //set picked category
                        CategoryTV.setText(category);
                    }
                })
                .show();

    }

    private void subCategoryDialog() {
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Book Sub-Category")
                .setItems(Constants.sub_books, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked category
                        String subcategory = Constants.sub_books[which];

                        //set picked category
                        subCategoryTV.setText(subcategory);
                    }
                })
                .show();
    }

    private void showImagePickDialog() {
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


    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, StoragePermission, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromGallery() {
        //intent to pick image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_GALLERY_CODE && resultCode == RESULT_OK && data!=null){

            image_uri = data.getData();
            BookIMG.setImageURI(image_uri);
        }

        else if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == RESULT_OK && data !=null){

            image_uri = data.getData();
            BookIMG.setImageURI(image_uri);
        }

    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result; //return true or false
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, CameraPermission, IMAGE_PICK_CAMERA_CODE);

    }

    private void pickFromCamera() {
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

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;

    }

    private void InputData() {
        //1) Input data
        ISBN = ISBNed.getText().toString().trim();
        BookName = BNameed.getText().toString().trim();
        BookAuthor = BAuthored.getText().toString().trim();
        BookPublish = BPublished.getText().toString().trim();
        OriPrice = Bprice.getText().toString().trim();
        BookDescript = Bdescriped.getText().toString().trim();
        BQuantity = Bquantity.getText().toString().trim();
        BookCategory = CategoryTV.getText().toString().trim();
        BooksubCategory = subCategoryTV.getText().toString().trim();
        DiscPrice = discPriceTV.getText().toString().trim();
        DiscNote = disctNoteTV.getText().toString().trim();
       // DiscAvailable = discSwitch.isChecked(); //true or false

        //2) Validated data
        if (TextUtils.isEmpty(ISBN)) {
            Toast.makeText(this, "ISBN is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (TextUtils.isEmpty(BookName)) {
            Toast.makeText(this, "Book title is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (TextUtils.isEmpty(BookAuthor)) {
            Toast.makeText(this, "Author's name required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (TextUtils.isEmpty(OriPrice)) {
            Toast.makeText(this, "Price is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (TextUtils.isEmpty(BQuantity)) {
            Toast.makeText(this, "Quantity is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (TextUtils.isEmpty(BookCategory)) {
            Toast.makeText(this, "Category is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
        if (TextUtils.isEmpty(BooksubCategory)) {
            Toast.makeText(this, "Sub-Category is required", Toast.LENGTH_SHORT).show();
            return;        //don't proceed forward
        }
       else {

           DiscPrice="0";
           DiscNote="";

            StoreImginfo();
        }

    }

    private void StoreImginfo() {
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

        ProRandomKey = CurrentDate + CurrentTime;

        //Product image link
        StorageReference filePath = ProductImageRef.child(image_uri.getLastPathSegment() + ProRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(image_uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddBook_Screen.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddBook_Screen.this, "Book Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();

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

                                            Toast.makeText(AddBook_Screen.this, "Product Image Url got Successfully!", Toast.LENGTH_SHORT).show();

                                            addBooks();
                                        }
                                    }
                                });
                    }
                });

    }

    private void addBooks() {
        //3) Add data to db
        progressDialog.setMessage("Adding Book Details...");
        progressDialog.show();

        if (image_uri == null) {
            //Upload without image

            //setup data to upload
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("isbn", "" + ISBN);
            hashMap.put("bookname", "" + BookName);
            hashMap.put("bookauthor", "" + BookAuthor);
            hashMap.put("bookpublisher", "" + BookPublish);
            hashMap.put("bookprice", "" + OriPrice);
            hashMap.put("bookdesctript", "" + BookDescript);
            hashMap.put("quantity", "" + BQuantity);
            hashMap.put("bookcategory", "" + BookCategory);
            hashMap.put("bookStatus", "In-Stock");
            hashMap.put("subbookcategory", "" + BooksubCategory);
            hashMap.put("bookimage", "");    //no image : set empty
            hashMap.put("discprice", "" + DiscPrice);
            hashMap.put("discnote", "" + DiscNote);
            hashMap.put("discountav", "" + DiscAvailable);
            hashMap.put("proid", ProRandomKey);
            hashMap.put("date", CurrentDate);
            hashMap.put("time", CurrentTime);

            //add to db
            ProDatabaseRef.child(ProRandomKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        Toast.makeText(AddBook_Screen.this, "Product is added Successfully...", Toast.LENGTH_SHORT).show();
                        ClearData();

                    } else {
                        //failed adding to db
                        progressDialog.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(AddBook_Screen.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        ClearData();

                    }
                }
            });
        } else {
            //upload with an image

            //upload image to storage

            //setup data to upload
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("isbn", "" + ISBN);
            hashMap.put("bookname", "" + BookName);
            hashMap.put("bookauthor", "" + BookAuthor);
            hashMap.put("bookpublisher", "" + BookPublish);
            hashMap.put("bookprice", "" + OriPrice);
            hashMap.put("bookdesctript", "" + BookDescript);
            hashMap.put("quantity", "" + BQuantity);
            hashMap.put("bookcategory", "" + BookCategory);
            hashMap.put("bookStatus", "In-Stock");
            hashMap.put("subbookcategory", "" + BooksubCategory);
            hashMap.put("bookimage", "" + downloadImageUrl);
            hashMap.put("discprice", "" + DiscPrice);
            hashMap.put("discnote", "" + DiscNote);
            hashMap.put("discountav", "" + DiscAvailable);
            hashMap.put("proid", ProRandomKey);
            hashMap.put("date", CurrentDate);
            hashMap.put("time", CurrentTime);

            //add to db
            ProDatabaseRef.child(ProRandomKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        Toast.makeText(AddBook_Screen.this, "Product is added Successfully...", Toast.LENGTH_SHORT).show();
                        ClearData();

                    } else {
                        //failed adding to db
                        progressDialog.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(AddBook_Screen.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        ClearData();

                    }
                }
            });

        }

    }

}