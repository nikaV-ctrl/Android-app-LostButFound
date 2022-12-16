package com.example.lostbutfound;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lostbutfound.Adapter.ThingsAdapter;
import com.example.lostbutfound.Model.Things;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class ThingPage extends AppCompatActivity {

    private ThingsAdapter thingsAdapter;
    private ArrayList<Things> thingsArrayList;
    private DatabaseReference thingsDatabase = FirebaseDatabase.getInstance().getReference("Things");
    private Query query;
    private ImageView imageView, editImageView, deleteImageView;
    private TextView myTitle, myDescription, myLastLocation, myData,
            extendTextView, extendTextView1;
    private Chip myStatus, myLocation;
    Button whatsapp, call, extendBtn;
    String id, title, imageUrl, location, status, description,
            lastLocation, user, phone, currentTime;
    Integer year, month, day;

    boolean isEditMode = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_page);

        imageView = findViewById(R.id.imageView);
        editImageView = findViewById(R.id.editImageView);
        deleteImageView = findViewById(R.id.deleteImageView);
        extendTextView = findViewById(R.id.extendTextView);
        extendTextView1 = findViewById(R.id.extendTextView1);
        extendBtn = findViewById(R.id.extendBtn);
        myTitle = findViewById(R.id.myTitle);
        myDescription = findViewById(R.id.myDescription);
        myLastLocation = findViewById(R.id.myLastLocation);
        myStatus = findViewById(R.id.myStatus);
        myLocation = findViewById(R.id.myLocation);
        myData = findViewById(R.id.myData);
        whatsapp = findViewById(R.id.whatsapp);
        call = findViewById(R.id.call);

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        lastLocation = getIntent().getStringExtra("lastLocation");
        location = getIntent().getStringExtra("location");
        status = getIntent().getStringExtra("status");
        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);
        day = getIntent().getIntExtra("day", 0);
        imageUrl = getIntent().getStringExtra("imageUrl");
        user = getIntent().getStringExtra("user");
        phone = getIntent().getStringExtra( "phone");
        currentTime = getIntent().getStringExtra( "currentTime");

        myTitle.setText(title);
        myDescription.setText(description);
        myLastLocation.setText(lastLocation);
        myLocation.setText(location);
        myStatus.setText(status);

        if (month == 10 || month == 11 || month == 12){
            myData.setText(day + "." + month + "." + year);
        } else {
            myData.setText(day + ".0" + month + "." + year);
        }

        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(imageView);

        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(user)) {

            extendBtn.setVisibility(View.VISIBLE);
            extendTextView.setVisibility(View.VISIBLE);
            extendTextView1.setVisibility(View.VISIBLE);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
            LocalDate plus30ToCurrentTime = LocalDate.parse(currentTime).plusDays(30);
            String activeUntilDate = formatter.format(plus30ToCurrentTime);
            extendTextView1.setText("Заявление активно до " + activeUntilDate);

            extendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Query query = thingsDatabase.orderByChild("id").equalTo(id);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                if(dataSnapshot.exists()){
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("id", id);
                                    hashMap.put("title", title);
                                    hashMap.put("location", location);
                                    hashMap.put("description", description);
                                    hashMap.put("lastLocation", lastLocation);
                                    hashMap.put("status", status);
                                    hashMap.put("phone", phone);
                                    hashMap.put("user", user);
                                    hashMap.put("imageUrl", imageUrl);
                                    hashMap.put("currentTime", LocalDate.now().toString());
                                    hashMap.put("year", LocalDate.now().getYear());
                                    hashMap.put("month", LocalDate.now().getMonthValue());
                                    hashMap.put("day", LocalDate.now().getDayOfMonth());

                                    dataSnapshot.getRef().updateChildren(hashMap);
                                    Utility.showToast(ThingPage.this,
                                            "Вы успешно продлили заявление");
                                    startActivity(new Intent(ThingPage.this, MainActivity2.class));
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            });


