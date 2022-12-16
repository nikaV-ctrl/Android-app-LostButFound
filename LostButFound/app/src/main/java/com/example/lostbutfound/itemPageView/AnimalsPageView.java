package com.example.lostbutfound.itemPageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import com.example.lostbutfound.Add.AddAnimalsStatements;
import com.example.lostbutfound.Add.AddPeopleStatements;
import com.example.lostbutfound.MainActivity2;
import com.example.lostbutfound.R;
import com.example.lostbutfound.ThingPage;
import com.example.lostbutfound.Utility;
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
import java.util.HashMap;

public class AnimalsPageView extends AppCompatActivity {

    private ImageView imageView, editImageView, deleteImageView;
    private TextView myTitle, myCircumstances, myDescription,
            myBreed, myLastLocation, myReward, myData,
            extendTextView, extendTextView1;
    private Chip myStatus, myLocation;

    Button whatsapp, call, extendBtn;

    private DatabaseReference animalsDatabase =
            FirebaseDatabase.getInstance().getReference("Animals");

    String id, title, imageUrl, city, circumstances,
            description, breed, lastLocation,
            status, reward, user, phone, currentTime;
    Integer year, month, day;

    boolean isEditMode = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_page_view);

        imageView = findViewById(R.id.imageView);
        editImageView = findViewById(R.id.editImageView);
        deleteImageView = findViewById(R.id.deleteImageView);
        extendTextView = findViewById(R.id.extendTextView);
        extendTextView1 = findViewById(R.id.extendTextView1);
        extendBtn = findViewById(R.id.extendBtn);
        myTitle = findViewById(R.id.myTitle);
        myCircumstances = findViewById(R.id.myCircumstances);
        myDescription = findViewById(R.id.myDescription);
        myBreed = findViewById(R.id.myBreed);
        myLastLocation = findViewById(R.id.myLastLocation);
        myStatus = findViewById(R.id.myStatus);
        myLocation = findViewById(R.id.myLocation);
        myReward = findViewById(R.id.myReward);
        myData = findViewById(R.id.myData);
        whatsapp = findViewById(R.id.whatsapp);
        call = findViewById(R.id.call);

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        imageUrl = getIntent().getStringExtra("imageUrl");
        city = getIntent().getStringExtra("city");
        circumstances = getIntent().getStringExtra("circumstances");
        description = getIntent().getStringExtra("description");
        breed = getIntent().getStringExtra("breed");
        lastLocation = getIntent().getStringExtra("lastLocation");
        status = getIntent().getStringExtra("status");
        reward = getIntent().getStringExtra("reward");
        user = getIntent().getStringExtra("user");
        phone = getIntent().getStringExtra("phone");
        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);
        day = getIntent().getIntExtra("day", 0);
        currentTime = getIntent().getStringExtra( "currentTime");

        myTitle.setText(title);
        myLocation.setText(city);
        myCircumstances.setText(circumstances);
        myDescription.setText(description);
        myBreed.setText(breed);
        myLastLocation.setText(lastLocation);
        myStatus.setText(status);
        myReward.setText(reward);

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
                    Query query = animalsDatabase.orderByChild("id").equalTo(id);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                if(dataSnapshot.exists()){
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("id", id);
                                    hashMap.put("title", title);
                                    hashMap.put("city", city);
                                    hashMap.put("circumstances", circumstances);
                                    hashMap.put("description", description);
                                    hashMap.put("breed", breed);
                                    hashMap.put("lastLocation", lastLocation);
                                    hashMap.put("status", status);
                                    hashMap.put("reward", reward);
                                    hashMap.put("phone", phone);
                                    hashMap.put("user", user);
                                    hashMap.put("imageUrl", imageUrl);
                                    hashMap.put("currentTime", LocalDate.now().toString());
                                    hashMap.put("year", LocalDate.now().getYear());
                                    hashMap.put("month", LocalDate.now().getMonthValue());
                                    hashMap.put("day", LocalDate.now().getDayOfMonth());

                                    dataSnapshot.getRef().updateChildren(hashMap);
                                    Utility.showToast(AnimalsPageView.this,
                                            "Вы успешно продлили заявление");
                                    startActivity(new Intent(AnimalsPageView.this, MainActivity2.class));
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            });


            editImageView.setVisibility(View.VISIBLE);
            deleteImageView.setVisibility(View.VISIBLE);

            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog
                            .Builder(AnimalsPageView.this, R.style.AlertDialogTheme);
                    view = LayoutInflater.from(AnimalsPageView.this).inflate(
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
                            Intent intent = new Intent(AnimalsPageView.this, AddAnimalsStatements.class);
                            intent.putExtra("isEditMode", isEditMode);
                            intent.putExtra("id", id);
                            intent.putExtra("title", title);
                            intent.putExtra("imageUrl", imageUrl);
                            intent.putExtra("city", city);
                            intent.putExtra("circumstances", circumstances);
                            intent.putExtra("description", description);
                            intent.putExtra("breed", breed);
                            intent.putExtra("lastLocation", lastLocation);
                            intent.putExtra("status", status);
                            intent.putExtra("reward", reward);
                            intent.putExtra("user", user);
                            intent.putExtra("phone", phone);
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
                            .Builder(AnimalsPageView.this, R.style.AlertDialogTheme);
                    view = LayoutInflater.from(AnimalsPageView.this).inflate(
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

                            Query query = animalsDatabase.orderByChild("id").equalTo(id);

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        if (dataSnapshot.exists()) {
                                            dataSnapshot.getRef().removeValue();


                                            AlertDialog.Builder builder = new AlertDialog
                                                    .Builder(AnimalsPageView.this, R.style.AlertDialogTheme);
                                            View view = LayoutInflater.from(AnimalsPageView.this).inflate(
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
                                                    startActivity(new Intent(AnimalsPageView.this, MainActivity2.class));
                                                }
                                            });
                                            if (alertDialog.getWindow() != null) {
                                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                            }
                                            alertDialog.show();
                                        } else {
                                            Utility.showToast(AnimalsPageView.this,
                                                    "Заявление не найдено");
                                            startActivity(new Intent(AnimalsPageView.this, MainActivity2.class));
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