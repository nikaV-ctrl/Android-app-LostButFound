package com.example.lostbutfound.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostbutfound.itemPageView.HumanPageView;
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<People> list;

    public MyAdapter(Context context, ArrayList<People> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        People people = list.get(position);

        holder.myTitle.setText(people.title);
        holder.myStatus.setText(people.status);
        if (people.month == 10 || people.month == 11 || people.month == 12){
            holder.myData.setText(people.day + "." + people.month + "." + people.year);
        } else {
            holder.myData.setText(people.day + ".0" + people.month + "." + people.year);
        }
        Picasso.get()
                .load(people.getImageUrl())
                .fit()
                .centerInside()
                .into(holder.myImage);

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, HumanPageView.class);
            intent.putExtra("title", people.getTitle());
            intent.putExtra("fio", people.getFio());
            intent.putExtra("imageUrl", people.getImageUrl());
            intent.putExtra("age", people.getAge());
            intent.putExtra("sex", people.getSex());
            intent.putExtra("circumstances", people.getCircumstances());
            intent.putExtra("signs", people.getSigns());
            intent.putExtra("clothes", people.getClothes());
            intent.putExtra("lastLocation", people.getLastLocation());
            intent.putExtra("location", people.getLocation());
            intent.putExtra("status", people.getStatus());
            intent.putExtra("year", people.getYear());
            intent.putExtra("month", people.getMonth());
            intent.putExtra("day", people.getDay());
            intent.putExtra("phone", people.getPhone());
            intent.putExtra("id", people.getId());
            intent.putExtra("user", people.getUser());
            intent.putExtra("currentTime", people.getCurrentTime());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView myTitle, myStatus, myData;
        public ImageView myImage;

        public MyViewHolder(@NonNull View itemView ) {
            super(itemView);

            myTitle = itemView.findViewById(R.id.myTitle);
            myStatus = itemView.findViewById(R.id.myStatus);
            myImage = itemView.findViewById(R.id.myImage);
            myData = itemView.findViewById(R.id.myData);
        }
    }
}
