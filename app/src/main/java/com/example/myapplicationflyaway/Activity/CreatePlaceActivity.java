package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.Model.Place;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.databinding.ActivityCreateDayBinding;
import com.example.myapplicationflyaway.databinding.ActivityCreatePlaceBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class CreatePlaceActivity extends AppCompatActivity {
    Button add;
    String itineraryId,dayid;
    ProgressDialog progressDialog;
    ActivityCreatePlaceBinding binding;
    private DatabaseReference databaseReference, dbRefPlace;
    private FirebaseAuth mAuth;
    String placeName, desc, cost, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);
        progressDialog = new ProgressDialog(this);

        binding = ActivityCreatePlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        itineraryId = getIntent().getExtras().getString("ItineraryId");
        dayid = getIntent().getExtras().getString("DayName");
        add = findViewById(R.id.buttonCreatePlace);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlace(v);
//                Log.d("DayName", dayid+"abcde");
//                Log.d("ItineraryId", itineraryId);
                sendUsertoPlacePage();
            }
        });
    }


    private void createPlace(View v) {
        progressDialog.setMessage("Criando local");
        progressDialog.setTitle("local do roteiro");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        SearchView simpleSearchView = (SearchView) findViewById(R.id.searchview);
        CharSequence query = simpleSearchView.getQuery();
        placeName = String.valueOf(query);
        desc = binding.description.getText().toString();
        cost = binding.custo.getText().toString();

        Double newCost = Double.parseDouble(cost);

        databaseReference = FirebaseDatabase.getInstance().getReference("Itineraries");

        mAuth = FirebaseAuth.getInstance();
        id = UUID.randomUUID().toString();

        databaseReference.child(mAuth.getCurrentUser().getUid()).child(itineraryId).child("Days").child(dayid).child("Places").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Place place = new Place(placeName,desc,newCost,id, null, dayid,itineraryId);
                dbRefPlace = snapshot.getRef();
                dbRefPlace.setValue(place).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendUsertoPlacePage() {
        Intent intent = new Intent(CreatePlaceActivity.this, ItineraryPageActivity.class);
        intent.putExtra("ItineraryId", itineraryId);
        intent.putExtra("DayName",dayid);
        intent.putExtra("IdPlace",id);
        startActivity(intent);
        finish();
    }



}

