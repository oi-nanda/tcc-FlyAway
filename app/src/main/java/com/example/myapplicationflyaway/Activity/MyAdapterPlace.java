package com.example.myapplicationflyaway.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationflyaway.Model.Place;
import com.example.myapplicationflyaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MyAdapterPlace extends RecyclerView.Adapter<MyAdapterPlace.ViewHolder> {


    Context context;
    private FirebaseAuth mAuth;
    ArrayList<Place> placeArrayList;
    DatabaseReference reference;

    public MyAdapterPlace(Context context, ArrayList<Place> placeArrayList) {
        this.context = context;
        this.placeArrayList = placeArrayList;
    }

    @NonNull
    @Override
    public MyAdapterPlace.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dia_card_item_layout,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterPlace.ViewHolder holder, int position) {

        Place place = placeArrayList.get(position);
        holder.place.setText(place.getName());

//        holder.imgPlace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), PlacePageActivity.class);
//                intent.putExtra("PlaceName", place.getName());
//                v.getContext().startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return placeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView place,valor;
        CardView imgPlace;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          //  valor = itemView.findViewById(R.id.valordolugar);
            place = itemView.findViewById(R.id.title_card);
//            imgPlace = itemView.findViewById(R.id.image_card);

        }
    }
}
