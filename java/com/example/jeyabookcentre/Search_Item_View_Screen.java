package com.example.jeyabookcentre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Eventbus.MyUpdateCartEvent;
import com.example.jeyabookcentre.Listners.ICartLoadListner;
import com.example.jeyabookcentre.Models.CartModel;
import com.example.jeyabookcentre.Models.Category_section;
import com.example.jeyabookcentre.Models.SearchModel;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Search_Item_View_Screen extends AppCompatActivity implements ICartLoadListner {

    ImageView backBtnImg,BookImg,ChatImg,removeItem,addItem;
    TextView bookName,bookAuthor,bookISBN,bookCategory,bookPrice,bookDesc,bookPublisher,bookQuantity,Discount;
    FrameLayout CartBtn;
    NotificationBadge badge;
    //RelativeLayout DiscountRel;
    int totalQunatity = 1;
    float totalPrice = 0;
    float total_discount = 0;
    AppCompatButton Btn_addcart;
    LinearLayout mainLayout;

    String userID;

    FirebaseUser fUser;
    DatabaseReference ref;

    //Rating
    RatingBar ratingStar;
    float myRatings = 0;

    SearchModel searchModel = null;
    List<CartModel> cartModelList;

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

    private void countCartItems()
    {
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
        setContentView(R.layout.activity_search_item_view_screen);

        //capture the passing data from home screen - SearchAdapter
        final  Object object1 = getIntent().getSerializableExtra("searchinfo");
        if (object1 instanceof SearchModel) {
            searchModel = (SearchModel) object1;
        }

        //Initializing
        mainLayout = findViewById(R.id.SmainLinear);
        backBtnImg = findViewById(R.id.backBtn_sview);
        BookImg = findViewById(R.id.sbookImg_view);
        ChatImg = findViewById(R.id.schatImg);
        Btn_addcart = findViewById(R.id.saddtocart_btn);
        removeItem = findViewById(R.id.sdecrementBtn);
        addItem = findViewById(R.id.sincrementBtn);

        CartBtn = findViewById(R.id.sbtnCart);
        badge = findViewById(R.id.sBadge);

        bookName = findViewById(R.id.sbookName_view);
        bookAuthor = findViewById(R.id.sbookAuthor_view);
        bookISBN = findViewById(R.id.sbookISBN_view);
        bookCategory = findViewById(R.id.sbookSubCategory_view);
        bookPrice = findViewById(R.id.sbookPrice_view);
        bookDesc = findViewById(R.id.sdescNote_view);
        bookPublisher = findViewById(R.id.sbPublish_view);
        bookQuantity = findViewById(R.id.sQtyTxt);

        //rating bar
        ratingStar = findViewById(R.id.ratingBar);

        ratingStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                int rating = (int) v;
                String message = null;

                myRatings = ratingBar.getRating();

                switch (rating)
                {
                    case 0:
                        message = "Is that bad!";
                        GetItemRatings();
                        break;

                    case 1:
                        message = "Sorry to here that!";
                        GetItemRatings();
                        break;

                    case 2:
                        message = "You can requests favourites from the app!";
                        GetItemRatings();
                        break;

                    case 3:
                        message = "Good enough!";
                        GetItemRatings();
                        break;
                    case 4:
                        message = "Thank You!";
                        GetItemRatings();
                        break;
                    case 5:
                        message = "Awesome! ";
                        GetItemRatings();
                        break;
                }
                Toast.makeText(Search_Item_View_Screen.this, message, Toast.LENGTH_SHORT).show();

            }
        });


        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        ChatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search_Item_View_Screen.this,Chat_Entrance_screen.class);
                startActivity(intent);
            }
        });

        if (searchModel != null)
        {
            Glide.with(getApplicationContext()).load(searchModel.getBookimage()).into(BookImg);
            bookName.setText(searchModel.getBookname());
            bookAuthor.setText(searchModel.getBookauthor());
            bookISBN.setText(searchModel.getIsbn());
            bookCategory.setText(searchModel.getBookcategory());
            bookPrice.setText(searchModel.getBookprice());
            bookDesc.setText(searchModel.getBookdesctript());
            bookPublisher.setText(searchModel.getBookpublisher());

            //totalPrice = category_section.getBookprice() * totalQunatity;
            totalPrice = totalQunatity * Float.parseFloat(String.valueOf(searchModel.getBookprice()));
            total_discount = totalQunatity * Float.parseFloat(String.valueOf(searchModel.getDiscprice()));

        }

        Init();
        countCartItems();

        CartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search_Item_View_Screen.this,Cart_Screen.class);
                startActivity(intent);
            }
        });

        Btn_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userID.isEmpty()){
                    Toast.makeText(Search_Item_View_Screen.this, "You're not logged in,Please Login", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(Search_Item_View_Screen.this);
                    builder.setTitle("Alert!");
                    builder.setMessage("You're not logged in...")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(Search_Item_View_Screen.this,Login_Screen.class);
                                    startActivity(intent);

                                }
                            });
                    builder.show();
                }
                else {
                    SItemAddtoCart();
                    countCartItems();
                }

            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalQunatity <= 10)
                {
                    totalQunatity++;
                    bookQuantity.setText(String.valueOf(totalQunatity));
                    totalPrice = totalQunatity * Float.parseFloat(String.valueOf(searchModel.getBookprice()));
                    total_discount = totalQunatity * Float.parseFloat(String.valueOf(searchModel.getDiscprice()));
                }
                else
                {
                    Toast.makeText(Search_Item_View_Screen.this,"You have exceeded the item limit!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalQunatity <= 1)
                {
                    totalQunatity--;
                    bookQuantity.setText(String.valueOf(totalQunatity));
                    totalPrice = totalQunatity * Float.parseFloat(String.valueOf(searchModel.getBookprice()));
                    total_discount = totalQunatity * Float.parseFloat(String.valueOf(searchModel.getDiscprice()));
                }


            }
        });
    }

    private void GetItemRatings()
    {
        String saveCurrentDate,saveCurrentTime;

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String RatingRandomKey = saveCurrentDate + saveCurrentTime;

        final HashMap<String,Object> rateMap = new HashMap<>();

        DatabaseReference userrating = FirebaseDatabase.getInstance().getReference("Ratings").child(RatingRandomKey);

        userrating.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    SearchModel searchModel =snapshot.getValue(SearchModel.class);
                    searchModel.setKey(snapshot.getKey());
                    rateMap.put("bookisbn",searchModel.getIsbn());
                    rateMap.put("bookname",searchModel.getBookname());
                    rateMap.put("bookauthor",searchModel.getBookauthor());
                    rateMap.put("userID",userID);
                    rateMap.put("rating",myRatings);

                    userrating.updateChildren(rateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Search_Item_View_Screen.this,"Item has rated!"+myRatings,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    rateMap.put("bookisbn",searchModel.getIsbn());
                    rateMap.put("bookname",searchModel.getBookname());
                    rateMap.put("bookauthor",searchModel.getBookauthor());
                    rateMap.put("userID",userID);
                    rateMap.put("rating",myRatings);


                    userrating.updateChildren(rateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Search_Item_View_Screen.this,"Item has rated!"+myRatings,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Search_Item_View_Screen.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SItemAddtoCart()
    {
        String saveCurrentDate,saveCurrentTime;

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String ItemRandomKey = saveCurrentDate + saveCurrentTime;

        final HashMap<String,Object> cartMap = new HashMap<>();

        DatabaseReference userCart = FirebaseDatabase.getInstance().getReference("CartList").child(userID).child(searchModel.getKey());

        userCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    CartModel cartModel =snapshot.getValue(CartModel.class);
                    cartModel.setKey(snapshot.getKey());
                    cartModel.setQuantity(cartModel.getQuantity()+1);
                    cartMap.put("total_quantity",totalQunatity);
                    cartMap.put("total_price",totalPrice);
                    cartMap.put("discount",total_discount);

                    userCart.updateChildren(cartMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Search_Item_View_Screen.this,"Item added to the cart",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
                else {

                    cartMap.put("bookisbn",searchModel.getIsbn());
                    cartMap.put("bookname",searchModel.getBookname());
                    cartMap.put("bookauthor",searchModel.getBookauthor());
                    cartMap.put("bookprice",bookPrice.getText().toString());
                    cartMap.put("bookpublisher",searchModel.getBookpublisher());
                    cartMap.put("bookimg",searchModel.getBookimage());
                    cartMap.put("CurrentDate",saveCurrentDate);
                    cartMap.put("CurrentTime",saveCurrentTime);
                    cartMap.put("total_quantity",totalQunatity);
                    cartMap.put("total_price",totalPrice);
                    cartMap.put("discount",total_discount);

                    userCart.updateChildren(cartMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Search_Item_View_Screen.this,"Item added to the cart",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Search_Item_View_Screen.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Init()
    {
        ButterKnife.bind(this);

        iCartLoadListner = this;
    }

    @Override
    public void onItemCartLoadSuccess(List<CartModel> cartModelList) {

        int CartSum = 0;
        for (CartModel cartModel: cartModelList)
            CartSum += cartModel.getTotal_quantity();
        badge.setNumber(CartSum);

    }

    @Override
    public void onItemCartLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }
}