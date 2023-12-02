package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.Model.Place;
import com.example.myapplicationflyaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DayPageActivity extends AppCompatActivity {
    String itineraryId, dayname;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    MyAdapterPlace myAdapterPlace;
    private RecyclerView recyclerView;
    private ArrayList<Place> placelist;
    private ImageView buttonCreatePlace;
    private TextView data,valordodia,nomedodia,description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_page);

        data = findViewById(R.id.data);
        valordodia = findViewById(R.id.valordodia);
        nomedodia = findViewById(R.id.nomedodia);
        description = findViewById(R.id.description);
        placelist = new ArrayList<Place>();
        buttonCreatePlace = findViewById(R.id.buttonCreatePlace);

        recyclerView = findViewById(R.id.places_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            itineraryId = getIntent().getExtras().getString("ItineraryId");
            dayname = getIntent().getExtras().getString("DayName");

            reference = FirebaseDatabase.getInstance().getReference("Itineraries").child(mAuth.getCurrentUser().getUid()).child(itineraryId)
                    .child("Days").child(dayname);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    data.setText(snapshot.child("date").getValue().toString());

                    if (snapshot.child("description").getValue()==null || snapshot.child("description").getValue()==""){
                    }
                    else{
                        description.setText((snapshot.child("description").getValue().toString()));
                    }

                    if (snapshot.child("img").getValue()==null){}
                    else{
//                        String image = snapshot.child("img").getValue().toString();
//                        Picasso.get().load(image).into(itinerary_pic);
                    }
                    if(snapshot.hasChild("Places")){
                        reference.child("Places").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                    Place place = dataSnapshot.getValue(Place.class);
//                                    placelist.add(place);
                                }
                                myAdapterPlace.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        myAdapterPlace = new MyAdapterPlace(DayPageActivity.this, placelist);
                        recyclerView.setAdapter(myAdapterPlace);
                    }

                    nomedodia.setText((snapshot.child("dayname").getValue().toString()));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        buttonCreatePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DayPageActivity.this, CreatePlaceActivity.class);
                i.putExtra("ItineraryId",itineraryId);
                i.putExtra("DayName",dayname);
                startActivity(i); //? não vai
            }
        });

    }
}