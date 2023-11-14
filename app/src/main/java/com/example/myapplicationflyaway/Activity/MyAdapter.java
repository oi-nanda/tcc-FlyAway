package com.example.myapplicationflyaway.Activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Context context;
    ArrayList<Day> dayArrayList;

    public MyAdapter(Context context, ArrayList<Day> dayArrayList) {
        this.context = context;
        this.dayArrayList = dayArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.roteiro_card_item_layout,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

        Day day = dayArrayList.get(position);
        holder.day.setText(day.getDayname());

    }

    @Override
    public int getItemCount() {
        return dayArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView day;
        CardView imgDay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.title_card);
            imgDay = itemView.findViewById(R.id.image_card);

        }
    }
}
