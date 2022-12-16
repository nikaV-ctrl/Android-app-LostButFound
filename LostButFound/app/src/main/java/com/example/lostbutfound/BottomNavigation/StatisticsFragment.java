package com.example.lostbutfound.BottomNavigation;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.lostbutfound.Adapter.AnimalsAdapter;
import com.example.lostbutfound.Adapter.MyAdapter;
import com.example.lostbutfound.Adapter.ThingsAdapter;
import com.example.lostbutfound.Model.Animals;
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.Model.Things;
import com.example.lostbutfound.R;
import com.example.lostbutfound.Utility;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

public class StatisticsFragment extends Fragment {

    private FrameLayout frameLayout;

    private AnyChartView allStatementsPieChart;
    private AnyChartView allStatementsInPeoplePieChart;
    private AnyChartView allStatementsInAnimalsPieChart;
    private AnyChartView allStatementsInThingsPieChart;

//    PieChart statusPeopleStatementsPieChart;
    RadioGroup radioGroup;
    RadioButton radio_button_1, radio_button_2, radio_button_3, radio_button_4;

    private DatabaseReference allDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference peopleDatabase = FirebaseDatabase.getInstance().getReference("People");
    private DatabaseReference animalsDatabase = FirebaseDatabase.getInstance().getReference("Animals");
    private DatabaseReference thingsDatabase = FirebaseDatabase.getInstance().getReference("Things");
    private ArrayList<People> peopleArrayList;
    private ArrayList<Animals> animalsArrayList;
    private ArrayList<Things> thingsArrayList;
    MyAdapter myAdapter;
    AnimalsAdapter animalsAdapter;
    ThingsAdapter thingsAdapter;
    long numberOfRecordsInHuman;
    long numberOfRecordsInAnimals;
    long numberOfRecordsInThings;
    long[] allStatementsArray = new long[3];
    String[] db = {"человека", "животного", "предмета"};
    String[] db2 = {"Поиски продолжаются", "Человек найден. Жив", "Человек найден. Погиб"};
    String[] db3 = {"Поиски продолжаются", "Животное найдено"};
    String[] db4 = {"Поиски продолжаются", "Вещь найдена"};

    ArrayList<DataEntry> dataEntries, dataEntries1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // получаем объект RadioGroup
        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);

        allStatementsPieChart = view.findViewById(R.id.allStatementsPieChart);
        allStatementsInPeoplePieChart = view.findViewById(R.id.allStatementsInPeoplePieChart);
        allStatementsInAnimalsPieChart = view.findViewById(R.id.allStatementsInAnimalsPieChart);
        allStatementsInThingsPieChart = view.findViewById(R.id.allStatementsInThingsPieChart);
//        statusPeopleStatementsPieChart = view.findViewById(R.id.statusPeopleStatementsPieChart);
//
//
//        Pie piesr = AnyChart.pie();
//        for (int i=0; i<dal.length; i++){
//            dataEntries.add(new ValueDataEntry(db[i], salary[i]));
//        }
//        piesr.data(dataEntries);
//        piesr.title("Количество заявлений");
//        statusPeopleStatementsPieChart.setChart(piesr);

//        statusPeopleStatementsPieChart = view.findViewById(R.id.statusPeopleStatementsPieChart);

//        setupPieChart();
//        loadPieChartData();


        APIlib.getInstance().setActiveAnyChartView(allStatementsPieChart);
        loadAllStatements();
        allStatementsPieChart.setVisibility(View.VISIBLE);
        allStatementsInPeoplePieChart.setVisibility(View.INVISIBLE);
        allStatementsInAnimalsPieChart.setVisibility(View.INVISIBLE);
        allStatementsInThingsPieChart.setVisibility(View.INVISIBLE);
    }

