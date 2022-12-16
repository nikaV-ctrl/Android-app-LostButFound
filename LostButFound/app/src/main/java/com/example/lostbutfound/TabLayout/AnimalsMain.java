package com.example.lostbutfound.TabLayout;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.lostbutfound.Adapter.AnimalsAdapter;
import com.example.lostbutfound.Adapter.MyAdapter;
import com.example.lostbutfound.Model.Animals;
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;

public class AnimalsMain extends Fragment {

    private RecyclerView animalsList;
    private AnimalsAdapter animalsAdapter;
    private ArrayList<Animals> list;
    private Chip filter;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("Animals");
    private Query query;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animals_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        createDialog();
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        downloadAllDataFromDb();
    }

    void init(View view){
        animalsList = view.findViewById(R.id.animalsList);
        filter = view.findViewById(R.id.filter);

        // TODO: setHasFixedSize
        animalsList.setHasFixedSize(true);
        animalsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();

        bottomSheetDialog = new BottomSheetDialog(getActivity());
    }

    private void createDialog(){

        View view = getLayoutInflater().inflate(R.layout.filter_animals_modal_sheet, null, false);

        // --------------------------------------------------------

        Chip january_chip = view.findViewById(R.id.january_chip);
        Chip february_chip = view.findViewById(R.id.february_chip);
        Chip march_chip = view.findViewById(R.id.march_chip);
        Chip april_chip = view.findViewById(R.id.april_chip);

        Chip may_chip = view.findViewById(R.id.may_chip);
        Chip june_chip = view.findViewById(R.id.june_chip);
        Chip july_chip = view.findViewById(R.id.july_chip);
        Chip august_chip = view.findViewById(R.id.august_chip);

        Chip september_chip = view.findViewById(R.id.september_chip);
        Chip october_chip = view.findViewById(R.id.october_chip);
        Chip november_chip = view.findViewById(R.id.november_chip);
        Chip december_chip = view.findViewById(R.id.december_chip);

        january_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(1);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        february_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(2);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        march_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(3);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        april_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(4);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });

        may_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(5);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        june_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(6);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        july_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(7);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        august_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(8);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });

        september_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(9);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        october_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(10);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        november_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(11);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        december_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("month").equalTo(12);
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });

        // --------------------------------------------------------

        Chip karaganda_chip = view.findViewById(R.id.karaganda_chip);
        Chip astana_chip = view.findViewById(R.id.astana_chip);
        Chip almata_chip = view.findViewById(R.id.almata_chip);

        karaganda_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("city").equalTo("Караганда");
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        astana_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("city").equalTo("Астана");
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        almata_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("city").equalTo("Алмата");
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });

        // --------------------------------------------------------

        Chip status_in_progress = view.findViewById(R.id.status_in_progress);
        Chip status_successful = view.findViewById(R.id.status_successful);

        status_in_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("status").equalTo("Поиски продолжаются");
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });
        status_successful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = database.orderByChild("status").equalTo("Животное найдено");
                takeDataFromDbByQuery(query);
                bottomSheetDialog.dismiss();
            }
        });

        // --------------------------------------------------------

        bottomSheetDialog.setContentView(view);
    }

    void downloadAllDataFromDb(){

        list.clear();

        database.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Animals animal = dataSnapshot.getValue(Animals.class);

                    LocalDate nowDate = LocalDate.now();
                    LocalDate plus30ToCurrentTime = LocalDate.parse(animal.currentTime).plusDays(30);

                    if(nowDate.isBefore(plus30ToCurrentTime)
                            && nowDate.isAfter(LocalDate.parse(animal.currentTime).minusDays(1))){
                        list.add(animal);
                    } else {
                        Query query = database.orderByChild("id").equalTo(animal.id);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot.exists()) {
                                        dataSnapshot.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
                animalsAdapter = new AnimalsAdapter(getActivity(), list);
                animalsList.setAdapter(animalsAdapter);
                animalsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void takeDataFromDbByQuery(Query query){

        list.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Animals animal = dataSnapshot.getValue(Animals.class);
                    list.add(animal);
                }
                animalsAdapter = new AnimalsAdapter(getActivity(), list);
                animalsList.setAdapter(animalsAdapter);
                animalsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
