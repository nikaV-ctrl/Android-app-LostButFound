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

import com.example.lostbutfound.Model.Animals;
import com.example.lostbutfound.R;
import com.example.lostbutfound.itemPageView.AnimalsPageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnimalsAdapter extends RecyclerView.Adapter<AnimalsAdapter.AnimalsViewHolder> {

    private Context context;
    private ArrayList<Animals> list;

    public AnimalsAdapter(Context context, ArrayList<Animals> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AnimalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new AnimalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalsViewHolder holder, int position) {
        Animals animal = list.get(position);
        holder.myTitle.setText(animal.title);
        holder.myStatus.setText(animal.status);
        if (animal.month == 10 || animal.month == 11 || animal.month == 12){
            holder.myData.setText(animal.day + "." + animal.month + "." + animal.year);
        } else {
            holder.myData.setText(animal.day + ".0" + animal.month + "." + animal.year);
        }
        Picasso.get()
                .load(animal.getImageUrl())
                .fit()
                .centerInside()
                .into(holder.myImage);

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, AnimalsPageView.class);
            intent.putExtra("id", animal.getId());
            intent.putExtra("title", animal.getTitle());
            intent.putExtra("imageUrl", animal.getImageUrl());
            intent.putExtra("city", animal.getCity());
            intent.putExtra("circumstances", animal.getCircumstances());
            intent.putExtra("description", animal.getDescription());
            intent.putExtra("breed", animal.getBreed());
            intent.putExtra("lastLocation", animal.getLastLocation());
            intent.putExtra("status", animal.getStatus());
            intent.putExtra("reward", animal.getReward());
            intent.putExtra("user", animal.getUser());
            intent.putExtra("phone", animal.getPhone());
            intent.putExtra("year", animal.getYear());
            intent.putExtra("month", animal.getMonth());
            intent.putExtra("day", animal.getDay());
            intent.putExtra("currentTime", animal.getCurrentTime());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AnimalsViewHolder extends RecyclerView.ViewHolder {

        public TextView myTitle, myStatus, myData;
        public ImageView myImage;

        public AnimalsViewHolder(@NonNull View itemView ) {
            super(itemView);

            myTitle = itemView.findViewById(R.id.myTitle);
            myStatus = itemView.findViewById(R.id.myStatus);
            myImage = itemView.findViewById(R.id.myImage);
            myData = itemView.findViewById(R.id.myData);
        }
    }
}

