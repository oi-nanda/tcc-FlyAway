package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.example.myapplicationflyaway.Model.Place;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.databinding.ActivityCreateDayBinding;
import com.example.myapplicationflyaway.databinding.ActivityCreateItineraryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CreateDayActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ActivityCreateDayBinding binding;
    String desc, cost;
    String placeName;
    FirebaseUser user;

    FirebaseDatabase db;
    DatabaseReference reference;

    String uid;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_day);
        binding = ActivityCreateDayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);


        binding.buttonCreateDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SearchView simpleSearchView = (SearchView) findViewById(R.id.searchview);
                CharSequence query = simpleSearchView.getQuery();
                placeName = String.valueOf(query);
                desc = binding.description.getText().toString();
                cost = binding.custo.getText().toString();


                progressDialog.setMessage("Criando dia");
                progressDialog.setTitle("Dia do roteiro");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String dayname = "day "+1;

                Day day = new Day("day 2",desc,null);
                Place place = new Place(placeName,null,Double.valueOf(cost));
                db = FirebaseDatabase.getInstance();
                reference = db.getReference("Itineraries").child("abcs");

                reference.child("Days").child("day3").setValue(day).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.description.setText("");
                        Toast.makeText(CreateDayActivity.this, "Dia criado com sucesso!",Toast.LENGTH_SHORT).show();
                    }
                });

                reference.child("Days").child("day3").child("places").setValue(place).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.searchview.setQuery("", false);
                        binding.custo.setText("");
                    }
                });

                Intent i = new Intent(CreateDayActivity.this, ItineraryPageActivity.class);
                startActivity(i);

            }
        });

    }
}