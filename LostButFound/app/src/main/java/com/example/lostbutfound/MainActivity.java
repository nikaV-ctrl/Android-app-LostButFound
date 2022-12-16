package com.example.lostbutfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.lostbutfound.Adapter.AnimalsAdapter;
import com.example.lostbutfound.Adapter.MyAdapter;
import com.example.lostbutfound.Adapter.RelativesAdapter;
import com.example.lostbutfound.Model.Animals;
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.Model.Relatives;
import com.google.common.collect.Maps;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    long[] shit = new long[3];
    long numberOfRecordsInHuman;
    long numberOfRecordsInAnimals;
    long numberOfRecordsInRelatives;

    Button phoneNumberBtn, vatsapBtn, shareBtn;
    String[] db = {"People", "Animals", "Relatives"};
    int[] salary = {16000, 20000, 30000};
    AnyChartView anyChartView;

    TextView peopleText, animalsText, relativesText;

    long[] allRecordsNum;
    public static long allRecordsNumInPeople = 0L;
    public long allRecordsNumInAnimals;
    public long allRecordsNumInRelatives;
    int[] all = new int[3];
//    Integer[] all = new Integer[3];
    private DatabaseReference allDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference peopleDatabase = FirebaseDatabase.getInstance().getReference("People");
    private DatabaseReference animalsDatabase = FirebaseDatabase.getInstance().getReference("Animals");
    private DatabaseReference relativesDatabase = FirebaseDatabase.getInstance().getReference("Relatives");
    private Query query;
    private ArrayList<People> peopleArrayList;
    private ArrayList<Animals> animalsArrayList;
    private ArrayList<Relatives> relativesArrayList;
    MyAdapter myAdapter;
    AnimalsAdapter animalsAdapter;
    RelativesAdapter relativesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberBtn = findViewById(R.id.phoneNumberBtn);
        vatsapBtn = findViewById(R.id.vatsapBtn);
        shareBtn = findViewById(R.id.shareBtn);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Yandex.class);
                startActivity(intent);
            }
        });

        phoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSuccesAlertDialog();

//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:12345"));
//                startActivity(intent);

            }
        });
        vatsapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                showWarningAlertDialog();
                
                String text = "Пропал человек.";
                String text1 = "Имя Вася.";


                // Перекинет на беседу с эти номером
                // Код страны должен быть указан

                String number = "+77015324785";
//
//                String url = "https://api.whatsapp.com/send?phone="+number;
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                startActivity(intent);

