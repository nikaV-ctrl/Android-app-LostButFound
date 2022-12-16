package com.example.lostbutfound.itemPageView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lostbutfound.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RelativePageView extends AppCompatActivity {

    private ImageView imageView, editImageView, deleteImageView;
    private TextView myTitle, myAge, mySex, myCircumstances,
            myStatus, myLocation, myData;
    Button whatsapp, call;

    private DatabaseReference relativesDatabase =
            FirebaseDatabase.getInstance().getReference("Relatives");

    String id, title, imageUrl, city, status, circumstances, sex, user;
    Integer age, year, month, day, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_page_view);

        imageView = findViewById(R.id.imageView);
        editImageView = findViewById(R.id.editImageView);
        deleteImageView = findViewById(R.id.deleteImageView);
        myTitle = findViewById(R.id.myTitle);
        myAge = findViewById(R.id.myAge);
        mySex = findViewById(R.id.mySex);
        myCircumstances = findViewById(R.id.myCircumstances);
        myStatus = findViewById(R.id.myStatus);
        myLocation = findViewById(R.id.myLocation);
        myData = findViewById(R.id.myData);
        whatsapp = findViewById(R.id.whatsapp);
        call = findViewById(R.id.call);

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        sex = getIntent().getStringExtra("sex");
        circumstances = getIntent().getStringExtra("circumstances");
        city = getIntent().getStringExtra("city");
        status = getIntent().getStringExtra("status");
        age = getIntent().getIntExtra("age", 0);
        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);
        day = getIntent().getIntExtra("day", 0);
        imageUrl = getIntent().getStringExtra("imageUrl");
        user = getIntent().getStringExtra("user");
        phone = getIntent().getIntExtra("phone", 0);

        myTitle.setText(title);
//        myAge.setText(age.toString());
//        mySex.setText(sex);
//        myCircumstances.setText(circumstances);
//        myStatus.setText(status);
//        myLocation.setText(city);

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

//        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//        Utility.showToast(HumanPageView.this, String.valueOf(currentUser == user));
//        Utility.showToast(HumanPageView.this, currentUser);
//        Utility.showToast(HumanPageView.this, user);

//        if (FirebaseAuth.getInstance().getCurrentUser() != null){
//            if (currentUser == user){
//                editImageView.setVisibility(View.VISIBLE);
//                deleteImageView.setVisibility(View.VISIBLE);
//
//                editImageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//                deleteImageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Query query = peopleDatabase.orderByChild("id").equalTo(id);
//
//                        query.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                                    if(dataSnapshot.exists()){
//                                        dataSnapshot.getRef().removeValue();
//                                        Utility.showToast(HumanPageView.this,
//                                                "Заявление успешно удалено");
//                                    } else {
//                                        Utility.showToast(HumanPageView.this,
//                                                "Заявление не найдено");
//                                    }
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
//                    }
//                });
//            }
//        }

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