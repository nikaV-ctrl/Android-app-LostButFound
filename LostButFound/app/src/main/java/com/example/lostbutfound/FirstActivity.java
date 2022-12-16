package com.example.lostbutfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.lostbutfound.Adapter.AnimalsAdapter;
import com.example.lostbutfound.Adapter.MyAdapter;
import com.example.lostbutfound.Adapter.ThingsAdapter;
import com.example.lostbutfound.Model.Animals;
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.Model.Things;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {

    private TextView allStatements, successStatement;
    private DatabaseReference allDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference peopleDatabase = FirebaseDatabase.getInstance().getReference("People");
    private DatabaseReference animalsDatabase = FirebaseDatabase.getInstance().getReference("Animals");
    private DatabaseReference thingsDatabase = FirebaseDatabase.getInstance().getReference("Things");
    private ArrayList<People> peopleArrayList;
    private ArrayList<Animals> animalsArrayList;
    private ArrayList<Things> thingsArrayList;
    private ArrayList<People> peopleArrayListSuccess;
    private ArrayList<Animals> animalsArrayListSuccess;
    private ArrayList<Things> thingsArrayListSuccess;
    MyAdapter myAdapter;
    AnimalsAdapter animalsAdapter;
    ThingsAdapter thingsAdapter;
    long numberOfRecordsInHuman;
    long numberOfRecordsInAnimals;
    long numberOfRecordsInThings;
    long[] allStatementsArray = new long[3];
    long numberOfSuccessRecordsInHuman;
    long numberOfSuccessRecordsInAnimals;
    long numberOfSuccessRecordsInThings;
    long[] allSuccessStatementsArray = new long[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        allStatements = findViewById(R.id.allStatements);
        successStatement = findViewById(R.id.successStatement);

        loadAllStatements();


    }

    private void loadAllStatements(){

        peopleArrayList = new ArrayList<>();
        animalsArrayList = new ArrayList<>();
        thingsArrayList = new ArrayList<>();

        peopleArrayList.clear();
        animalsArrayList.clear();
        thingsArrayList.clear();

        Query statusIsSuccessfullPeople = peopleDatabase
                .orderByChild("status").equalTo("Человек найден. Жив");
        Query statusIsSuccessfullAnimals = animalsDatabase
                .orderByChild("status").equalTo("Животное найдено");
        Query statusIsSuccessfullThings = thingsDatabase
                .orderByChild("status").equalTo("Вещь найдена");

        peopleArrayListSuccess = new ArrayList<>();
        animalsArrayListSuccess = new ArrayList<>();
        thingsArrayListSuccess = new ArrayList<>();

        peopleArrayListSuccess.clear();
        animalsArrayListSuccess.clear();
        thingsArrayListSuccess.clear();

        allDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                peopleDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            People people = dataSnapshot.getValue(People.class);
                            peopleArrayList.add(people);
                        }
                        myAdapter = new MyAdapter(FirstActivity.this, peopleArrayList);
                        myAdapter.notifyDataSetChanged();

                        numberOfRecordsInHuman = snapshot.getChildrenCount();
                        allStatementsArray[0] = numberOfRecordsInHuman;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                animalsDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Animals animals = dataSnapshot.getValue(Animals.class);
                            animalsArrayList.add(animals);
                        }
                        animalsAdapter = new AnimalsAdapter(FirstActivity.this, animalsArrayList);
                        animalsAdapter.notifyDataSetChanged();

                        numberOfRecordsInAnimals = snapshot.getChildrenCount();
                        allStatementsArray[1] = numberOfRecordsInAnimals;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                thingsDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Things things = dataSnapshot.getValue(Things.class);
                            thingsArrayList.add(things);
                        }
                        thingsAdapter = new ThingsAdapter(FirstActivity.this, thingsArrayList);
                        thingsAdapter.notifyDataSetChanged();

                        numberOfRecordsInThings = snapshot.getChildrenCount();
                        allStatementsArray[2] = numberOfRecordsInThings;

                        int[] shitValue = {(int) allStatementsArray[0], (int) allStatementsArray[1], (int) allStatementsArray[2]};
                        Integer numberAllStatements = (int) allStatementsArray[0] + (int) allStatementsArray[1] + (int) allStatementsArray[2];
                        allStatements.setText(numberAllStatements.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                statusIsSuccessfullPeople.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            People people = dataSnapshot.getValue(People.class);
                            peopleArrayListSuccess.add(people);
                        }
                        myAdapter = new MyAdapter(FirstActivity.this, peopleArrayListSuccess);
                        myAdapter.notifyDataSetChanged();

                        numberOfSuccessRecordsInHuman = snapshot.getChildrenCount();
                        allSuccessStatementsArray[0] = numberOfSuccessRecordsInHuman;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                statusIsSuccessfullAnimals.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Animals animals = dataSnapshot.getValue(Animals.class);
                            animalsArrayListSuccess.add(animals);
                        }
                        animalsAdapter = new AnimalsAdapter(FirstActivity.this, animalsArrayListSuccess);
                        animalsAdapter.notifyDataSetChanged();

                        numberOfSuccessRecordsInAnimals = snapshot.getChildrenCount();
                        allSuccessStatementsArray[1] = numberOfSuccessRecordsInAnimals;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                statusIsSuccessfullThings.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Things things = dataSnapshot.getValue(Things.class);
                            thingsArrayListSuccess.add(things);
                        }
                        thingsAdapter = new ThingsAdapter(FirstActivity.this, thingsArrayListSuccess);
                        thingsAdapter.notifyDataSetChanged();

                        numberOfSuccessRecordsInThings = snapshot.getChildrenCount();
                        allSuccessStatementsArray[2] = numberOfSuccessRecordsInThings;

                        int[] shitValueSuccess = {(int) allSuccessStatementsArray[0], (int) allSuccessStatementsArray[1], (int) allSuccessStatementsArray[2]};
                        Integer numberAllSuccessStatements = (int) allSuccessStatementsArray[0] + (int) allSuccessStatementsArray[1] + (int) allSuccessStatementsArray[2];
                        Integer allNum = Integer.parseInt(String.valueOf(allStatements.getText()));
                        Integer percentage = (numberAllSuccessStatements * 100 )/ allNum;
//                        Utility.showToast(FirstActivity.this, "ggg" + percentage + "%"); "% заявлений уже успешно завершены"
                        successStatement.setText(percentage.toString() + "%");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void startMainActivity2(View view){
        startActivity(new Intent(FirstActivity.this, MainActivity2.class));
    }
}