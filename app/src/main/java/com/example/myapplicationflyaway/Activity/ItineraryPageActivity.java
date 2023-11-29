package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Adapter.ItineraryCardAdapter;
import com.example.myapplicationflyaway.Adapter.MyItineraryAdapter;
import com.example.myapplicationflyaway.Fragments.MyitinerariesFragment;
import com.example.myapplicationflyaway.Fragments.ProfileFragment;
import com.example.myapplicationflyaway.ItineraryAdapter;
import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.google.firebase.auth.FirebaseAuth;
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
    ImageButton btn_back, btn_edit;
    ImageView btn_img;
    private RecyclerView recyclerView;
    private ArrayList<Day> daylist;
    MyAdapter myAdapter;
    String itineraryId, itineraryName;
    private FirebaseAuth mAuth;
    private ImageView itinerary_pic;
    DatabaseReference reference;

    LinearLayout btn_clima, btn_galeria;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_page);

        mAuth = FirebaseAuth.getInstance();

        add = findViewById(R.id.Adicionar);
        txt_people = findViewById(R.id.pessoas);
        txt_inicial_date = findViewById(R.id.datai);
        txt_final_date = findViewById(R.id.dataf);
        txt_description = findViewById(R.id.description);
        itinerary_pic = findViewById(R.id.cover_1);
        btn_clima = findViewById(R.id.btn_clima);
        btn_back = findViewById(R.id.btn_back_myItineraries);
        btn_galeria = findViewById(R.id.btn_galeria);
        btn_edit = findViewById(R.id.btn_edit);
        btn_img = findViewById(R.id.btn_img);

        recyclerView = findViewById(R.id.trip_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        daylist = new ArrayList<Day>();


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            itineraryId = getIntent().getExtras().getString("ItineraryId");

            reference = FirebaseDatabase.getInstance().getReference("Itineraries").child(mAuth.getCurrentUser().getUid()).child(itineraryId);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    txt_inicial_date.setText(snapshot.child("inicialDate").getValue().toString());
                    txt_final_date.setText(snapshot.child("finalDate").getValue().toString());

                    if (snapshot.child("Description").getValue()==null){
                    }
                    else{
                        txt_description.setText((snapshot.child("Description").getValue().toString()));
                    }

                    if (snapshot.child("img").getValue()==null){}
                    else{
                        String image = snapshot.child("img").getValue().toString();
                        Picasso.get().load(image).into(itinerary_pic);
                    }
                    if(snapshot.hasChild("Days")){
                        reference.child("Days").addValueEventListener(new ValueEventListener() {
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
                        myAdapter = new MyAdapter(ItineraryPageActivity.this, daylist);
                        recyclerView.setAdapter(myAdapter);
                    }

                    txt_people.setText((snapshot.child("numberOfTravelers").getValue().toString()));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, MyitinerariesFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ItineraryPageActivity.this, EditItinerary.class );
                i.putExtra("ItineraryId", itineraryId);
                i.putExtra("description", txt_description.getText().toString());
                i.putExtra("numberOfTravelers", txt_people.getText().toString());
                startActivity(i);
            }
        });


        btn_clima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this, ClimaActivity.class);
                i.putExtra("itineraryId", itineraryId);
                startActivity(i);
               // finish();
            }
        });

        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ItineraryPageActivity.this, downloadImgItineraryPage.class);
                i.putExtra("ItineraryId", itineraryId);
                startActivity(i);
            }
        });

        btn_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this, GaleryPhotosActivity.class);
                i.putExtra("itineraryId", itineraryId);
                startActivity(i);
               // finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this, CreateDayActivity.class);
                i.putExtra("ItineraryIdAtual", itineraryId);
                startActivity(i);
                finish();
            }
        });
    }

}