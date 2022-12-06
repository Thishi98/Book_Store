package com.example.jeyabookcentre.Admin_Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.jeyabookcentre.Adapter.AdminItemAdapter;
import com.example.jeyabookcentre.Adapter.StationeryItemAdapter;
import com.example.jeyabookcentre.Listners.IItemLoadListner;
import com.example.jeyabookcentre.Models.Category_section;
import com.example.jeyabookcentre.Models.StationeryModel;
import com.example.jeyabookcentre.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Admin_View_All_Books extends AppCompatActivity implements IItemLoadListner {

    private RecyclerView AllitemRecycler;
    private ImageButton backBTn;
    private LinearLayout mainLayout;

    IItemLoadListner iItemLoadListner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_all_books);

        AllitemRecycler = findViewById(R.id.AllitemRecycler);
        backBTn = findViewById(R.id.allBackBTN);

        //getting books according to it's subcategory
        String type = getIntent().getStringExtra("Sub_Category");
        //getting books according to it's category
        String typo = getIntent().getStringExtra("book_category");
        //getting Stationeries according to it's category
        String types = getIntent().getStringExtra("commoncategory");

        backBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_View_All_Books.this, View_Category_Screen.class);
                startActivity(intent);
            }
        });

        initiate();

        //View all books by it's category
        //to view all Fiction books
        if ("Fiction".equalsIgnoreCase(typo))
        {
            getallFictions();
        }
        //to view all Non - Fiction books
        if ("Non-Fiction".equalsIgnoreCase(typo))
        {
            getallNonFiction();
        }
        //to view all Children books
        if ("Children".equalsIgnoreCase(typo))
        {
            getallChildren();
        }
        //to view all Children books
        if ("commoncategory".equalsIgnoreCase(types))
        {
            getallStationery();
        }

        //Fiction
        if (type != null && type.equalsIgnoreCase("Literary Fiction"))
        {
            getLiteraryFiction();
        }
        if (type != null && type.equalsIgnoreCase("Novel"))
        {
            getNovelFiction();
        }
        if (type != null && type.equalsIgnoreCase("Classics"))
        {
            getClassicFiction();
        }
        if (type != null && type.equalsIgnoreCase("Comics, Graphic Novels, Anime & Manga"))
        {
            getCGAMFiction();
        }
        if (type != null && type.equalsIgnoreCase("Science Fiction"))
        {
            getScienceFiction();
        }
        if (type != null && type.equalsIgnoreCase("Historical Fiction"))
        {
            getHistoryFiction();
        }
        if (type != null && type.equalsIgnoreCase("Short Stories"))
        {
            getShortFictionFrom();
        }

        //Non - Fiction
        if (type != null && type.equalsIgnoreCase("Auto Biography"))
        {
            getAutoBioNonFic();
        }
        if (type != null && type.equalsIgnoreCase("Philosophy"))
        {
            getPhilNonFic();
        }
        if (type != null && type.equalsIgnoreCase("True Crime"))
        {
            getTruCrimeNonFic();
        }
        if (type != null && type.equalsIgnoreCase("Motivational/Inspirational"))
        {
            getMotivateNonFic();
        }
        if (type != null && type.equalsIgnoreCase("Humor & Entertainment"))
        {
            getHumorNonFic();
        }
        if (type != null && type.equalsIgnoreCase("History"))
        {
            getHIstoryNonFic();
        }

        //Children
        if (type != null && type.equalsIgnoreCase("Picture Books"))
        {
            getPictureChild();
        }
        if (type != null && type.equalsIgnoreCase("Babies"))
        {
            getBabyChild();
        }
        if (type != null && type.equalsIgnoreCase("Color & Draw"))
        {
            getColorDrawChild();
        }
        if (type != null && type.equalsIgnoreCase("Coloring Books"))
        {
            getColoringChild();
        }
        if (type != null && type.equalsIgnoreCase("Encyclopedia"))
        {
            getEncyclopediaChild();
        }
        if (type != null && type.equalsIgnoreCase("Chart"))
        {
            getChartChild();
        }
        if (type != null && type.equalsIgnoreCase("General Knowledge"))
        {
            getGKchild();
        }
        if (type != null && type.equalsIgnoreCase("Birds, Animals & Nature"))
        {
            getBANchild();
        }
        if (type != null && type.equalsIgnoreCase("Children Stories"))
        {
            getStoryChild();
        }
        if (type != null && type.equalsIgnoreCase("Nursery Rhymes & Songs"))
        {
            getRymesChild();
        }
        if (type != null && type.equalsIgnoreCase("Essays"))
        {
            getEssaysChild();
        }


    }

    private void getallStationery()
    {
        List<StationeryModel> modelList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Stationery").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (task.isSuccessful())
                        {
                            for (DataSnapshot ds: task.getResult().getChildren())
                            {
                                StationeryModel stationeryModel = ds.getValue(StationeryModel.class);
                                modelList.add(stationeryModel);
                                StationeryItemAdapter itemAdapter = new StationeryItemAdapter(Admin_View_All_Books.this,modelList);
                                AllitemRecycler.setAdapter(itemAdapter);
                            }
                        }

                    }
                });
    }

    //Children
    private void getEssaysChild()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Essays")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section Childmodel = ChildSnapshot.getValue(Category_section.class);
                                Childmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(Childmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void getRymesChild()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Nursery Rhymes & Songs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section Childmodel = ChildSnapshot.getValue(Category_section.class);
                                Childmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(Childmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void getStoryChild()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Children Stories")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section Childmodel = ChildSnapshot.getValue(Category_section.class);
                                Childmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(Childmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void getBANchild()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Birds, Animals & Nature")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section Childmodel = ChildSnapshot.getValue(Category_section.class);
                                Childmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(Childmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void getGKchild()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("General Knowledge")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section Childmodel = ChildSnapshot.getValue(Category_section.class);
                                Childmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(Childmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void getChartChild()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Chart")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section Childmodel = ChildSnapshot.getValue(Category_section.class);
                                Childmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(Childmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void getEncyclopediaChild()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Encyclopedia")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section Childmodel = ChildSnapshot.getValue(Category_section.class);
                                Childmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(Childmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void getColoringChild()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Coloring Books")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section Childmodel = ChildSnapshot.getValue(Category_section.class);
                                Childmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(Childmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }


    private void getColorDrawChild()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Color & Draw")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section Childmodel = ChildSnapshot.getValue(Category_section.class);
                                Childmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(Childmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void getBabyChild()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Babies")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section Childmodel = ChildSnapshot.getValue(Category_section.class);
                                Childmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(Childmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void getPictureChild()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Picture Books")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section Childmodel = ChildSnapshot.getValue(Category_section.class);
                                Childmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(Childmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    //Non - Fiction Books
    private void getHIstoryNonFic()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("History")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot NonFicSnapshot:snapshot.getChildren()){

                                Category_section NonFicmodel = NonFicSnapshot.getValue(Category_section.class);
                                NonFicmodel.setKey(NonFicSnapshot.getKey());
                                categorySections.add(NonFicmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void getHumorNonFic()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Humor & Entertainment")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot NonFicSnapshot:snapshot.getChildren()){

                                Category_section NonFicmodel = NonFicSnapshot.getValue(Category_section.class);
                                NonFicmodel.setKey(NonFicSnapshot.getKey());
                                categorySections.add(NonFicmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    private void getMotivateNonFic()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Motivational/Inspirational")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot NonFicSnapshot:snapshot.getChildren()){

                                Category_section NonFicmodel = NonFicSnapshot.getValue(Category_section.class);
                                NonFicmodel.setKey(NonFicSnapshot.getKey());
                                categorySections.add(NonFicmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    private void getTruCrimeNonFic()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("True Crime")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot NonFicSnapshot:snapshot.getChildren()){

                                Category_section NonFicmodel = NonFicSnapshot.getValue(Category_section.class);
                                NonFicmodel.setKey(NonFicSnapshot.getKey());
                                categorySections.add(NonFicmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    private void getPhilNonFic()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Philosophy")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot NonFicSnapshot:snapshot.getChildren()){

                                Category_section NonFicmodel = NonFicSnapshot.getValue(Category_section.class);
                                NonFicmodel.setKey(NonFicSnapshot.getKey());
                                categorySections.add(NonFicmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    private void getAutoBioNonFic()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Auto Biography")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot NonFicSnapshot:snapshot.getChildren()){

                                Category_section NonFicmodel = NonFicSnapshot.getValue(Category_section.class);
                                NonFicmodel.setKey(NonFicSnapshot.getKey());
                                categorySections.add(NonFicmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    //Fiction Books
    private void getShortFictionFrom()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Short Stories")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot FicSnapshot:snapshot.getChildren()){

                                Category_section Ficmodel = FicSnapshot.getValue(Category_section.class);
                                Ficmodel.setKey(FicSnapshot.getKey());
                                categorySections.add(Ficmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    private void getHistoryFiction()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Historical Fiction")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot FicSnapshot:snapshot.getChildren()){

                                Category_section Ficmodel = FicSnapshot.getValue(Category_section.class);
                                Ficmodel.setKey(FicSnapshot.getKey());
                                categorySections.add(Ficmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    private void getScienceFiction()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Science Fiction")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot FicSnapshot:snapshot.getChildren()){

                                Category_section Ficmodel = FicSnapshot.getValue(Category_section.class);
                                Ficmodel.setKey(FicSnapshot.getKey());
                                categorySections.add(Ficmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    private void getCGAMFiction()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Comics, Graphic Novels, Anime & Manga")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot FicSnapshot:snapshot.getChildren()){

                                Category_section Ficmodel = FicSnapshot.getValue(Category_section.class);
                                Ficmodel.setKey(FicSnapshot.getKey());
                                categorySections.add(Ficmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    private void getClassicFiction()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Classics")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot FicSnapshot:snapshot.getChildren()){

                                Category_section Ficmodel = FicSnapshot.getValue(Category_section.class);
                                Ficmodel.setKey(FicSnapshot.getKey());
                                categorySections.add(Ficmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    private void getNovelFiction()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Novel")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot FicSnapshot:snapshot.getChildren()){

                                Category_section Ficmodel = FicSnapshot.getValue(Category_section.class);
                                Ficmodel.setKey(FicSnapshot.getKey());
                                categorySections.add(Ficmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    private void getLiteraryFiction()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Literary Fiction")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot FicSnapshot:snapshot.getChildren()){

                                Category_section Ficmodel = FicSnapshot.getValue(Category_section.class);
                                Ficmodel.setKey(FicSnapshot.getKey());
                                categorySections.add(Ficmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);
                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                    }
                });
    }

    //Children
    private void getallChildren()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("subbookcategory").equalTo("Picture Books")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section allmodel = ChildSnapshot.getValue(Category_section.class);
                                allmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(allmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    //get all Non - Fictions books
    private void getallNonFiction()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("bookcategory").equalTo("Non-Fiction")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section allmodel = ChildSnapshot.getValue(Category_section.class);
                                allmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(allmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    //get all Fictions books
    private void getallFictions()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("bookcategory").equalTo("Fiction")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ChildSnapshot:snapshot.getChildren()){

                                Category_section allmodel = ChildSnapshot.getValue(Category_section.class);
                                allmodel.setKey(ChildSnapshot.getKey());
                                categorySections.add(allmodel);
                            }
                            iItemLoadListner.onItemLoadSuccess(categorySections);

                        }
                        else {

                            iItemLoadListner.onItemLoadFailed("There's nothing to show...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iItemLoadListner.onItemLoadFailed("There's nothing to show...");
                        // iItemLoadListner.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void initiate()
    {
        ButterKnife.bind(this);

        iItemLoadListner = (IItemLoadListner) this;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        AllitemRecycler.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onItemLoadSuccess(List<Category_section> category_sectionList) {
        AdminItemAdapter adminItemAdapter = new AdminItemAdapter(this,category_sectionList);
        AllitemRecycler.setAdapter(adminItemAdapter);
    }

    @Override
    public void onItemLoadFailed(String message) {
        Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG).show();
    }
}