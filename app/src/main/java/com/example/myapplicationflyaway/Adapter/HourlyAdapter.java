package com.example.myapplicationflyaway.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationflyaway.Model.Hourly;
import com.example.myapplicationflyaway.R;

import java.util.ArrayList;

public class
HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {

    ArrayList<Hourly> hourlyList;
    Context context;

    public HourlyAdapter(Context context) {
        this.context = context;
    }
    public void setItemList(ArrayList<Hourly> hourlyList) {
        this.hourlyList = hourlyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HourlyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_hourly, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyAdapter.ViewHolder holder, int position) {
        Hourly hourly = hourlyList.get(position);

        holder.timeTxt.setText(hourly.getTime());
        holder.descriptionTxt.setText(hourly.getDescription());
        holder.tempTxt.setText(hourly.getTemp() + "°C" +
                "");
        if(hourly.getPic().equals("chuva")){
            holder.pic.setImageResource(R.drawable.heavy_rain);
        }
        if(hourly.getPic().equals("tempestade")){
            holder.pic.setImageResource(R.drawable.storm);
        }
        if(hourly.getPic().equals("chuvisco")){

            holder.pic.setImageResource(R.drawable.garoa);
        }
        if(hourly.getPic().equals("neve") || hourly.getPic().equals("granizo")){

            holder.pic.setImageResource(R.drawable.freezing_rain);
        }
        if(hourly.getPic().equals("limpo") ){

            holder.pic.setImageResource(R.drawable.clear_sky);
        }
        if(hourly.getPic().equals("nuvens") ){
            holder.pic.setImageResource(R.drawable.sol);
        }
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView descriptionTxt, tempTxt, timeTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeTxt = itemView.findViewById(R.id.time);
            tempTxt = itemView.findViewById(R.id.temp);
            descriptionTxt = itemView.findViewById(R.id.description);
            pic = itemView.findViewById(R.id.pic);
        }
    }


}
