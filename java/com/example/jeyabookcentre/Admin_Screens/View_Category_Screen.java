package com.example.jeyabookcentre.Admin_Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Adapter.StationeryHorizontalAdapter;
import com.example.jeyabookcentre.Models.StationeryModel;
import com.example.jeyabookcentre.R;
import com.example.jeyabookcentre.View_All_Books;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class View_Category_Screen extends AppCompatActivity {

    //Category cardview
    private CardView LitFicCard,NovelFicCard,CGAMFicCard,ClassFicCard,ScienFicCard,HistFicCard,ShortFicCard;
    private CardView AutoNonCard,PhilNonCard,TrucriNonCard,MotiveNonCard,HumorNonCard,HistNonCard;
    private CardView pictureChild,babyChild,clrdrwChild,clrChild,encyChild,chartChild,gkChild,banChild,storyChild,songChild,essayChild;

    //Item adding navigation button
    private AppCompatButton addProBTN,addStationBTN;

    //Stationery recycler
    RecyclerView StationeryRec;
    StationeryHorizontalAdapter stationeryAdapter;
    List<StationeryModel> stationeryModerList;

    //Firebase initializing
    private FirebaseUser fUser;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category_screen);

        //Initializing variables
        addProBTN = findViewById(R.id.addbookBTN);
        addStationBTN = findViewById(R.id.addStationBTN);

        //Stationery recycler layout setting
        StationeryRec = findViewById(R.id.StationeryHorizRec);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        StationeryRec.setLayoutManager(layoutManager);

        loadStationeries();

        //Fiction scrollview cards
        LitFicCard = findViewById(R.id.litCardview);
        NovelFicCard = findViewById(R.id.novCardview);
        CGAMFicCard = findViewById(R.id.cgamCardview);
        ClassFicCard = findViewById(R.id.classCardview);
        ScienFicCard = findViewById(R.id.scieCardview);
        HistFicCard = findViewById(R.id.hisCardview);
        ShortFicCard = findViewById(R.id.shortCardview);

        //Sending book categories to the next activity - Fiction
        ShortFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Short Stories");
                startActivity(intent);
            }
        });

        HistFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Historical Fiction");
                startActivity(intent);
            }
        });

        ScienFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Science Fiction");
                startActivity(intent);
            }
        });

        ClassFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Classics");
                startActivity(intent);
            }
        });

        LitFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Literary Fiction");
                startActivity(intent);
            }
        });

        NovelFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Novel");
                startActivity(intent);
            }
        });

        CGAMFicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Comics, Graphic Novels, Anime & Manga");
                startActivity(intent);
            }
        });

        //This will navigate admins to the add book screen
        addStationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, AddStationery_Screen.class);
                startActivity(intent);
            }
        });

        //This will navigate admins to the add book screen
        addProBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, AddBook_Screen.class);
                startActivity(intent);
            }
        });

        //Non-Fiction scrollview cards
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
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Auto Biography");
                startActivity(intent);
            }
        });

        PhilNonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Philosophy");
                startActivity(intent);
            }
        });

        TrucriNonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","True Crime");
                startActivity(intent);
            }
        });

        MotiveNonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Motivational/Inspirational");
                startActivity(intent);
            }
        });

        HumorNonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Humor & Entertainment");
                startActivity(intent);
            }
        });

        HistNonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","History");
                startActivity(intent);
            }
        });

        //Children scrollview cards
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
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Picture Books");
                startActivity(intent);
            }
        });

        babyChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Babies");
                startActivity(intent);
            }
        });

        clrdrwChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Color & Draw");
                startActivity(intent);
            }
        });

        clrChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, View_All_Books.class);
                intent.putExtra("Sub_Category","Coloring Books");
                startActivity(intent);
            }
        });

        encyChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Encyclopedia");
                startActivity(intent);
            }
        });

        chartChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Chart");
                startActivity(intent);
            }
        });

        gkChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","General Knowledge");
                startActivity(intent);
            }
        });

        banChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Birds, Animals & Nature");
                startActivity(intent);
            }
        });

        storyChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Children Stories");
                startActivity(intent);
            }
        });

        songChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Nursery Rhymes & Songs");
                startActivity(intent);
            }
        });

        essayChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
                intent.putExtra("Sub_Category","Essays");
                startActivity(intent);
            }
        });


    }

    private void loadStationeries()
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
                        stationeryAdapter = new StationeryHorizontalAdapter(View_Category_Screen.this,stationeryModerList);
                        StationeryRec.setAdapter(stationeryAdapter);
                    }
                }

            }
        });
    }

    public void viewmore5(View view)
    {
        Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
        intent.putExtra("book_category","Fiction");
        startActivity(intent);

    }

    public void viewmore6(View view)
    {
        Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
        intent.putExtra("book_category","Non-Fiction");
        startActivity(intent);

    }

    public void viewmore7(View view)
    {
        Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
        intent.putExtra("book_category","Children");
        startActivity(intent);

    }

    public void viewmore8(View view)
    {
        Intent intent = new Intent(View_Category_Screen.this, Admin_View_All_Books.class);
        intent.putExtra("commoncategory","Stationery");
        startActivity(intent);

    }

}