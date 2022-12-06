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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Eventbus.MyUpdateCartEvent;
import com.example.jeyabookcentre.Listners.ICartLoadListner;
import com.example.jeyabookcentre.Models.CartModel;
import com.example.jeyabookcentre.Models.Category_section;
import com.example.jeyabookcentre.Models.SearchModel;
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
import java.util.Map;

import butterknife.ButterKnife;

public class Item_View_Screen extends AppCompatActivity implements ICartLoadListner {


    ImageView backBtnImg,BookImg,ChatImg,removeItem,addItem;
    TextView bookName,bookAuthor,bookISBN,bookCategory,bookPrice,bookDesc,bookPublisher,bookQuantity,Discount,bookStatus;
    FrameLayout CartBtn;
    NotificationBadge badge;
    //RelativeLayout DiscountRel;
    int totalQunatity = 1;
    float totalPrice = 0;
    float total_discount = 0;
    AppCompatButton Btn_addcart;
    LinearLayout mainLayout;

    String userID,bStatus;

    FirebaseUser fUser;
    DatabaseReference ref;

    Category_section category_section = null;
    //SearchModel searchModel = null;
    List<CartModel> cartModelList;

    //Rating
    RatingBar ratingStars;
    float myRatings = 0;

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

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view_screen);

        mainLayout = findViewById(R.id.mainLinear);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        //capture the passing data from View all books - ItemAdapter
        final Object object = getIntent().getSerializableExtra("info");
        if (object instanceof Category_section){
            category_section = (Category_section) object;
        }

        backBtnImg = findViewById(R.id.backBtn_view);
        BookImg = findViewById(R.id.bookImg_view);
        ChatImg = findViewById(R.id.chatImg);
        Btn_addcart = findViewById(R.id.addtocart_btn);
        removeItem = findViewById(R.id.decrementBtn);
        addItem = findViewById(R.id.incrementBtn);

        CartBtn = findViewById(R.id.btnCart);
        badge = findViewById(R.id.Badge);

        //rating bar
        ratingStars = findViewById(R.id.rb_ratingBar);

        ratingStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                int rating = (int) v;
                String message = null;

                myRatings = ratingBar.getRating();

                switch (rating)
                {
                    case 0:
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
                Toast.makeText(Item_View_Screen.this, message, Toast.LENGTH_SHORT).show();

            }
        });

        //DiscountRel = findViewById(R.id.discRelative);

        bookName = findViewById(R.id.bookName_view);
        bookAuthor = findViewById(R.id.bookAuthor_view);
        bookISBN = findViewById(R.id.bookISBN_view);
        bookCategory = findViewById(R.id.bookSubCategory_view);
        bookPrice = findViewById(R.id.bookPrice_view);
        bookDesc = findViewById(R.id.descNote_view);
        bookPublisher = findViewById(R.id.bPublish_view);
        bookQuantity = findViewById(R.id.QtyTxt);
        bookStatus = findViewById(R.id.bookStatus_view);

        //Discount.setVisibility(View.INVISIBLE);
        //DiscountRel.setVisibility(View.GONE);
        
        Init();
        countCartItems();

        ChatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Item_View_Screen.this,Chat_Entrance_screen.class);
                startActivity(intent);
            }
        });

        CartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Item_View_Screen.this,Cart_Screen.class);
                startActivity(intent);
            }
        });

        Btn_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userID.isEmpty()){
                    Toast.makeText(Item_View_Screen.this, "You're not logged in,Please Login", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(Item_View_Screen.this);
                    builder.setTitle("Alert!");
                    builder.setMessage("You're not logged in...")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(Item_View_Screen.this,Login_Screen.class);
                                    startActivity(intent);

                                }
                            });
                    builder.show();
                }
                else {
                    ItemAddtoCart();
                    countCartItems();
                }
                /*String CartSUM = String.valueOf(0);
                for (CartModel cartModel : cartModelList)
                    CartSUM += cartModel.getQuantity();
                badge.setNumber(Integer.parseInt(CartSUM));*/

            }
        });

        if(category_section != null){
            Glide.with(getApplicationContext()).load(category_section.getBookimage()).into(BookImg);
            bookName.setText(category_section.getBookname());
            bookAuthor.setText(category_section.getBookauthor());
            bookISBN.setText(category_section.getIsbn());
            bookCategory.setText(category_section.getBookcategory());
            bookPrice.setText(category_section.getBookprice());
            bookDesc.setText(category_section.getBookdesctript());
            bookPublisher.setText(category_section.getBookpublisher());
            bookStatus.setText(category_section.getBookStatus());
            //Discount.setText("- " + category_section.getDiscprice() + " Discount");


            //totalPrice = category_section.getBookprice() * totalQunatity;
            totalPrice = totalQunatity * Float.parseFloat(String.valueOf(category_section.getBookprice()));
            total_discount = totalQunatity * Float.parseFloat(String.valueOf(category_section.getDiscprice()));

        /*    if (category_section.getDiscprice().isEmpty())
            {
                Discount.setVisibility(View.GONE);
                DiscountRel.setVisibility(View.GONE);
            }
            else
            {
                Discount.setVisibility(View.VISIBLE);
                DiscountRel.setVisibility(View.VISIBLE);
            }*/

        }

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalQunatity <= 10)
                {
                    totalQunatity++;
                    bookQuantity.setText(String.valueOf(totalQunatity));
                    totalPrice = totalQunatity * Float.parseFloat(String.valueOf(category_section.getBookprice()));
                    total_discount = totalQunatity * Float.parseFloat(String.valueOf(category_section.getDiscprice()));
                }
                else
                {
                    Toast.makeText(Item_View_Screen.this,"You have exceeded the item limit!",Toast.LENGTH_SHORT).show();
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
                    totalPrice = totalQunatity * Float.parseFloat(String.valueOf(category_section.getBookprice()));
                    total_discount = totalQunatity * Float.parseFloat(String.valueOf(category_section.getDiscprice()));
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
                    Category_section category_section =snapshot.getValue(Category_section.class);
                    category_section.setKey(snapshot.getKey());
                    rateMap.put("bookisbn",category_section.getIsbn());
                    rateMap.put("bookname",category_section.getBookname());
                    rateMap.put("bookauthor",category_section.getBookauthor());
                    rateMap.put("userID",userID);
                    rateMap.put("rating",myRatings);

                    userrating.updateChildren(rateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Item_View_Screen.this,"Item has rated!"+myRatings,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    rateMap.put("bookisbn",category_section.getIsbn());
                    rateMap.put("bookname",category_section.getBookname());
                    rateMap.put("bookauthor",category_section.getBookauthor());
                    rateMap.put("userID",userID);
                    rateMap.put("rating",myRatings);


                    userrating.updateChildren(rateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Item_View_Screen.this,"Item has rated!"+myRatings,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Item_View_Screen.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Init() {

        ButterKnife.bind(this);

        iCartLoadListner = this;
    }

    private void ItemAddtoCart() {
        String saveCurrentDate,saveCurrentTime;

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String ItemRandomKey = saveCurrentDate + saveCurrentTime;

        final HashMap<String,Object> cartMap = new HashMap<>();

        /*cartMap.put("bookisbn",category_section.getIsbn());
        cartMap.put("bookname",category_section.getBookname());
        cartMap.put("bookauthor",category_section.getBookauthor());
        cartMap.put("bookprice",bookPrice.getText().toString());
        cartMap.put("bookpublisher",category_section.getBookpublisher());
        cartMap.put("bookimg",category_section.getBookimage());
        cartMap.put("CurrentDate",saveCurrentDate);
        cartMap.put("CurrentTime",saveCurrentTime);
        cartMap.put("total_quantity",totalQunatity);
        cartMap.put("total_price",totalPrice);
        cartMap.put("discount",total_discount);*/

        DatabaseReference userCart = FirebaseDatabase.getInstance().getReference("CartList").child(userID).child(category_section.getKey());

        userCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {

                        CartModel cartModel = snapshot.getValue(CartModel.class);
                        cartModel.setKey(snapshot.getKey());
                        cartModel.setQuantity(cartModel.getQuantity() + 1);
                        cartMap.put("total_quantity", totalQunatity);
                        cartMap.put("total_price", totalPrice);
                        cartMap.put("discount", total_discount);

                        userCart.updateChildren(cartMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Item_View_Screen.this, "Item added to the cart", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                }
                else {

                        cartMap.put("bookisbn", category_section.getIsbn());
                        cartMap.put("bookname", category_section.getBookname());
                        cartMap.put("bookauthor", category_section.getBookauthor());
                        cartMap.put("bookprice", bookPrice.getText().toString());
                        cartMap.put("bookpublisher", category_section.getBookpublisher());
                        cartMap.put("bookimg", category_section.getBookimage());
                        cartMap.put("CurrentDate", saveCurrentDate);
                        cartMap.put("CurrentTime", saveCurrentTime);
                        cartMap.put("total_quantity", totalQunatity);
                        cartMap.put("total_price", totalPrice);
                        cartMap.put("discount", total_discount);

                        userCart.updateChildren(cartMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Item_View_Screen.this, "Item added to the cart", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Item_View_Screen.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });

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