//                openWhatsApp();
            }
        });

        peopleText = findViewById(R.id.peopleText);
        animalsText = findViewById(R.id.animalsText);
        relativesText = findViewById(R.id.relativesText);
        anyChartView = findViewById(R.id.anyChartView);

        peopleArrayList = new ArrayList<>();
        animalsArrayList = new ArrayList<>();
        relativesArrayList = new ArrayList<>();

        allDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                peopleDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            People people = dataSnapshot.getValue(People.class);
                            peopleArrayList.add(people);
                        }
                        myAdapter = new MyAdapter(MainActivity.this, peopleArrayList);
                        myAdapter.notifyDataSetChanged();

                        numberOfRecordsInHuman = snapshot.getChildrenCount();
                        peopleText.setText(numberOfRecordsInHuman+"");
                        shit[0] = numberOfRecordsInHuman;
                        relativesDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Relatives relatives = dataSnapshot.getValue(Relatives.class);
                                    relativesArrayList.add(relatives);
                                }
                                relativesAdapter = new RelativesAdapter(MainActivity.this, relativesArrayList);
                                relativesAdapter.notifyDataSetChanged();

                                numberOfRecordsInRelatives = snapshot.getChildrenCount();
                                relativesText.setText("Number: " + numberOfRecordsInRelatives);
                                shit[2] = numberOfRecordsInRelatives;


                                int[] shitValue = {(int) shit[0], (int) shit[1], (int) shit[2]};

                                Pie pie = AnyChart.pie();
                                List<DataEntry> dataEntries = new ArrayList<>();
                                for (int i=0; i<db.length; i++){
                                    dataEntries.add(new ValueDataEntry(db[i], shitValue[i]));
                                }
                                pie.data(dataEntries);
                                pie.title("Количество заявлений");
                                anyChartView.setChart(pie);

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
                animalsDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Animals animals = dataSnapshot.getValue(Animals.class);
                            animalsArrayList.add(animals);
                        }
                        animalsAdapter = new AnimalsAdapter(MainActivity.this, animalsArrayList);
                        animalsAdapter.notifyDataSetChanged();

                        numberOfRecordsInAnimals = snapshot.getChildrenCount();
                        animalsText.setText("Number: " + numberOfRecordsInAnimals);
                        shit[1] = numberOfRecordsInAnimals;
                        relativesDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Relatives relatives = dataSnapshot.getValue(Relatives.class);
                                    relativesArrayList.add(relatives);
                                }
                                relativesAdapter = new RelativesAdapter(MainActivity.this, relativesArrayList);
                                relativesAdapter.notifyDataSetChanged();

                                numberOfRecordsInRelatives = snapshot.getChildrenCount();
                                relativesText.setText("Number: " + numberOfRecordsInRelatives);
                                shit[2] = numberOfRecordsInRelatives;

                                int[] shitValue = {(int) shit[0], (int) shit[1], (int) shit[2]};

                                Pie pie = AnyChart.pie();
                                List<DataEntry> dataEntries = new ArrayList<>();
                                for (int i=0; i<db.length; i++){
                                    dataEntries.add(new ValueDataEntry(db[i], shitValue[i]));
                                }
                                pie.data(dataEntries);
                                pie.title("Количество заявлений");
                                anyChartView.setChart(pie);

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
                relativesDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Relatives relatives = dataSnapshot.getValue(Relatives.class);
                            relativesArrayList.add(relatives);
                        }
                        relativesAdapter = new RelativesAdapter(MainActivity.this, relativesArrayList);
                        relativesAdapter.notifyDataSetChanged();

                        numberOfRecordsInRelatives = snapshot.getChildrenCount();
                        relativesText.setText("Number: " + numberOfRecordsInRelatives);
                        shit[2] = numberOfRecordsInRelatives;



                        int[] shitValue = {(int) shit[0], (int) shit[1], (int) shit[2]};

                        Pie pie = AnyChart.pie();
                        List<DataEntry> dataEntries = new ArrayList<>();
                        for (int i=0; i<db.length; i++){
                            dataEntries.add(new ValueDataEntry(db[i], shitValue[i]));
                        }
                        pie.data(dataEntries);
                        pie.title("Количество заявлений");
                        anyChartView.setChart(pie);

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

    private void showWarningAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog
                .Builder(MainActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.warning_dialog,
                (ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle))
                .setText("Изменить");
        ((TextView) view.findViewById(R.id.textMessage))
                .setText("Хотите изменить заявление?");
        ((TextView) view.findViewById(R.id.buttonActionNo))
                .setText("Нет");
        ((TextView) view.findViewById(R.id.buttonActionYes))
                .setText("Да");
        ((ImageView)view.findViewById(R.id.imageIcon))
                .setImageResource(R.drawable.warning);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonActionYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Utility.showToast(MainActivity.this, "YESSSSSSSSSS");
            }
        });
        view.findViewById(R.id.buttonActionNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Utility.showToast(MainActivity.this, "Nooooooo");
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showSuccesAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog
                .Builder(MainActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.success_dialog,
                (ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle))
                .setText("Заявление успешно создано");
        ((TextView) view.findViewById(R.id.textMessage))
                .setText("Пользователь ... успешно добавил новое заявление");
        ((TextView) view.findViewById(R.id.buttonAction))
                .setText("Ок");
        ((ImageView)view.findViewById(R.id.imageIcon))
                .setImageResource(R.drawable.done);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }


    void snapshotFromPeople(){
        allDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                peopleDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            People people = dataSnapshot.getValue(People.class);
                            peopleArrayList.add(people);
                        }
                        myAdapter = new MyAdapter(MainActivity.this, peopleArrayList);
                        myAdapter.notifyDataSetChanged();

                        numberOfRecordsInHuman = snapshot.getChildrenCount();
                        peopleText.setText(numberOfRecordsInHuman+"");
                        shit[0] = numberOfRecordsInHuman;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                animalsDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Animals animals = dataSnapshot.getValue(Animals.class);
                            animalsArrayList.add(animals);
                        }
                        animalsAdapter = new AnimalsAdapter(MainActivity.this, animalsArrayList);
                        animalsAdapter.notifyDataSetChanged();

                        numberOfRecordsInAnimals = snapshot.getChildrenCount();
                        animalsText.setText("Number: " + numberOfRecordsInAnimals);
                        shit[1] = numberOfRecordsInAnimals;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                relativesDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Relatives relatives = dataSnapshot.getValue(Relatives.class);
                            relativesArrayList.add(relatives);
                        }
                        relativesAdapter = new RelativesAdapter(MainActivity.this, relativesArrayList);
                        relativesAdapter.notifyDataSetChanged();

                        numberOfRecordsInRelatives = snapshot.getChildrenCount();
                        relativesText.setText("Number: " + numberOfRecordsInRelatives);
                        shit[2] = numberOfRecordsInRelatives;

                        int[] shitValue = {(int) shit[0], (int) shit[1], (int) shit[2]};

                        Pie pie = AnyChart.pie();
                        List<DataEntry> dataEntries = new ArrayList<>();
                        for (int i=0; i<db.length; i++){
                            dataEntries.add(new ValueDataEntry(db[i], shitValue[i]));
                        }
                        pie.data(dataEntries);
                        pie.title("Количество заявлений");
                        anyChartView.setChart(pie);

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


    void setupChartView() {

//        Utility.showToast(MainActivity.this, "fuck" + fuck[0] + "-" + fuck[1] + "-" + fuck[2]);

//        String a = peopleText.getText().toString();
//        Integer b = Integer.valueOf(a);
//
//        String aa = peopleText.getText().toString();
//        Integer bb = Integer.valueOf(aa);
//
//        String aaa = peopleText.getText().toString();
//        Integer bbb = Integer.valueOf(aaa);
//
//        Integer sum = b + bb + bbb;
//        Utility.showToast(MainActivity.this, "+++" + sum);
//        Pie pie = AnyChart.pie();
//
//        for (int i=0; i<db.length; i++){
//            dataEntries.add(new ValueDataEntry(db[i], salary[i]));
//        }
//        pie.data(dataEntries);
//        pie.title("Количество заявлений");
//        anyChartView.setChart(pie);

    }

    private void openWhatsApp(){
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }
    public void startMainActivity2(View v) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

}