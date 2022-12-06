package com.example.jeyabookcentre;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Adapter.ChatListAdapter;
import com.example.jeyabookcentre.Adapter.PromotionAdapter;
import com.example.jeyabookcentre.Adapter.RecommenderAdapter;
import com.example.jeyabookcentre.Adapter.SearchAdapter;
import com.example.jeyabookcentre.Adapter.SliderAdapter;
import com.example.jeyabookcentre.Adapter.StationeryHorizontalAdapter;
import com.example.jeyabookcentre.Admin_Screens.AdminHome_Screen;
import com.example.jeyabookcentre.Admin_Screens.View_Category_Screen;
import com.example.jeyabookcentre.Eventbus.MyUpdateCartEvent;
import com.example.jeyabookcentre.Listners.ICartLoadListner;
import com.example.jeyabookcentre.Models.CartModel;
import com.example.jeyabookcentre.Models.Category_section;
import com.example.jeyabookcentre.Models.Promotions;
import com.example.jeyabookcentre.Models.PublisherModel;
import com.example.jeyabookcentre.Models.SearchModel;
import com.example.jeyabookcentre.Models.StationeryModel;
import com.example.jeyabookcentre.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Home_Screen extends AppCompatActivity implements ICartLoadListner, NavigationView.OnNavigationItemSelectedListener {

    //Category cardview
    private CardView LitFicCard,NovelFicCard,CGAMFicCard,ClassFicCard,ScienFicCard,HistFicCard,ShortFicCard;
    private CardView AutoNonCard,PhilNonCard,TrucriNonCard,MotiveNonCard,HumorNonCard,HistNonCard;
    private CardView pictureChild,babyChild,clrdrwChild,clrChild,encyChild,chartChild,gkChild,banChild,storyChild,songChild,essayChild;
    private Toolbar toolbar;

    //Drawer layout
    private DrawerLayout drawerLayout;

    //Cart and notification badge
    private FrameLayout CartBtn;
    private NotificationBadge badgeHome;

    //Textview
    TextView UserName,UserPhone;

    //Search variables
    private AutoCompleteTextView txtSearch;
    //private ArrayList<Category_section> category_sections;
    private RecyclerView listData;
    //private SearchAdapter searchAdapter;
    DatabaseReference searchref;

    //String variables for Textview
    String userID,uName,uPhone;

    //Stationery recycler
    RecyclerView StationeryRec;
    StationeryHorizontalAdapter stationeryAdapter;
    List<StationeryModel> stationeryModerList;

    //recommender recycler
    RecyclerView recomenderRec;
    RecommenderAdapter recommenderAdapter;
    List<Category_section> categorySections;

    //Firebase initializing
    private FirebaseUser fUser;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;

    //Promotion recycler
    RecyclerView promRec;
    SliderAdapter sliderAdapter;
    List<Promotions> promotionsList;

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

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        ViewAlertDialog();
    }

    private void ViewAlertDialog()
    {
        AlertDialog dialog = new AlertDialog.Builder(Home_Screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
                .setTitle("LogOut")
                .setMessage("Are you sure to Logout?")
                .setNegativeButton("No", (dialog1, which) -> {
                    dialog1.dismiss();
                })
                .setPositiveButton("Yes", (dialog2, which) -> {

                    mAuth.signOut();
                    SignOut();

                    dialog2.dismiss();
                }).create();
        dialog.show();
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
        setContentView(R.layout.activity_home_screen);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        //init auth
        mAuth = FirebaseAuth.getInstance();

        searchref = FirebaseDatabase.getInstance().getReference("books");

        //Stationery recycler layout setting
        StationeryRec = findViewById(R.id.StationRec);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        StationeryRec.setLayoutManager(layoutManager);

        loadStationery();

        //Recommendation recycler layout setting
        recomenderRec = findViewById(R.id.recommendRec);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recomenderRec.setLayoutManager(linearLayoutManager);

        loadrecommenderlist();

        //Init variables
        toolbar = findViewById(R.id.HomeNav);
        setSupportActionBar(toolbar);
        toolbar.getOverflowIcon().setTint(ContextCompat.getColor(this,R.color.purple_500));   // menu icon style
        drawerLayout = findViewById(R.id.DrawerLayout);
        UserName = findViewById(R.id.userName);
        UserPhone = findViewById(R.id.userphone);
        CartBtn = findViewById(R.id.CartButton);
        badgeHome = findViewById(R.id.BadgeHome);

        //promotion recycler layout setting
        promRec = findViewById(R.id.promoRec);
        LinearLayoutManager HorizlayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        promRec.setLayoutManager(HorizlayoutManager);

        loadpromotions();

        //Drawer perform
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.purple_500));
        toggle.syncState();

        //Navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.NavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        Init();
        countCartItems();

        // Getting logged user name to the home activity
        ref.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userdata =snapshot.getValue(Users.class);

                if (userdata !=null)
                {
                    uName = userdata.getfName() + " " + userdata.getlName();
                    uPhone = userdata.getPhone();

                    UserName.setText(uName);
                    UserPhone.setText(uPhone);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home_Screen.this,"Proceeding without logging...",Toast.LENGTH_SHORT).show();
            }
        });


        LitFicCard = findViewById(R.id.litCardview);
        NovelFicCard = findViewById(R.id.novCardview);
        CGAMFicCard = findViewById(R.id.cgamCardview);
        ClassFicCard = findViewById(R.id.classCardview);
        ScienFicCard = findViewById(R.id.scieCardview);
        HistFicCard = findViewById(R.id.hisCardview);
        ShortFicCard = findViewById(R.id.shortCardview);

        CartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Screen.this,Cart_Screen.class);
                startActivity(intent);
            }
        });

        //Sending book categories to the next activity - Fiction
        ShortFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Short Stories");
                startActivity(intent);
            }
        });

        HistFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Historical Fiction");
                startActivity(intent);
            }
        });

        ScienFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Science Fiction");
                startActivity(intent);
            }
        });

        ClassFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Classics");
                startActivity(intent);
            }
        });

        LitFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Literary Fiction");
                startActivity(intent);
            }
        });

        NovelFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Novel");
                startActivity(intent);
            }
        });

        CGAMFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Comics, Graphic Novels, Anime & Manga");
                startActivity(intent);
            }
        });

        AutoNonCard = findViewById(R.id.autoBioCardview);
        PhilNonCard = findViewById(R.id.philCardview);
        TrucriNonCard = findViewById(R.id.truecrimeCardview);
        MotiveNonCard = findViewById(R.id.motiveCardview);
        HumorNonCard = findViewById(R.id.humorCardview);
        HistNonCard = findViewById(R.id.historyCardview);

        //Sending book categories to the next activity - Non Fiction
        AutoNonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Auto Biography");
                startActivity(intent);
            }
        });

        PhilNonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Philosophy");
                startActivity(intent);
            }
        });

        TrucriNonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","True Crime");
                startActivity(intent);
            }
        });

        MotiveNonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Motivational/Inspirational");
                startActivity(intent);
            }
        });

        HumorNonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Humor & Entertainment");
                startActivity(intent);
            }
        });

        HistNonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","History");
                startActivity(intent);
            }
        });

        pictureChild = findViewById(R.id.picChilCardview);
        babyChild = findViewById(R.id.babeChilCardview);
        clrdrwChild = findViewById(R.id.clrndrwChilCardview);
        clrChild = findViewById(R.id.clringChilCardview);
        encyChild = findViewById(R.id.encyChilCardview);
        chartChild = findViewById(R.id.chartChilCardview);
        gkChild = findViewById(R.id.generalChilCardview);
        banChild = findViewById(R.id.banChilCardview);
        storyChild = findViewById(R.id.storyChilCardview);
        songChild = findViewById(R.id.nurseryChilCardview);
        essayChild = findViewById(R.id.essayChilCardview);

        //Sending book categories to the next activity - Children
        pictureChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Picture Books");
                startActivity(intent);
            }
        });

        babyChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Babies");
                startActivity(intent);
            }
        });

        clrdrwChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Color & Draw");
                startActivity(intent);
            }
        });

        clrChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Coloring Books");
                startActivity(intent);
            }
        });

        encyChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Encyclopedia");
                startActivity(intent);
            }
        });

        chartChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Chart");
                startActivity(intent);
            }
        });

        gkChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","General Knowledge");
                startActivity(intent);
            }
        });

        banChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Birds, Animals & Nature");
                startActivity(intent);
            }
        });

        storyChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Children Stories");
                startActivity(intent);
            }
        });

        songChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Nursery Rhymes & Songs");
                startActivity(intent);
            }
        });

        essayChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Essays");
                startActivity(intent);
            }
        });

        //Search function initializing
        txtSearch = findViewById(R.id.searchField);
        listData = findViewById(R.id.searchRV);
        RecyclerView.LayoutManager searchlayoutManager = new LinearLayoutManager(this);
        listData.setLayoutManager(searchlayoutManager);

        PopulateSearch();

    }

    private void loadrecommenderlist()
    {
        categorySections = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("books").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful())
                {
                    for (DataSnapshot ds: task.getResult().getChildren())
                    {
                        Category_section category_section = ds.getValue(Category_section.class);
                        categorySections.add(category_section);
                        recommenderAdapter = new RecommenderAdapter(Home_Screen.this,categorySections);
                        recomenderRec.setAdapter(recommenderAdapter);
                    }
                }

            }
        });

    }

    private void loadStationery()
    {
        stationeryModerList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Stationery").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful())
                {
                    for (DataSnapshot ds: task.getResult().getChildren())
                    {
                        StationeryModel stationeryModel = ds.getValue(StationeryModel.class);
                        stationeryModerList.add(stationeryModel);
                        stationeryAdapter = new StationeryHorizontalAdapter(Home_Screen.this,stationeryModerList);
                        StationeryRec.setAdapter(stationeryAdapter);
                    }
                }

            }
        });
    }

    private void loadpromotions()
    {
       promotionsList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("promotions").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful())
                {
                    for (DataSnapshot ds: task.getResult().getChildren())
                    {
                        Promotions promotions = ds.getValue(Promotions.class);
                        promotionsList.add(promotions);
                        sliderAdapter = new SliderAdapter(Home_Screen.this,promotionsList);
                        promRec.setAdapter(sliderAdapter);
                    }
                }

            }
        });
    }

    private void PopulateSearch()
    {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    ArrayList<String> itemnames = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String itemname = ds.child("bookname").getValue(String.class);
                        String itemAuthor = ds.child("bookauthor").getValue(String.class);
                        itemnames.add(itemname);
                        itemnames.add(itemAuthor);
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,itemnames);
                    txtSearch.setAdapter(arrayAdapter);
                    txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String selection = adapterView.getItemAtPosition(i).toString();
                            getItems(selection);
                            clearTxt();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        searchref.addListenerForSingleValueEvent(eventListener);
    }

    private void clearTxt()
    {
        txtSearch.setText("");
    }

    private void getItems(String selection)
    {
        Query query = searchref.orderByChild("bookname").equalTo(selection);
        Query query1 = searchref.orderByChild("bookauthor").equalTo(selection);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                   ArrayList<SearchModel> searchInfos = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        SearchModel searchInfo  = new SearchModel(ds.getKey(),ds.child("bookname").getValue(String.class),
                                ds.child("bookauthor").getValue(String.class),ds.child("subbookcategory").getValue(String.class)
                                ,ds.child("bookprice").getValue(String.class),ds.child("isbn").getValue(String.class)
                                ,ds.child("bookpublisher").getValue(String.class),ds.child("bookdesctript").getValue(String.class)
                                ,ds.child("bookcategory").getValue(String.class),ds.child("bookimage").getValue(String.class)
                                ,ds.child("discprice").getValue(String.class),ds.child("discnote").getValue(String.class)
                                ,ds.child("quantity").getValue(String.class),ds.child("bookStatus").getValue(String.class));
                        searchInfos.add(searchInfo);
                    }

                    SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(),searchInfos);
                    listData.setAdapter(searchAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(eventListener);
        query1.addListenerForSingleValueEvent(eventListener);
    }

    private void Init() {

        ButterKnife.bind(this);

        iCartLoadListner = this;

    }

    @Override
    public void onItemCartLoadSuccess(List<CartModel> cartModelList) {

        int cartSum = 0;
        for (CartModel cartModel: cartModelList)
            cartSum += cartModel.getTotal_quantity();
        badgeHome.setNumber(cartSum);

    }

    @Override
    public void onItemCartLoadFailed(String message) {
        Snackbar.make(drawerLayout,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.home_menu:
                Intent homeIntent = new Intent(Home_Screen.this, Home_Screen.class);
                startActivity(homeIntent);
                Toast.makeText(Home_Screen.this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.myCart_menu:
                Intent cartIntent = new Intent(Home_Screen.this, Cart_Screen.class);
                startActivity(cartIntent);
                break;
            case R.id.myWhishlist_menu:
                Intent favIntent = new Intent(Home_Screen.this, Favourite_list.class);
                startActivity(favIntent);
                break;
            case R.id.request_menu:
                Intent reqIntent = new Intent(Home_Screen.this, Book_Request.class);
                startActivity(reqIntent);
                break;
            case R.id.order_menu:
                Intent ordIntent = new Intent(Home_Screen.this, OrderHistory_Screen.class);
                startActivity(ordIntent);
                Toast.makeText(Home_Screen.this, "Order History", Toast.LENGTH_SHORT).show();
                break;
            case R.id.promotions_menu:
                Intent PromIntent = new Intent(Home_Screen.this, ViewAllProm_Customer.class);
                startActivity(PromIntent);
                Toast.makeText(Home_Screen.this, "Promotions", Toast.LENGTH_SHORT).show();
                break;
            case R.id.evoucher_menu:
                Intent eVouchIntent = new Intent(Home_Screen.this, EVoucher_screen.class);
                startActivity(eVouchIntent);
                Toast.makeText(Home_Screen.this, "e-Vouchers", Toast.LENGTH_SHORT).show();
                break;
            case R.id.chat_menu:
                Intent chatIntent = new Intent(Home_Screen.this, Chat_Entrance_screen.class);
                startActivity(chatIntent);
                break;
            case R.id.aboutUs_menu:
                Toast.makeText(Home_Screen.this, "About Us", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //auto generated method

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);

        if (menu instanceof MenuBuilder)
        {
            MenuBuilder m =(MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.AccManage:
                Intent ProfIntent = new Intent(Home_Screen.this, User_Profile.class);
                startActivity(ProfIntent);
                break;
            case R.id.logout:
                AlertDialog dialog = new AlertDialog.Builder(Home_Screen.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
                        .setTitle("LogOut")
                        .setMessage("Are you sure to Logout?")
                        .setNegativeButton("No", (dialog1, which) -> dialog1.dismiss())
                        .setPositiveButton("Yes", (dialog2, which) -> {

                            mAuth.signOut();
                            SignOut();

                            dialog2.dismiss();
                        }).create();
                dialog.show();
                break;
            case R.id.settings:
                break;
        }
        return true;
    }

    //signout and clean task
    private void SignOut()
    {
        Intent signoutIntent = new Intent(Home_Screen.this,Login_Screen.class);
        signoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signoutIntent);
        finish();
    }

    public void viewmore1(View view)
    {
        Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
        intent.putExtra("book_category","Fiction");
        startActivity(intent);

    }

    public void viewmore2(View view)
    {
        Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
        intent.putExtra("book_category","Non-Fiction");
        startActivity(intent);

    }

    public void viewmore3(View view)
    {
        Intent intent = new Intent(Home_Screen.this, View_All_Books.class);
        intent.putExtra("book_category","Children");
        startActivity(intent);

    }

}