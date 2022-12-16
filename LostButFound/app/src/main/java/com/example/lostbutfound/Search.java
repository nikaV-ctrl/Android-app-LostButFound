package com.example.lostbutfound;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostbutfound.Adapter.AnimalsAdapter;
import com.example.lostbutfound.Adapter.MyAdapter;
import com.example.lostbutfound.Adapter.ThingsAdapter;
import com.example.lostbutfound.Model.Animals;
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.Model.Things;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.Index;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Search extends AppCompatActivity {

    private MyAdapter myAdapter;
    private AnimalsAdapter animalsAdapter;
    private ThingsAdapter thingsAdapter;

    private ArrayList<People> peopleArrayList;
    private ArrayList<Animals> animalsArrayList;
    private ArrayList<Things> thingsArrayList;

    private FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();
    private DatabaseReference peopleDatabase = FirebaseDatabase.getInstance().getReference("People");
    private DatabaseReference animalsDatabase = FirebaseDatabase.getInstance().getReference("Animals");
    private DatabaseReference thingsDatabase = FirebaseDatabase.getInstance().getReference("Things");

    private EditText searchEditText;
    private ImageView searchImageView;

    private RecyclerView humanList, animalsList, thingsList;
    private Query query1, query2, query3;

    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.searchEditText);
        searchImageView = findViewById(R.id.searchImageView);
        humanList = findViewById(R.id.humanList);
        animalsList = findViewById(R.id.animalsList);
        thingsList = findViewById(R.id.thingsList);


        humanList.setHasFixedSize(true);
        humanList.setLayoutManager(new LinearLayoutManager(Search.this));
        peopleArrayList = new ArrayList<>();

        animalsList.setHasFixedSize(true);
        animalsList.setLayoutManager(new LinearLayoutManager(Search.this));
        animalsArrayList = new ArrayList<>();

        thingsList.setHasFixedSize(true);
        thingsList.setLayoutManager(new LinearLayoutManager(Search.this));
        thingsArrayList = new ArrayList<>();


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (editable.toString() != null)
//                {
//                    query1 = peopleDatabase.orderByChild("title")
//                            .startAt(editable.toString()).endAt(editable + "\uf8ff");
//                    takeDataFromPeople(query1);
//
//                    query2 = animalsDatabase.orderByChild("title")
//                            .startAt(editable.toString()).endAt(editable + "\uf8ff");
//                    takeDataFromAnimals(query2);
//
//                    query3 = thingsDatabase.orderByChild("title")
//                            .startAt(editable.toString()).endAt(editable + "\uf8ff");
//                    takeDataFromThings(query3);
//                } else {
//                    query1 = peopleDatabase.orderByChild("title")
//                            .startAt("").endAt("" + "\uf8ff");
//                    takeDataFromPeople(query1);
//
//                    query2 = animalsDatabase.orderByChild("title")
//                            .startAt("").endAt("" + "\uf8ff");
//                    takeDataFromAnimals(query2);
//
//                    query3 = thingsDatabase.orderByChild("title")
//                            .startAt("").endAt("" + "\uf8ff");
//                    takeDataFromThings(query3);
//                }

                GoogleSignInAccount googleSignInAccount = GoogleSignIn
                        .getLastSignedInAccount(Search.this);

                GoogleApiClient googleApiClient = new GoogleApiClient.Builder(Search.this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API)
                        .build();




//                Api.Client client = new Api.Client("ZE1W9FFCUZ",
//                        "c4a3a8bc52a1505f0df1cc2a58dfe6bd");
//                Index index = client.getIndex("your_index_name");



            }
        });



//        searchImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String searchQuery = searchEditText.getText().toString();
//
////                Map<String, Object> mapOne = new HashMap<>();
////                mapOne.put("title", searchQuery);
////                Map<String, Object> mapTwo = new HashMap<>();
////                mapTwo.put("title", searchQuery);
////                Map<String, Object> mapThree = new HashMap<>();
////                mapThree.put("title", searchQuery);
////                WriteBatch writeBatch = databaseReference.batch();
//
//
//                query1 = peopleDatabase.orderByChild("title")
//                        .startAt(searchQuery).endAt(searchQuery + "\uf8ff");
//                takeDataFromPeople(query1);
//
//                query2 = animalsDatabase.orderByChild("title")
//                        .startAt(searchQuery).endAt(searchQuery + "\uf8ff");
//                takeDataFromAnimals(query2);
//
//                query3 = thingsDatabase.orderByChild("title")
//                        .startAt(searchQuery).endAt(searchQuery + "\uf8ff");
//                takeDataFromThings(query3);
//            }
//
//        });

    }
    void takeDataFromPeople(Query query){

        peopleArrayList.clear();
        animalsArrayList.clear();
        thingsArrayList.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    People people = dataSnapshot.getValue(People.class);
                    peopleArrayList.add(people);
                }
                myAdapter = new MyAdapter(Search.this, peopleArrayList);
                humanList.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    void takeDataFromAnimals(Query query){

        peopleArrayList.clear();
        animalsArrayList.clear();
        thingsArrayList.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Animals animals = dataSnapshot.getValue(Animals.class);
                    animalsArrayList.add(animals);
                }
                animalsAdapter = new AnimalsAdapter(Search.this, animalsArrayList);
                animalsList.setAdapter(animalsAdapter);
                animalsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    void takeDataFromThings(Query query){

        peopleArrayList.clear();
        animalsArrayList.clear();
        thingsArrayList.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Things things = dataSnapshot.getValue(Things.class);
                    thingsArrayList.add(things);
                }
                thingsAdapter = new ThingsAdapter(Search.this, thingsArrayList);
                thingsList.setAdapter(thingsAdapter);
                thingsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}