package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplicationflyaway.Adapter.ItineraryCardAdapter;
import com.example.myapplicationflyaway.ItineraryAdapter;
import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;

public class ItineraryPageActivity extends AppCompatActivity {

    private TextView txt_people, txt_inicial_date, txt_final_date, txt_description;
    private Button add;
    private RecyclerView recyclerView;
    private ArrayList<Day> daylist;
    MyAdapter myAdapter;
    DatabaseReference databaseReference;
    private ItineraryAdapter adapter;
    private ImageView itinerary_pic;

    DatabaseReference reference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_page);

        add = findViewById(R.id.Adicionar);
        txt_people = findViewById(R.id.pessoas);
        txt_inicial_date = findViewById(R.id.datai);
        txt_final_date = findViewById(R.id.dataf);
        txt_description = findViewById(R.id.description);
        itinerary_pic = findViewById(R.id.cover_1);


        recyclerView = findViewById(R.id.trip_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("Itineraries").child("abcs").child("Days");

        daylist = new ArrayList<Day>();
        myAdapter = new MyAdapter(ItineraryPageActivity.this,daylist);
        recyclerView.setAdapter(myAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Day day = dataSnapshot.getValue(Day.class);
                    daylist.add(day);
                }
                myAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        reference = FirebaseDatabase.getInstance().getReference().child("Itineraries").child("abcs");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                txt_inicial_date.setText(snapshot.child("inicialDate").getValue().toString());
                txt_final_date.setText(snapshot.child("finalDate").getValue().toString());

                if (snapshot.child("description").getValue()==null){
                    txt_description.setText("Sem descrição");
                }
                else{
                    txt_people.setText((snapshot.child("description").getValue().toString()));
                }

                if (snapshot.child("img").getValue()==null){}
                else{
                    String image = snapshot.child("img").getValue().toString();
                    Picasso.get().load(image).into(itinerary_pic);
                }

                txt_people.setText((snapshot.child("numberOfTravelers").getValue().toString())+ " pessoas");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this, CreateDayActivity.class);
                startActivity(i);
            }
        });
    }

}