package com.example.jeyabookcentre;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.jeyabookcentre.Eventbus.MyUpdateCartEvent;
import com.example.jeyabookcentre.Listners.ICartLoadListner;
import com.example.jeyabookcentre.Models.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class EVoucher_screen extends AppCompatActivity implements ICartLoadListner {

    private CardView voucher1,voucher2,voucher3,voucher4;
    private String userID,voucherRandomKey,saveCurrentDate,saveCurrentTime;
    private ImageButton PromBackBtn;

    private FirebaseUser fUser;
    private DatabaseReference ref;

    //Cart and notification badge
    private FrameLayout CartBtnvoucher;
    private NotificationBadge badgevoucher;

    //Main layout
    private LinearLayout VoucherLayout;

    //Cart item load listner
    ICartLoadListner iCartLoadListner;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItems();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event)
    {
        countCartItems();
    }

    //Cart item counting
    private void countCartItems() {

        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("CartList")
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot cartsnapshop : snapshot.getChildren())
                        {CartModel cartModel = cartsnapshop.getValue(CartModel.class);
                            cartModel.setKey(cartsnapshop.getKey());
                            cartModels.add(cartModel);
                        }
                        iCartLoadListner.onItemCartLoadSuccess(cartModels);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iCartLoadListner.onItemCartLoadFailed(error.getMessage());
                    }
                });

    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evoucher_screen);

        //Firebase ini
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        voucher1 = findViewById(R.id.voucher500);
        voucher2 = findViewById(R.id.voucher1000);
        voucher3 = findViewById(R.id.voucher2500);
        voucher4 = findViewById(R.id.voucher5000);

        countCartItems();
        Init();

        //cart btn and notification badge ini
        CartBtnvoucher = findViewById(R.id.Cartvoucher);

        CartBtnvoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EVoucher_screen.this,Cart_Screen.class);
                startActivity(intent);
            }
        });

        //back btn init
        PromBackBtn = findViewById(R.id.PromBack);
        PromBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //notification badge
        badgevoucher = findViewById(R.id.BadgeVoucher);

        //main layout of xml
        VoucherLayout = findViewById(R.id.voucherLayout);

        //first voucher - 500, alert dialog box
        voucher1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder
                        (EVoucher_screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                builder.setTitle("Select an option");
                builder.setMessage("Select a proceed option...");
                builder.setCancelable(true);

                builder.setPositiveButton("Gift", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setNegativeButton("Add Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ItemAddCart1();
                        countCartItems();
                    }
                });
                builder.show();
            }
        });

        //first voucher - 500, alert dialog box
        voucher2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder
                        (EVoucher_screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                builder.setTitle("Select an option");
                builder.setMessage("Select a proceed option...");
                builder.setCancelable(true);

                builder.setPositiveButton("Gift", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setNegativeButton("Add Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ItemAddCart2();
                        countCartItems();
                    }
                });
                builder.show();
            }
        });

        //first voucher - 500, alert dialog box
        voucher3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder
                        (EVoucher_screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                builder.setTitle("Select an option");
                builder.setMessage("Select a proceed option...");
                builder.setCancelable(true);

                builder.setPositiveButton("Gift", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setNegativeButton("Add Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ItemAddCart3();
                        countCartItems();
                    }
                });
                builder.show();
            }
        });

        //first voucher - 500, alert dialog box
        voucher4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder
                        (EVoucher_screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                builder.setTitle("Select an option");
                builder.setMessage("Select a proceed option...");
                builder.setCancelable(true);

                builder.setPositiveButton("Gift", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setNegativeButton("Add Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ItemAddCart4();
                        countCartItems();
                    }
                });
                builder.show();
            }
        });

    }

    
    private void ItemAddCart4()
    {
        int totalQunatity = 1;
        float totalPrice = 5000;
        float total_discount = 0;

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        voucherRandomKey = saveCurrentDate + saveCurrentTime;

        final HashMap<String,Object> voucherMap = new HashMap<>();
        voucherMap.put("bookisbn", "");
        voucherMap.put("bookname", "Gift Voucher 5000");
        voucherMap.put("bookauthor", "");
        voucherMap.put("bookprice", "5000.00");
        voucherMap.put("bookpublisher", "");
        voucherMap.put("bookimg", "");
        voucherMap.put("CurrentDate", saveCurrentDate);
        voucherMap.put("CurrentTime", saveCurrentTime);
        voucherMap.put("total_quantity", totalQunatity);
        voucherMap.put("total_price", totalPrice);
        voucherMap.put("discount", total_discount);

        DatabaseReference voucherCart = FirebaseDatabase.getInstance().getReference("CartList").child(userID)
                .child(voucherRandomKey);

        voucherCart.updateChildren(voucherMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EVoucher_screen.this, "Item added to the cart", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(EVoucher_screen.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void ItemAddCart3()
    {
        int totalQunatity = 1;
        float totalPrice = 2500;
        float total_discount = 0;

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        voucherRandomKey = saveCurrentDate + saveCurrentTime;

        final HashMap<String,Object> voucherMap = new HashMap<>();
        voucherMap.put("bookisbn", "");
        voucherMap.put("bookname", "Gift Voucher 2500");
        voucherMap.put("bookauthor", "");
        voucherMap.put("bookprice", "2500.00");
        voucherMap.put("bookpublisher", "");
        voucherMap.put("bookimg", "");
        voucherMap.put("CurrentDate", saveCurrentDate);
        voucherMap.put("CurrentTime", saveCurrentTime);
        voucherMap.put("total_quantity", totalQunatity);
        voucherMap.put("total_price", totalPrice);
        voucherMap.put("discount", total_discount);

        DatabaseReference voucherCart = FirebaseDatabase.getInstance().getReference("CartList").child(userID)
                .child(voucherRandomKey);

        voucherCart.updateChildren(voucherMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EVoucher_screen.this, "Item added to the cart", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(EVoucher_screen.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void ItemAddCart1()
    {
        int totalQunatity = 1;
        float totalPrice = 500;
        float total_discount = 0;

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        voucherRandomKey = saveCurrentDate + saveCurrentTime;

        final HashMap<String,Object> voucherMap = new HashMap<>();
        voucherMap.put("bookisbn", "");
        voucherMap.put("bookname", "Gift Voucher 500");
        voucherMap.put("bookauthor", "");
        voucherMap.put("bookprice", "500.00");
        voucherMap.put("bookpublisher", "");
        voucherMap.put("bookimg", "");
        voucherMap.put("CurrentDate", saveCurrentDate);
        voucherMap.put("CurrentTime", saveCurrentTime);
        voucherMap.put("total_quantity", totalQunatity);
        voucherMap.put("total_price", totalPrice);
        voucherMap.put("discount", total_discount);

        DatabaseReference voucherCart = FirebaseDatabase.getInstance().getReference("CartList").child(userID)
                .child(voucherRandomKey);

        voucherCart.updateChildren(voucherMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EVoucher_screen.this, "Item added to the cart", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(EVoucher_screen.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void ItemAddCart2()
    {

        int totalQunatity = 1;
        float totalPrice = 1000;
        float total_discount = 0;

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        voucherRandomKey = saveCurrentDate + saveCurrentTime;

        final HashMap<String,Object> voucherMap = new HashMap<>();
        voucherMap.put("bookisbn", "");
        voucherMap.put("bookname", "Gift Voucher 1000");
        voucherMap.put("bookauthor", "");
        voucherMap.put("bookprice", "1000.00");
        voucherMap.put("bookpublisher", "");
        voucherMap.put("bookimg", R.drawable.giftcard_temp1000);
        voucherMap.put("CurrentDate", saveCurrentDate);
        voucherMap.put("CurrentTime", saveCurrentTime);
        voucherMap.put("total_quantity", totalQunatity);
        voucherMap.put("total_price", totalPrice);
        voucherMap.put("discount", total_discount);

        DatabaseReference voucherCart = FirebaseDatabase.getInstance().getReference("CartList").child(userID)
                .child(voucherRandomKey);

        voucherCart.updateChildren(voucherMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EVoucher_screen.this, "Item added to the cart", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(EVoucher_screen.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void Init() {

        ButterKnife.bind(this);

        iCartLoadListner = this;

    }

    @Override
    public void onItemCartLoadSuccess(List<CartModel> cartModelList)
    {
        int cartSum = 0;
        for (CartModel cartModel: cartModelList)
            cartSum += cartModel.getTotal_quantity();
        badgevoucher.setNumber(cartSum);
    }

    @Override
    public void onItemCartLoadFailed(String message) {
        Snackbar.make(VoucherLayout,message,Snackbar.LENGTH_LONG).show();
    }
}