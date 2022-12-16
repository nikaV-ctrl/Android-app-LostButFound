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

import com.example.lostbutfound.Model.Relatives;
import com.example.lostbutfound.R;
import com.example.lostbutfound.itemPageView.RelativePageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RelativesAdapter extends RecyclerView.Adapter<RelativesAdapter.RelativesViewHolder> {
    private Context context;
    private ArrayList<Relatives> list;

    public RelativesAdapter(Context context, ArrayList<Relatives> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RelativesAdapter.RelativesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new RelativesAdapter.RelativesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelativesAdapter.RelativesViewHolder holder, int position) {
        Relatives relatives = list.get(position);
        holder.myTitle.setText(relatives.title);
        holder.myStatus.setText(relatives.status);
        if (relatives.month == 10 || relatives.month == 11 || relatives.month == 12){
            holder.myData.setText(relatives.day + "." + relatives.month + "." + relatives.year);
        } else {
            holder.myData.setText(relatives.day + ".0" + relatives.month + "." + relatives.year);
        }
        Picasso.get()
                .load(relatives.getImageUrl())
                .fit()
                .centerInside()
                .into(holder.myImage);

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, RelativePageView.class);
            intent.putExtra("id", relatives.getId());
            intent.putExtra("title", relatives.getTitle());
            intent.putExtra("sex", relatives.getSex());
            intent.putExtra("circumstances", relatives.getCircumstances());
            intent.putExtra("city", relatives.getCity());
            intent.putExtra("status", relatives.getStatus());
            intent.putExtra("age", relatives.getAge());
            intent.putExtra("year", relatives.getYear());
            intent.putExtra("month", relatives.getMonth());
            intent.putExtra("day", relatives.getDay());
            intent.putExtra("imageUrl", relatives.getImageUrl());
            intent.putExtra("user", relatives.getUser());
            intent.putExtra("phone", relatives.getPhone());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RelativesViewHolder extends RecyclerView.ViewHolder {

        public TextView myTitle, myStatus, myData;
        public ImageView myImage;

        public RelativesViewHolder(@NonNull View itemView ) {
            super(itemView);

            myTitle = itemView.findViewById(R.id.myTitle);
            myStatus = itemView.findViewById(R.id.myStatus);
            myImage = itemView.findViewById(R.id.myImage);
            myData = itemView.findViewById(R.id.myData);
        }
    }
}