//            if(nowDate.isBefore(plus30ToCurrentTime)
//                    && nowDate.isAfter(LocalDate.parse(currentTime))){
//                Utility.showToast(ThingPage.this, "входит");
//            } else{
//                Utility.showToast(ThingPage.this, "не входит");
//            }

            // data create or update
            // data + 30 day
            // [dataCreateUpdate ; data+30] if now is in



            editImageView.setVisibility(View.VISIBLE);
            deleteImageView.setVisibility(View.VISIBLE);

            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog
                            .Builder(ThingPage.this, R.style.AlertDialogTheme);
                    view = LayoutInflater.from(ThingPage.this).inflate(
                            R.layout.warning_dialog,
                            (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
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
                    ((ImageView) view.findViewById(R.id.imageIcon))
                            .setImageResource(R.drawable.warning);

                    final AlertDialog alertDialog = builder.create();

                    view.findViewById(R.id.buttonActionYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            isEditMode = true;
                            Intent intent = new Intent(ThingPage.this, AddThings.class);
                            intent.putExtra("isEditMode", isEditMode);
                            intent.putExtra("title", title);
                            intent.putExtra("imageUrl", imageUrl);
                            intent.putExtra("description", description);
                            intent.putExtra("lastLocation", lastLocation);
                            intent.putExtra("location", location);
                            intent.putExtra("status", status);
                            intent.putExtra("phone", phone);
                            intent.putExtra("id", id);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        }
                    });
                    view.findViewById(R.id.buttonActionNo).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    if (alertDialog.getWindow() != null) {
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    alertDialog.show();
                }
            });
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog
                            .Builder(ThingPage.this, R.style.AlertDialogTheme);
                    view = LayoutInflater.from(ThingPage.this).inflate(
                            R.layout.warning_dialog,
                            (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
                    );
                    builder.setView(view);
                    ((TextView) view.findViewById(R.id.textTitle))
                            .setText("Удалить");
                    ((TextView) view.findViewById(R.id.textMessage))
                            .setText("Хотите удалить заявление?");
                    ((TextView) view.findViewById(R.id.buttonActionNo))
                            .setText("Нет");
                    ((TextView) view.findViewById(R.id.buttonActionYes))
                            .setText("Да");
                    ((ImageView) view.findViewById(R.id.imageIcon))
                            .setImageResource(R.drawable.warning);

                    final AlertDialog alertDialog = builder.create();

                    view.findViewById(R.id.buttonActionYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();

                            Query query = thingsDatabase.orderByChild("id").equalTo(id);

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        if (dataSnapshot.exists()) {
                                            dataSnapshot.getRef().removeValue();

                                            AlertDialog.Builder builder = new AlertDialog
                                                    .Builder(ThingPage.this, R.style.AlertDialogTheme);
                                            View view = LayoutInflater.from(ThingPage.this).inflate(
                                                    R.layout.success_dialog,
                                                    (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
                                            );
                                            builder.setView(view);
                                            ((TextView) view.findViewById(R.id.textTitle))
                                                    .setText("Заявление успешно удалено!");
                                            ((TextView) view.findViewById(R.id.textMessage))
                                                    .setText("Теперь данное заявление отсутствует в базе данных");
                                            ((TextView) view.findViewById(R.id.buttonAction))
                                                    .setText("Ок");
                                            ((ImageView) view.findViewById(R.id.imageIcon))
                                                    .setImageResource(R.drawable.done);

                                            final AlertDialog alertDialog = builder.create();

                                            view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    alertDialog.dismiss();
                                                    startActivity(new Intent(ThingPage.this, MainActivity2.class));
                                                }
                                            });
                                            if (alertDialog.getWindow() != null) {
                                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                            }
                                            alertDialog.show();
                                        } else {
                                            Utility.showToast(ThingPage.this,
                                                    "Заявление не найдено");
                                            startActivity(new Intent(ThingPage.this, MainActivity2.class));
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    });
                    view.findViewById(R.id.buttonActionNo).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    if (alertDialog.getWindow() != null) {
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    alertDialog.show();

                }
            });


            // 1, 21,  день
            // 2, 3, 4, 22, 23, 24,  дня
            // 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, дней
            // 16, 17, 18, 19, 20, 25, 26, 27, 28, 29, 30 дней

        }

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone="+phone;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });
    }
}