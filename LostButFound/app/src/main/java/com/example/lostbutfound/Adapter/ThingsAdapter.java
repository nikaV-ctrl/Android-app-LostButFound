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

import com.example.lostbutfound.Model.Things;
import com.example.lostbutfound.R;
import com.example.lostbutfound.ThingPage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ThingsAdapter extends RecyclerView.Adapter<ThingsAdapter.ThingsViewHolder> {

    private Context context;
    private ArrayList<Things> list;

    public ThingsAdapter(Context context, ArrayList<Things> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ThingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ThingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThingsViewHolder holder, int position) {
        Things thing = list.get(position);

        holder.myTitle.setText(thing.title);
        holder.myStatus.setText(thing.status);
        if (thing.month == 10 || thing.month == 11 || thing.month == 12){
            holder.myData.setText(thing.day + "." + thing.month + "." + thing.year);
        } else {
            holder.myData.setText(thing.day + ".0" + thing.month + "." + thing.year);
        }
        Picasso.get()
                .load(thing.getImageUrl())
                .fit()
                .centerInside()
                .into(holder.myImage);

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, ThingPage.class);
            intent.putExtra("title", thing.getTitle());
            intent.putExtra("imageUrl", thing.getImageUrl());
            intent.putExtra("description", thing.getDescription());
            intent.putExtra("lastLocation", thing.getLastLocation());
            intent.putExtra("location", thing.getLocation());
            intent.putExtra("status", thing.getStatus());
            intent.putExtra("year", thing.getYear());
            intent.putExtra("month", thing.getMonth());
            intent.putExtra("day", thing.getDay());
            intent.putExtra("phone", thing.getPhone());
            intent.putExtra("id", thing.getId());
            intent.putExtra("user", thing.getUser());
            intent.putExtra("currentTime", thing.getCurrentTime());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ThingsViewHolder extends RecyclerView.ViewHolder {

        public TextView myTitle, myStatus, myData;
        public ImageView myImage;

        public ThingsViewHolder(@NonNull View itemView ) {
            super(itemView);

            myTitle = itemView.findViewById(R.id.myTitle);
            myStatus = itemView.findViewById(R.id.myStatus);
            myImage = itemView.findViewById(R.id.myImage);
            myData = itemView.findViewById(R.id.myData);
        }
    }
}
