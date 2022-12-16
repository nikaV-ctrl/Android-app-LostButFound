package com.example.lostbutfound.BottomNavigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lostbutfound.Adapter.AnimalsAdapter;
import com.example.lostbutfound.Adapter.MyAdapter;
import com.example.lostbutfound.Adapter.ThingsAdapter;
import com.example.lostbutfound.Model.Animals;
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.Model.Things;
import com.example.lostbutfound.R;
import com.example.lostbutfound.Search;
import com.example.lostbutfound.TabLayout.AnimalsMain;
import com.example.lostbutfound.TabLayout.HumanMain;
import com.example.lostbutfound.TabLayout.ThingsMain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private TextView seeAllPeople, seeAllAnimals, seeAllThings;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        query = peopleDatabase.orderByChild("currentTime").limitToLast(3);
        takeDataFromPeople(query);

        query = animalsDatabase.orderByChild("currentTime").limitToLast(3);
        takeDataFromAnimals(query);

        query = thingsDatabase.orderByChild("currentTime").limitToLast(3);
        takeDataFromThings(query);
    }

    void init(View view){
        seeAllPeople = view.findViewById(R.id.seeAllPeople);
        seeAllAnimals = view.findViewById(R.id.seeAllAnimal);
        seeAllThings = view.findViewById(R.id.seeAllThings);

        seeAllPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HumanMain humanMainFragment = new HumanMain();
//                MainFragment mainFragment = new MainFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
//                ft.remove(mainFragment);
//                ft.add(R.id.frameLayout, humanMainFragment);
                ft.replace(R.id.frameLayout, humanMainFragment);
                ft.commit();
            }
        });
        seeAllAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimalsMain animalsMainFragment = new AnimalsMain();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, animalsMainFragment);
                ft.commit();
            }
        });
        seeAllThings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThingsMain thingsMain = new ThingsMain();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, thingsMain);
                ft.commit();
            }
        });

        humanList = view.findViewById(R.id.humanList);
        animalsList = view.findViewById(R.id.animalsList);
        thingsList = view.findViewById(R.id.thingsList);

        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getActivity().getApplicationContext());
        humanList.setLayoutManager(
                RecyclerViewLayoutManager);
        HorizontalLayout
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        humanList.setHasFixedSize(true);
        humanList.setLayoutManager(HorizontalLayout);
        peopleArrayList = new ArrayList<>();

//        humanList.setHasFixedSize(true);
//        humanList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        peopleArrayList = new ArrayList<>();

        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getActivity().getApplicationContext());
        animalsList.setLayoutManager(
                RecyclerViewLayoutManager);
        HorizontalLayout
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        animalsList.setHasFixedSize(true);
        animalsList.setLayoutManager(HorizontalLayout);
        animalsArrayList = new ArrayList<>();

//        animalsList.setHasFixedSize(true);
//        animalsList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        animalsArrayList = new ArrayList<>();

        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getActivity().getApplicationContext());
        thingsList.setLayoutManager(
                RecyclerViewLayoutManager);
        HorizontalLayout
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        thingsList.setHasFixedSize(true);
        thingsList.setLayoutManager(HorizontalLayout);
        thingsArrayList = new ArrayList<>();

//        thingsList.setHasFixedSize(true);
//        thingsList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        thingsArrayList = new ArrayList<>();

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
                myAdapter = new MyAdapter(getActivity(), peopleArrayList);
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
                animalsAdapter = new AnimalsAdapter(getActivity(), animalsArrayList);
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
                thingsAdapter = new ThingsAdapter(getActivity(), thingsArrayList);
                thingsList.setAdapter(thingsAdapter);
                thingsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
