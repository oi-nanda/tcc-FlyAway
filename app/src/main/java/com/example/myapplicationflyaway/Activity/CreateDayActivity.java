package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Fragments.ProfileFragment;
import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.example.myapplicationflyaway.Model.Place;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.databinding.ActivityCreateDayBinding;
import com.example.myapplicationflyaway.databinding.ActivityCreateItineraryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CreateDayActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ActivityCreateDayBinding binding;
    String desc, cost;
    private ArrayList<Day> daylist;
    MyAdapter myAdapter;
    String placeName;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference dbReference;
    private FirebaseAuth auth;
    long a = 0;
    View view;
    DatabaseReference databaseReference, dbRefDay, dbRefPlace;
    FirebaseDatabase db;
    DatabaseReference reference;
    String uid;
    String NDaysNew;
    String dayname;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_day);
        id = getIntent().getExtras().getString("ItineraryIdAtual");
        binding = ActivityCreateDayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);



        binding.buttonCreateDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDay(view);
            }
        });

    }

    public void createDay(View view){

        progressDialog.setMessage("Criando dia");
        progressDialog.setTitle("Dia do roteiro");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        SearchView simpleSearchView = (SearchView) findViewById(R.id.searchview);
        CharSequence query = simpleSearchView.getQuery();
        placeName = String.valueOf(query);
        desc = binding.description.getText().toString();
        cost = binding.custo.getText().toString();

        databaseReference = FirebaseDatabase.getInstance().getReference("Itineraries");

        mAuth = FirebaseAuth.getInstance();



        databaseReference.child(mAuth.getCurrentUser().getUid()).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild("NDays")){
                    String NDays = snapshot.child("NDays").getValue().toString();
                    int a = Integer.valueOf(NDays);
                    a++;
                    NDaysNew = Integer.toString(a);
                }
                else{NDaysNew = "1";}

                dayname = "dia "+NDaysNew;

                dbRefDay =  snapshot.child("Days").getRef();
                String id = UUID.randomUUID().toString();
                Day day = new Day(dayname,desc,id,null,null,null,null);
                dbRefDay.child(dayname).setValue(day).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dbRefPlace = snapshot.child("Days").child(dayname).child("Places").getRef();
                        Place place = new Place(placeName,null,Double.valueOf(cost),null,null,null,null);
                        dbRefPlace.child(placeName).setValue(place).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        databaseReference.child(mAuth.getCurrentUser().getUid()).child(id).child("NDays").setValue(NDaysNew);
                        progressDialog.dismiss();
                        sendUsertoItinerariesPage();
                        Toast.makeText(CreateDayActivity.this, "Dia criado com sucesso", Toast.LENGTH_SHORT).show();

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
         }

public void sendUsertoItinerariesPage () {
    Intent i = new Intent(CreateDayActivity.this, ItineraryPageActivity.class);
    i.putExtra("ItineraryId", id);
    startActivity(i);
    finish();
}

}