//    private void setupPieChart(){
//
//        statusPeopleStatementsPieChart.setDrawHoleEnabled(true);
//        statusPeopleStatementsPieChart.setUsePercentValues(true);
//        statusPeopleStatementsPieChart.setEntryLabelTextSize(12);
//        statusPeopleStatementsPieChart.setEntryLabelColor(Color.BLACK);
//        statusPeopleStatementsPieChart.setCenterText("fggggggggggggg");
//        statusPeopleStatementsPieChart.setCenterTextSize(24);
//        statusPeopleStatementsPieChart.getDescription().setEnabled(false);
//
//        Legend legend = statusPeopleStatementsPieChart.getLegend();
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        legend.setDrawInside(false);
//        legend.setEnabled(true);
//    }
//
//    private void loadPieChartData(){
//
//        ArrayList<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(20, "Food"));
//        entries.add(new PieEntry(30, "Stay"));
//        entries.add(new PieEntry(70, "Lime"));
//
//        ArrayList<Integer> colors = new ArrayList<>();
//        for (int color: ColorTemplate.MATERIAL_COLORS){
//            colors.add(color);
//        }
//
//        for (int color: ColorTemplate.VORDIPLOM_COLORS){
//            colors.add(color);
//        }
//
//        PieDataSet dataSet = new PieDataSet(entries, "EXPENSE CATEGORY");
//        dataSet.setColors(colors);
//
//        PieData data = new PieData(dataSet);
//        data.setDrawValues(true);
//        data.setValueFormatter(new PercentFormatter(statusPeopleStatementsPieChart));
//        data.setValueTextSize(12f);
//        data.setValueTextColor(Color.BLACK);
//
//        statusPeopleStatementsPieChart.setData(data);
//        statusPeopleStatementsPieChart.invalidate();
//
//        statusPeopleStatementsPieChart.animateY(1400, Easing.EaseInOutQuad);
//    }

    private void loadAllStatements(){

        peopleArrayList = new ArrayList<>();
        animalsArrayList = new ArrayList<>();
        thingsArrayList = new ArrayList<>();

        peopleArrayList.clear();
        animalsArrayList.clear();
        thingsArrayList.clear();

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
                        myAdapter = new MyAdapter(getActivity(), peopleArrayList);
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
                        animalsAdapter = new AnimalsAdapter(getActivity(), animalsArrayList);
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
                        thingsAdapter = new ThingsAdapter(getActivity(), thingsArrayList);
                        thingsAdapter.notifyDataSetChanged();

                        numberOfRecordsInThings = snapshot.getChildrenCount();
                        allStatementsArray[2] = numberOfRecordsInThings;

                        int[] shitValue = {(int) allStatementsArray[0], (int) allStatementsArray[1], (int) allStatementsArray[2]};

                        Pie pie = AnyChart.pie();
                        List<DataEntry> dataEntries = new ArrayList<>();
                        for (int i = 0; i < db.length; i++) {
                            dataEntries.add(new ValueDataEntry(db[i], shitValue[i]));
                        }
                        pie.data(dataEntries);
                        pie.title("Количество заявлений по поиску");
                        allStatementsPieChart.setChart(pie);
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

    private void loadAllStatementsInPeople(){

        peopleArrayList = new ArrayList<>();

        Query statusInProgress = peopleDatabase
                .orderByChild("status").equalTo("Поиски продолжаются");
        Query statusIsSuccessfull = peopleDatabase
                .orderByChild("status").equalTo("Человек найден. Жив");
        Query statusNotSuccessfull = peopleDatabase
                .orderByChild("status").equalTo("Человек найден. Погиб");

        peopleArrayList.clear();

        peopleDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statusInProgress.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            People people = dataSnapshot.getValue(People.class);
                            peopleArrayList.add(people);
                        }
                        myAdapter = new MyAdapter(getActivity(), peopleArrayList);
                        myAdapter.notifyDataSetChanged();

                        numberOfRecordsInHuman = snapshot.getChildrenCount();
                        allStatementsArray[0] = numberOfRecordsInHuman;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                statusIsSuccessfull.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            People people = dataSnapshot.getValue(People.class);
                            peopleArrayList.add(people);
                        }
                        myAdapter = new MyAdapter(getActivity(), peopleArrayList);
                        myAdapter.notifyDataSetChanged();

                        numberOfRecordsInHuman = snapshot.getChildrenCount();
                        allStatementsArray[1] = numberOfRecordsInHuman;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                statusNotSuccessfull.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            People people = dataSnapshot.getValue(People.class);
                            peopleArrayList.add(people);
                        }
                        myAdapter = new MyAdapter(getActivity(), peopleArrayList);
                        myAdapter.notifyDataSetChanged();

                        numberOfRecordsInHuman = snapshot.getChildrenCount();
                        allStatementsArray[2] = numberOfRecordsInHuman;

                        int[] shitValue = {(int) allStatementsArray[0], (int) allStatementsArray[1], (int) allStatementsArray[2]};

                        Pie pie1 = AnyChart.pie();
                        dataEntries1 = new ArrayList<>();
                        for (int i = 0; i < db2.length; i++) {
                            dataEntries1.add(new ValueDataEntry(db2[i], shitValue[i]));
                        }
                        pie1.data(dataEntries1);
                        pie1.title("Количество заявлений по статусу у категории - Розыск человека");
                        allStatementsInPeoplePieChart.setChart(pie1);
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

    private void loadAllStatementsInAnimals(){

        animalsArrayList = new ArrayList<>();

        Query statusInProgress = animalsDatabase
                .orderByChild("status").equalTo("Поиски продолжаются");
        Query statusIsSuccessfull = animalsDatabase
                .orderByChild("status").equalTo("Животное найдено");

        animalsArrayList.clear();

        animalsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statusInProgress.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Animals animals = dataSnapshot.getValue(Animals.class);
                            animalsArrayList.add(animals);
                        }
                        animalsAdapter = new AnimalsAdapter(getActivity(), animalsArrayList);
                        animalsAdapter.notifyDataSetChanged();

                        numberOfRecordsInAnimals = snapshot.getChildrenCount();
                        allStatementsArray[0] = numberOfRecordsInAnimals;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                statusIsSuccessfull.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Animals animals = dataSnapshot.getValue(Animals.class);
                            animalsArrayList.add(animals);
                        }
                        animalsAdapter = new AnimalsAdapter(getActivity(), animalsArrayList);
                        animalsAdapter.notifyDataSetChanged();

                        numberOfRecordsInAnimals = snapshot.getChildrenCount();
                        allStatementsArray[1] = numberOfRecordsInAnimals;

                        int[] shitValue = {(int) allStatementsArray[0], (int) allStatementsArray[1]};

                        Pie pie1 = AnyChart.pie();
                        dataEntries1 = new ArrayList<>();
                        for (int i = 0; i < db3.length; i++) {
                            dataEntries1.add(new ValueDataEntry(db3[i], shitValue[i]));
                        }
                        pie1.title("Количество заявлений по статусу у категории - Поиск животного");
                        pie1.data(dataEntries1);
                        allStatementsInAnimalsPieChart.setChart(pie1);
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

    private void loadAllStatementsInThings(){

        thingsArrayList = new ArrayList<>();

        Query statusInProgress = thingsDatabase
                .orderByChild("status").equalTo("Поиски продолжаются");
        Query statusIsSuccessfull = thingsDatabase
                .orderByChild("status").equalTo("Вещь найдена");

        thingsArrayList.clear();

        thingsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statusInProgress.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Things things = dataSnapshot.getValue(Things.class);
                            thingsArrayList.add(things);
                        }
                        thingsAdapter = new ThingsAdapter(getActivity(), thingsArrayList);
                        thingsAdapter.notifyDataSetChanged();

                        numberOfRecordsInThings = snapshot.getChildrenCount();
                        allStatementsArray[0] = numberOfRecordsInThings;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                statusIsSuccessfull.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Things things = dataSnapshot.getValue(Things.class);
                            thingsArrayList.add(things);
                        }
                        thingsAdapter = new ThingsAdapter(getActivity(), thingsArrayList);
                        thingsAdapter.notifyDataSetChanged();

                        numberOfRecordsInThings = snapshot.getChildrenCount();
                        allStatementsArray[1] = numberOfRecordsInThings;

                        int[] shitValue = {(int) allStatementsArray[0], (int) allStatementsArray[1]};

                        Pie pie1 = AnyChart.pie();
                        dataEntries1 = new ArrayList<>();
                        for (int i = 0; i < db4.length; i++) {
                            dataEntries1.add(new ValueDataEntry(db4[i], shitValue[i]));
                        }
                        pie1.title("Количество заявлений по статусу у категории - Поиск вещей");
                        pie1.data(dataEntries1);
                        allStatementsInThingsPieChart.setChart(pie1);
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


    @Override
    public void onStart() {
        super.onStart();

        // обработка переключения состояния переключателя
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch(id) {
                    case R.id.radio_button_1:
                        APIlib.getInstance().setActiveAnyChartView(allStatementsPieChart);
                        loadAllStatements();
                        allStatementsPieChart.setVisibility(View.VISIBLE);
                        allStatementsInPeoplePieChart.setVisibility(View.INVISIBLE);
                        allStatementsInAnimalsPieChart.setVisibility(View.INVISIBLE);
                        allStatementsInThingsPieChart.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.radio_button_2:
                        APIlib.getInstance().setActiveAnyChartView(allStatementsInPeoplePieChart);
                        loadAllStatementsInPeople();
                        allStatementsInPeoplePieChart.setVisibility(View.VISIBLE);
                        allStatementsPieChart.setVisibility(View.INVISIBLE);
                        allStatementsInAnimalsPieChart.setVisibility(View.INVISIBLE);
                        allStatementsInThingsPieChart.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.radio_button_3:
                        APIlib.getInstance().setActiveAnyChartView(allStatementsInAnimalsPieChart);
                        loadAllStatementsInAnimals();
                        allStatementsInAnimalsPieChart.setVisibility(View.VISIBLE);
                        allStatementsPieChart.setVisibility(View.INVISIBLE);
                        allStatementsInPeoplePieChart.setVisibility(View.INVISIBLE);
                        allStatementsInThingsPieChart.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.radio_button_4:
                        APIlib.getInstance().setActiveAnyChartView(allStatementsInThingsPieChart);
                        loadAllStatementsInThings();
                        allStatementsInThingsPieChart.setVisibility(View.VISIBLE);
                        allStatementsPieChart.setVisibility(View.INVISIBLE);
                        allStatementsInPeoplePieChart.setVisibility(View.INVISIBLE);
                        allStatementsInAnimalsPieChart.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }
            }});
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        allStatementsPieChart.removeAllViews();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}