package com.example.jeyabookcentre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeyabookcentre.Adapter.ItemAdapter;
import com.example.jeyabookcentre.Listners.IItemLoadListner;
import com.example.jeyabookcentre.Models.Category_section;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class View_All_Books extends AppCompatActivity implements IItemLoadListner{

    private RecyclerView AllBookRecycler;
    private ImageButton backBTn;
    private LinearLayout mainLayout;

    IItemLoadListner iItemLoadListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_books);

        AllBookRecycler = findViewById(R.id.AllBookRecycler);
        backBTn = findViewById(R.id.FicBackBTN);

        //getting books according to it's subcategory
        String type = getIntent().getStringExtra("Sub_Category");
        //getting books according to it's category
        String typo = getIntent().getStringExtra("book_category");

        backBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_All_Books.this, Home_Screen.class);
                startActivity(intent);
            }
        });

        init();

        //View all books by it's category
        //to view all Fiction books
        if ("Fiction".equalsIgnoreCase(typo))
        {
            getallFictionFromFirebase();
        }
        //to view all Non - Fiction books
        if ("Non-Fiction".equalsIgnoreCase(typo))
        {
            getallNonFictionFromFirebase();
        }
        //to view all Children books
        if ("Children".equalsIgnoreCase(typo))
        {
            getallChildrenFromFirebase();
        }

        //Fiction
        if (type != null && type.equalsIgnoreCase("Literary Fiction"))
        {
            getLiteraryFictionFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Novel"))
        {
            getNovelFictionFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Classics"))
        {
            getClassicFictionFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Comics, Graphic Novels, Anime & Manga"))
        {
            getCGAMFictionFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Science Fiction"))
        {
            getScienceFictionFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Historical Fiction"))
        {
            getHistoryFictionFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Short Stories"))
        {
            getShortFictionFromFirebase();
        }

        //Non - Fiction
        if (type != null && type.equalsIgnoreCase("Auto Biography"))
        {
            getAutoBioNonFicFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Philosophy"))
        {
            getPhilNonFicFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("True Crime"))
        {
            getTruCrimeNonFicFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Motivational/Inspirational"))
        {
            getMotivateNonFicFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Humor & Entertainment"))
        {
            getHumorNonFicFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("History"))
        {
            getHIstoryNonFicFromFirebase();
        }

        //Children
        if (type != null && type.equalsIgnoreCase("Picture Books"))
        {
            getPictureChildFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Babies"))
        {
            getBabyChildFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Color & Draw"))
        {
            getColorDrawChildFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Coloring Books"))
        {
            getColoringChildFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Encyclopedia"))
        {
            getEncyclopediaChildFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Chart"))
        {
            getChartChildFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("General Knowledge"))
        {
            getGKchildFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Birds, Animals & Nature"))
        {
            getBANchildFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Children Stories"))
        {
            getStoryChildFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Nursery Rhymes & Songs"))
        {
            getRymesChildFromFirebase();
        }
        if (type != null && type.equalsIgnoreCase("Essays"))
        {
            getEssaysChildFromFirebase();
        }

    }

    //get all Children books
    private void getallChildrenFromFirebase()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("bookcategory").equalTo("Children")
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

    //get all Non - Fictions books
    private void getallNonFictionFromFirebase()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("bookcategory").equalTo("Non-Fiction")
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

    //get all Fictions books
    private void getallFictionFromFirebase()
    {
        List<Category_section> categorySections = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("books")
                .orderByChild("bookcategory").equalTo("Fiction")
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

    //Children
    private void getPictureChildFromFirebase()
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

    private void getBabyChildFromFirebase()
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

    private void getColorDrawChildFromFirebase()
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

    private void getColoringChildFromFirebase()
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

    private void getEncyclopediaChildFromFirebase()
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

    private void getChartChildFromFirebase()
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

    private void getGKchildFromFirebase()
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

    private void getBANchildFromFirebase()
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

    private void getStoryChildFromFirebase()
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

    private void getRymesChildFromFirebase()
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

    private void getEssaysChildFromFirebase()
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



    //Non - Fiction Books
    private void getHIstoryNonFicFromFirebase()
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

    private void getHumorNonFicFromFirebase()
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

    private void getMotivateNonFicFromFirebase()
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

    private void getTruCrimeNonFicFromFirebase()
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

    private void getPhilNonFicFromFirebase()
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

    private void getAutoBioNonFicFromFirebase()
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
    private void getShortFictionFromFirebase()
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

    private void getHistoryFictionFromFirebase()
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

    private void getScienceFictionFromFirebase()
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

    private void getCGAMFictionFromFirebase()
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

    private void getClassicFictionFromFirebase()
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

    private void getNovelFictionFromFirebase()
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

    private void getLiteraryFictionFromFirebase()
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

    private void init()
    {
        ButterKnife.bind(this);

        iItemLoadListner = (IItemLoadListner) this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        AllBookRecycler.setLayoutManager(gridLayoutManager);

    }

    @Override
    public void onItemLoadSuccess(List<Category_section> category_sectionList) {
        ItemAdapter itemAdapter = new ItemAdapter(this,category_sectionList);
        AllBookRecycler.setAdapter(itemAdapter);
    }

    @Override
    public void onItemLoadFailed(String message) {
        Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG).show();
        //Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }
}