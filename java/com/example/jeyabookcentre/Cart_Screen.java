package com.example.jeyabookcentre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeyabookcentre.Adapter.MyCartAdapter;
import com.example.jeyabookcentre.Models.CartModel;
import com.example.jeyabookcentre.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Cart_Screen extends AppCompatActivity{

    FirebaseDatabase ref;
    FirebaseAuth auth;

    private FirebaseUser fUser;
    private DatabaseReference databaseReference;
    private String userID,userPhone,userName,CurrentDate,CurrentTime,OrdRandomKey;

    TextView SubPrice,totalPrice,Discounts,Username,Userphone;
    AppCompatButton buyNowBtn;
    RelativeLayout mainlayout;
    //SwipeRefreshLayout refreshLayout;

    private ProgressDialog progressDialog;

    RecyclerView cartRec;
    MyCartAdapter cartAdapter;
    List<CartModel> cartModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);

        ref = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        SubPrice = findViewById(R.id.subtotprice);
        totalPrice = findViewById(R.id.totprice);
        Discounts = findViewById(R.id.discounts);
        buyNowBtn = findViewById(R.id.buy_now);
        mainlayout = findViewById(R.id.cart_layout);
        Username = findViewById(R.id.userName);
        Userphone = findViewById(R.id.userphone);
       // refreshLayout = findViewById(R.id.refreshlayout);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users usersdata =snapshot.getValue(Users.class);

                if (usersdata !=null)
                {
                    userName = usersdata.getfName() + " " + usersdata.getlName();
                    userPhone = usersdata.getPhone();

                    Username.setText(userName);
                    Userphone.setText(userPhone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Progress dialog
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

       /* refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Toast.makeText(Cart_Screen.this, "Refreshed!", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);

            }
        });*/

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check whether the cart is empty or not
                if (cartModelList.size() == 0)
                {
                    Toast.makeText(Cart_Screen.this,"No items in cart..",Toast.LENGTH_SHORT).show();
                    return; // can't proceed further
                }
                else {

                    SubmitOrder();
                    SubmitOrderforAdmin();
                }
            }
        });

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReciever,new IntentFilter("MyTotalAmount"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageRecieverdisc,new IntentFilter("MyTotalDiscount"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageRecieversub,new IntentFilter("MySubTotal"));

        cartRec = findViewById(R.id.cartRecycler);
        cartRec.setLayoutManager(new LinearLayoutManager(this));

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(this,cartModelList);

        cartRec.setAdapter(cartAdapter);

        ref.getReference("CartList").child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    for (DataSnapshot cartsnapshot : task.getResult().getChildren()) {

                        if (cartsnapshot.exists()) {
                            CartModel cartModel = cartsnapshot.getValue(CartModel.class);
                            cartModel.setKey(cartsnapshot.getKey());
                            cartModelList.add(cartModel);
                            cartAdapter.notifyDataSetChanged();

                        }
                    }
                }
                else {
                    Toast.makeText(Cart_Screen.this,"Cart is empty",Toast.LENGTH_SHORT).show();
                    //mainlayout.setBackgroundResource(R.drawable.addtocart_icon);
                }
            }
        });

    }

    private void SubmitOrderforAdmin()
    {
        //Progress dialog
        progressDialog.setMessage("Placing order...");
        progressDialog.show();

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        CurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = currentTime.format(calendar.getTime());

        OrdRandomKey = CurrentDate + CurrentTime;

        //for order id and order time
        String timestamp = ""+System.currentTimeMillis();

        //Setup order data
        HashMap<String,Object> ordermap  = new HashMap<>();
        ordermap.put("order_id","" + timestamp);
        ordermap.put("order_date","" + CurrentDate);
        ordermap.put("order_status","InProgress");   //InProgress, canceled, completed
        ordermap.put("Total_price",SubPrice.getText().toString());
        ordermap.put("ordered_by","" + userID);
        ordermap.put("cust_name",Username.getText().toString());
        ordermap.put("cust_phone",Userphone.getText().toString());

        //add to db
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(OrdRandomKey);
        reference.setValue(ordermap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                //order info added to the db and now order items adding to the db
                for (int i=0; i<cartModelList.size();i++)
                {
                    String key = cartModelList.get(i).getKey();
                    String bookImg = cartModelList.get(i).getBookimg();
                    String bookName = cartModelList.get(i).getBookname();
                    String bookAuthor = cartModelList.get(i).getBookauthor();
                    int bookQty = cartModelList.get(i).getTotal_quantity();
                    int totalPrice = cartModelList.get(i).getTotal_price();

                    HashMap<String,Object> ordMap = new HashMap<>();
                    ordMap.put("Cart_Key", key);
                    ordMap.put("bookImg", bookImg);
                    ordMap.put("bookName", bookName);
                    ordMap.put("bookAuthor", bookAuthor);
                    ordMap.put("total_price", totalPrice);
                    ordMap.put("total_quantity",bookQty);

                    reference.child("Cart_Items").child(key).setValue(ordMap);

                }
                progressDialog.dismiss();
                Toast.makeText(Cart_Screen.this,"Order Placed Successfully...",Toast.LENGTH_SHORT).show();

                //Delete the cart items
                ref.getReference("CartList").child(userID).removeValue();

                //Switching cart screen to home screen after placing the order
                Intent intent = new Intent(Cart_Screen.this,Home_Screen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //Failed to placed the order
                progressDialog.dismiss();
                Toast.makeText(Cart_Screen.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void SubmitOrder()
    {
        //Progress dialog
        progressDialog.setMessage("Placing order...");
        progressDialog.show();

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        CurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = currentTime.format(calendar.getTime());

        OrdRandomKey = CurrentDate + CurrentTime;

        //for order id and order time
        String timestamp = ""+System.currentTimeMillis();

        //Setup order data
        HashMap<String,Object> ordermap  = new HashMap<>();
        ordermap.put("order_id","" + timestamp);
        ordermap.put("order_date","" + CurrentDate);
        ordermap.put("order_status","InProgress");   //InProgress, canceled, completed
        ordermap.put("Total_price",SubPrice.getText().toString());
        ordermap.put("ordered_by","" + userID);
        ordermap.put("cust_name",Username.getText().toString());
        ordermap.put("cust_phone",Userphone.getText().toString());

        //add to db
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OrderList").child(userID);
        reference.child(timestamp).setValue(ordermap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                //order info added to the db and now order items adding to the db
                for (int i=0; i<cartModelList.size();i++)
                {
                    String key = cartModelList.get(i).getKey();
                    String bookImg = cartModelList.get(i).getBookimg();
                    String bookName = cartModelList.get(i).getBookname();
                    String bookAuthor = cartModelList.get(i).getBookauthor();
                    int bookQty = cartModelList.get(i).getTotal_quantity();
                    int totalPrice = cartModelList.get(i).getTotal_price();

                    HashMap<String,Object> ordMap = new HashMap<>();
                    ordMap.put("Cart_Key", key);
                    ordMap.put("bookImg", bookImg);
                    ordMap.put("bookName", bookName);
                    ordMap.put("bookAuthor", bookAuthor);
                    ordMap.put("total_price", totalPrice);
                    ordMap.put("total_quantity",bookQty);

                    reference.child(timestamp).child("CartList_Items").child(key).setValue(ordMap);

                }
                progressDialog.dismiss();
                Toast.makeText(Cart_Screen.this,"Order Placed Successfully...",Toast.LENGTH_SHORT).show();

                //Delete the cart items
                ref.getReference("CartList").child(userID).removeValue();

                //Switching cart screen to home screen after placing the order
                Intent intent = new Intent(Cart_Screen.this,Home_Screen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //Failed to placed the order
                progressDialog.dismiss();
                Toast.makeText(Cart_Screen.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    public BroadcastReceiver mMessageReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int totbill = intent.getIntExtra("totalprice",0);
            totalPrice.setText("Rs. "+totbill + ".00");

        }
    };

  /* public BroadcastReceiver mMessageReciever2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String totQuntity = intent.getStringExtra("totalcount");
            totalQty.setText(totQuntity);

        }
    };*/

    public BroadcastReceiver mMessageRecieverdisc = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int discounts = intent.getIntExtra("totaldiscount",0);
            Discounts.setText("Rs. -"+discounts + ".00");

        }
    };

    public BroadcastReceiver mMessageRecieversub = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int subtotalbill = intent.getIntExtra("subtotal",0);
            SubPrice.setText("Rs. "+subtotalbill + ".00");

        }
    };
}