package com.example.myapplicationflyaway.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationflyaway.Activity.CreateItineraryActivity;
import com.example.myapplicationflyaway.Activity.ItineraryPageActivity;
import com.example.myapplicationflyaway.Activity.MyAdapter;
import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.example.myapplicationflyaway.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

public class MyItineraryAdapter extends RecyclerView.Adapter<MyItineraryAdapter.ViewHolder> {

    Context context;
    private FirebaseAuth mAuth;
    ArrayList<Itinerary> itineraryArrayList;
    DatabaseReference reference;

    public MyItineraryAdapter(Context context, ArrayList<Itinerary> itineraryArrayList) {
        this.context = context;
        this.itineraryArrayList = itineraryArrayList;
    }


    @NonNull
    @Override
    public MyItineraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itinerary_cards,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Itinerary itinerary = itineraryArrayList.get(position);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("username").getValue().toString();
                holder.username.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.placename.setText(itinerary.getPlaceName());
        holder.data.setText(itinerary.getInicialDate());
        holder.itinerary_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItineraryPageActivity.class);
                intent.putExtra("ItineraryId", itinerary.getId());
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itineraryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView placename, data, username;
        ConstraintLayout itinerary_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placename = itemView.findViewById(R.id.itinerary_name);
            data = itemView.findViewById(R.id.data);
            username = itemView.findViewById(R.id.username_txt);
            itinerary_card = itemView.findViewById(R.id.itinerary_card);
        }
    }

}
