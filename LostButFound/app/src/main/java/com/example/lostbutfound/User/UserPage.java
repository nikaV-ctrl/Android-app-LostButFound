package com.example.lostbutfound.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.lostbutfound.Adapter.AnimalsAdapter;
import com.example.lostbutfound.Adapter.MyAdapter;
import com.example.lostbutfound.Adapter.RelativesAdapter;
import com.example.lostbutfound.Adapter.ThingsAdapter;
import com.example.lostbutfound.MainActivity2;
import com.example.lostbutfound.Model.Animals;
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.Model.Relatives;
import com.example.lostbutfound.Model.Things;
import com.example.lostbutfound.R;
import com.example.lostbutfound.Search;
import com.example.lostbutfound.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserPage extends AppCompatActivity {

    TextView emailTextView;
    Button logoutBtn;
    private RecyclerView humanList, animalsList, thingsList;

    private MyAdapter myAdapter;
    private AnimalsAdapter animalsAdapter;
    private ThingsAdapter thingsAdapter;

    private ArrayList<People> peopleArrayList;
    private ArrayList<Animals> animalsArrayList;
    private ArrayList<Things> thingsArrayList;

    private DatabaseReference peopleDatabase = FirebaseDatabase.getInstance().getReference("People");
    private DatabaseReference animalsDatabase = FirebaseDatabase.getInstance().getReference("Animals");
    private DatabaseReference thingsDatabase = FirebaseDatabase.getInstance().getReference("Things");

    private Query query;

    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            init();
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            emailTextView.setText(userEmail);

            query = peopleDatabase.orderByChild("user").equalTo(userEmail);
            takeDataFromPeople(query);

            query = animalsDatabase.orderByChild("user").equalTo(userEmail);
            takeDataFromAnimals(query);

            query = thingsDatabase.orderByChild("user").equalTo(userEmail);
            takeDataFromThings(query);

            logoutBtn.setOnClickListener(v-> logout());
        } else{
            startActivity(new Intent(UserPage.this, LoginActivity.class));
        }
    }

    void init(){
        emailTextView = findViewById(R.id.emailTextView);
        logoutBtn = findViewById(R.id.logoutBtn);
        humanList = findViewById(R.id.humanList);
        animalsList = findViewById(R.id.animalsList);
        thingsList = findViewById(R.id.thingsList);

//        RecyclerViewLayoutManager
//                = new LinearLayoutManager(
//                getApplicationContext());
//        humanList.setLayoutManager(
//                RecyclerViewLayoutManager);
//        HorizontalLayout
//                = new LinearLayoutManager(
//                UserPage.this,
//                LinearLayoutManager.HORIZONTAL,
//                false);
//        humanList.setHasFixedSize(true);
//        humanList.setLayoutManager(HorizontalLayout);
//        peopleArrayList = new ArrayList<>();

        humanList.setHasFixedSize(true);
        humanList.setLayoutManager(new LinearLayoutManager(UserPage.this));
        peopleArrayList = new ArrayList<>();

//        RecyclerViewLayoutManager
//                = new LinearLayoutManager(
//                getApplicationContext());
//        animalsList.setLayoutManager(
//                RecyclerViewLayoutManager);
//        HorizontalLayout
//                = new LinearLayoutManager(
//                UserPage.this,
//                LinearLayoutManager.HORIZONTAL,
//                false);
//        animalsList.setHasFixedSize(true);
//        animalsList.setLayoutManager(HorizontalLayout);
//        animalsArrayList = new ArrayList<>();

        animalsList.setHasFixedSize(true);
        animalsList.setLayoutManager(new LinearLayoutManager(UserPage.this));
        animalsArrayList = new ArrayList<>();

//        RecyclerViewLayoutManager
//                = new LinearLayoutManager(
//                getApplicationContext());
//        thingsList.setLayoutManager(
//                RecyclerViewLayoutManager);
//        HorizontalLayout
//                = new LinearLayoutManager(
//                UserPage.this,
//                LinearLayoutManager.HORIZONTAL,
//                false);
//        thingsList.setHasFixedSize(true);
//        thingsList.setLayoutManager(HorizontalLayout);
//        thingsArrayList = new ArrayList<>();

        thingsList.setHasFixedSize(true);
        thingsList.setLayoutManager(new LinearLayoutManager(UserPage.this));
        thingsArrayList = new ArrayList<>();

    }

    void logout(){
        Utility.showToast(UserPage.this, "Вы вышли из аккаунта");
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, MainActivity2.class));
    }

    void takeDataFromPeople(Query query){

        peopleArrayList.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    People people = dataSnapshot.getValue(People.class);
                    peopleArrayList.add(people);
                }
                myAdapter = new MyAdapter(UserPage.this, peopleArrayList);
                humanList.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    void takeDataFromAnimals(Query query){

        animalsArrayList.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Animals animals = dataSnapshot.getValue(Animals.class);
                    animalsArrayList.add(animals);
                }
                animalsAdapter = new AnimalsAdapter(UserPage.this, animalsArrayList);
                animalsList.setAdapter(animalsAdapter);
                animalsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    void takeDataFromThings(Query query){

        thingsArrayList.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Things things = dataSnapshot.getValue(Things.class);
                    thingsArrayList.add(things);
                }
                thingsAdapter = new ThingsAdapter(UserPage.this, thingsArrayList);
                thingsList.setAdapter(thingsAdapter);
                thingsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}