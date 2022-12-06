package com.example.jeyabookcentre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import com.example.jeyabookcentre.Models.StationeryModel;
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

public class Stationery_item_view_screen extends AppCompatActivity implements ICartLoadListner{

    private ImageView backBtnImg,itemImg,ChatImg,removeItem,addItem;
    private TextView itemName,itemAuthor,itemcode,itemCategory,itemPrice,itemDesc,itemPublisher,itemQuantity,Discount,itemStatus;
    private FrameLayout CartBtn;
    private NotificationBadge badge;

    AppCompatButton Btn_addcart;

    //RelativeLayout DiscountRel;
    int totalQunatity = 1;
    float totalPrice = 0;
    float total_discount = 0;
    LinearLayout mainLayout;

    String userID,bStatus;

    FirebaseUser fUser;
    DatabaseReference ref;

    StationeryModel stationeryModelList = null;
    //SearchModel searchModel = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationery_item_view_screen);

        mainLayout = findViewById(R.id.statmainLinear);

        //capture the passing data from View all books - ItemAdapter
        final Object object = getIntent().getSerializableExtra("Sinfo");
        if (object instanceof StationeryModel){
            stationeryModelList = (StationeryModel) object;
        }

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        backBtnImg = findViewById(R.id.backBtn_Statview);
        itemImg = findViewById(R.id.itemImg_view);
        ChatImg = findViewById(R.id.Stat_chatImg);
        Btn_addcart = findViewById(R.id.Stataddtocart_btn);
        removeItem = findViewById(R.id.StatdecrementBtn);
        addItem = findViewById(R.id.StatincrementBtn);

        CartBtn = findViewById(R.id.StatbtnCart);
        badge = findViewById(R.id.StatBadge);
        
        itemName = findViewById(R.id.itemName_Statview);
        itemAuthor = findViewById(R.id.itemAuthor_Statview);
        itemcode = findViewById(R.id.itemcode_Statview);
        itemCategory = findViewById(R.id.itemSubCategory_Statview);
        itemPrice = findViewById(R.id.itemPrice_Statview);
        itemDesc = findViewById(R.id.descNote_Statview);
        itemPublisher = findViewById(R.id.StatPublish_Statview);
        itemQuantity = findViewById(R.id.StatQtyTxt);
        itemStatus = findViewById(R.id.itemStatus_Statview);

        Init();
        countCartItems();

        ChatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Stationery_item_view_screen.this,Chat_Entrance_screen.class);
                startActivity(intent);
            }
        });

        CartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Stationery_item_view_screen.this,Cart_Screen.class);
                startActivity(intent);
            }
        });

        Btn_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userID.isEmpty()){
                    Toast.makeText(Stationery_item_view_screen.this, "You're not logged in,Please Login", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(Stationery_item_view_screen.this);
                    builder.setTitle("Alert!");
                    builder.setMessage("You're not logged in...")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(Stationery_item_view_screen.this,Login_Screen.class);
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


        if(stationeryModelList != null){
            Glide.with(getApplicationContext()).load(stationeryModelList.getItemimage()).into(itemImg);
            itemName.setText(stationeryModelList.getItemname());
            itemAuthor.setText(stationeryModelList.getItemauthor());
            itemcode.setText(stationeryModelList.getItem_code());
            itemCategory.setText(stationeryModelList.getItemcategory());
            itemPrice.setText(stationeryModelList.getItemprice());
            itemDesc.setText(stationeryModelList.getItemdesctript());
            itemPublisher.setText(stationeryModelList.getItempublisher());
            itemStatus.setText(stationeryModelList.getItemStatus());
            //Discount.setText("- " + category_section.getDiscprice() + " Discount");


            //totalPrice = category_section.getBookprice() * totalQunatity;
            totalPrice = totalQunatity * Float.parseFloat(String.valueOf(stationeryModelList.getItemprice()));
            total_discount = totalQunatity * Float.parseFloat(String.valueOf(stationeryModelList.getDiscprice()));

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
                    itemQuantity.setText(String.valueOf(totalQunatity));
                    totalPrice = totalQunatity * Float.parseFloat(String.valueOf(stationeryModelList.getItemprice()));
                    total_discount = totalQunatity * Float.parseFloat(String.valueOf(stationeryModelList.getDiscprice()));
                }
                else
                {
                    Toast.makeText(Stationery_item_view_screen.this,"You have exceeded the item limit!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalQunatity <= 1)
                {
                    totalQunatity--;
                    itemQuantity.setText(String.valueOf(totalQunatity));
                    totalPrice = totalQunatity * Float.parseFloat(String.valueOf(stationeryModelList.getItemprice()));
                    total_discount = totalQunatity * Float.parseFloat(String.valueOf(stationeryModelList.getDiscprice()));
                }

            }
        });


    }

    private void ItemAddtoCart()
    {
        String saveCurrentDate,saveCurrentTime,randKey;

        //getting current date and time
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        randKey = saveCurrentDate + saveCurrentTime;

        final HashMap<String,Object> Map = new HashMap<>();
        Map.put("bookisbn", "" + stationeryModelList.getItem_code());
        Map.put("bookname", "" + stationeryModelList.getItemname());
        Map.put("bookauthor", "" + stationeryModelList.getItemauthor());
        Map.put("bookprice", "" +stationeryModelList.getItemprice());
        Map.put("bookpublisher", "" + stationeryModelList.getItempublisher());
        Map.put("bookimg", "" + stationeryModelList.getItemimage());
        Map.put("CurrentDate", saveCurrentDate);
        Map.put("CurrentTime", saveCurrentTime);
        Map.put("total_quantity", totalQunatity);
        Map.put("total_price", totalPrice);
        Map.put("discount", total_discount);

        DatabaseReference voucherCart = FirebaseDatabase.getInstance().getReference("CartList").child(userID)
                .child(randKey);

        voucherCart.updateChildren(Map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Stationery_item_view_screen.this, "Item added to the cart", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Stationery_item_view_screen.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });

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