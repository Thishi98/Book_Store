package com.example.jeyabookcentre.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.jeyabookcentre.Home_Screen;
import com.example.jeyabookcentre.Item_View_Screen;
import com.example.jeyabookcentre.Models.Category_section;
import com.example.jeyabookcentre.Models.SearchModel;
import com.example.jeyabookcentre.R;
import com.example.jeyabookcentre.Search_Item_View_Screen;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MySearchVH> {
    Context context;
    ArrayList<SearchModel> searchModelList;

    String url = "https://book-recommender-icbt.herokuapp.com/predict";

    private FirebaseAnalytics mFirebaseAnalytics;

    private String CurrentDate,CurrentTime,SearchRandKey;

    private FirebaseUser fUser;
    private DatabaseReference ref;
    private String userID;

    public SearchAdapter(Context context, ArrayList<SearchModel> searchModelList) {
        this.context = context;
        this.searchModelList = searchModelList;
    }

    @NonNull
    @Override
    public MySearchVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent,false);
        return new MySearchVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MySearchVH holder, @SuppressLint("RecyclerView") int position) {

       // Glide.with(context).load(list.get(position).getBookimage()).into(holder.searchImg);
        holder.Sname.setText(searchModelList.get(position).getBookname());
        holder.Sauthor.setText(searchModelList.get(position).getBookauthor());
        holder.Scategory.setText(searchModelList.get(position).getSubbookcategory());
        holder.Sprice.setText(searchModelList.get(position).getBookprice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hit the API - volley
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, //got the request method as POST and the API url
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    jsonObject.getString("");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "Can't find any recommendations for you!",Toast.LENGTH_SHORT).show();
                            }
                        }){

                    @Override
                    protected Map<String,String> getParams()
                    {
                        //getting inputs
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("Sname",holder.Sname.getText().toString());
                        params.put("Sauthor",holder.Sauthor.getText().toString());

                        return  params;

                    }
                };

                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(stringRequest);
                //Collecting search data
                AddSearchtoFirebase(position);
                //navigating customers to the search item view screen
                Intent intent = new Intent(context, Search_Item_View_Screen.class);
                intent.putExtra("searchinfo", searchModelList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        });

    }

    private void AddSearchtoFirebase(int position)
    {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyy");
        CurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = currentTime.format(calendar.getTime());

        SearchRandKey = CurrentDate + CurrentTime;

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        SearchModel searchModel = searchModelList.get(position);
        String ISBN = searchModel.getIsbn();
        String BookName =searchModel.getBookname();
        String BookAuothor = searchModel.getBookauthor();
        String BookPublisher = searchModel.getBookpublisher();
        String BookPrice = searchModel.getBookprice();
        String BookCategory = searchModel.getBookcategory();
        String BookSubCategory = searchModel.getSubbookcategory();
        String BookImage = searchModel.getBookimage();

        HashMap<String,Object> searchMap = new HashMap<>();
        searchMap.put("uID","" + userID);
        searchMap.put("isbn","" + ISBN);
        searchMap.put("bookname",""+BookName);
        searchMap.put("bookauthor",""+BookAuothor);
        searchMap.put("bookpublisher",""+BookPublisher);
        searchMap.put("bookprice",""+BookPrice);
        searchMap.put("bookcat",""+BookCategory);
        searchMap.put("booksubcat",""+BookSubCategory);
        searchMap.put("bookimage",""+BookImage);

        DatabaseReference searchreference = FirebaseDatabase.getInstance().getReference("SearchHistory");

        searchreference.child(userID).child(SearchRandKey).updateChildren(searchMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(context,"Your search history will be saved!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchModelList.size();
    }

    public class MySearchVH extends RecyclerView.ViewHolder {

        //ImageView searchImg;
        TextView Sname,Sauthor,Scategory,Sprice;

        public MySearchVH(@NonNull View itemView) {
            super(itemView);


            //searchImg = itemView.findViewById(R.id.searchImg);
            Sname = itemView.findViewById(R.id.searchName);
            Sauthor = itemView.findViewById(R.id.searchAuthor);
            Scategory = itemView.findViewById(R.id.searchCategory);
            Sprice = itemView.findViewById(R.id.searchPrice);

        }
    }
}
