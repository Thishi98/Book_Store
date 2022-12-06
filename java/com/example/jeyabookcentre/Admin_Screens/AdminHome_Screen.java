package com.example.jeyabookcentre.Admin_Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.jeyabookcentre.Home_Screen;
import com.example.jeyabookcentre.Login_Screen;
import com.example.jeyabookcentre.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHome_Screen extends AppCompatActivity {

   private RelativeLayout addbook, addprom,manCust,manOrder,bookReq,pubRegister;
   private ImageView Logout;

   FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        ViewAlertDialog();
    }

    private void ViewAlertDialog()
    {
        AlertDialog dialog = new AlertDialog.Builder(AdminHome_Screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        //Initializing variables
        addbook = findViewById(R.id.addbooklayout);
        addprom = findViewById(R.id.addpromlayout);
        manCust = findViewById(R.id.manCustlayout);
        manOrder = findViewById(R.id.manOrdlayout);
        pubRegister =findViewById(R.id.pubReglayout);
        bookReq =findViewById(R.id.BookRequlayout);

        mAuth = FirebaseAuth.getInstance();

        //logout from admin home
        Logout = findViewById(R.id.LogoutAdmin);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(AdminHome_Screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
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
        });

        //This will navigate admins to the category screen(Fiction, Non-Fiction, Children & stationery)
        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bookIntent = new Intent(AdminHome_Screen.this, View_Category_Screen.class);
                startActivity(bookIntent);

            }
        });

        //This will navigate admins to the promotion adding screen
        addprom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent promIntent = new Intent(AdminHome_Screen.this, AddPromotion_Screen.class);
                startActivity(promIntent);
            }
        });

        //This will navigate admins to the view customer screen
        manCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent manCustIntent = new Intent(AdminHome_Screen.this, ManageCustomer_Screen.class);
                startActivity(manCustIntent);

            }
        });

        //This will navigate admins to the view customer Order screen
        manOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent manOrderIntent = new Intent(AdminHome_Screen.this, Customer_orderlist_Screen.class);
                startActivity(manOrderIntent);

            }
        });

        //This will navigate admins to the book_request screen
        bookReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reqIntent = new Intent(AdminHome_Screen.this, View_All_Book_Requests.class);
                startActivity(reqIntent);

            }
        });


        //This will navigate admins to the view customer Order screen
        pubRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pubRegIntent = new Intent(AdminHome_Screen.this, Publisher_Register.class);
                startActivity(pubRegIntent);
            }
        });

    }

    private void SignOut()
    {
        Intent signoutIntent = new Intent(AdminHome_Screen.this, Login_Screen.class);
        signoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signoutIntent);
        finish();
    }
}