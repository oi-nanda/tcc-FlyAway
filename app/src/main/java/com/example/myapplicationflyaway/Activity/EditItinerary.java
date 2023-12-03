package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.databinding.ActivityEditItineraryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EditItinerary extends AppCompatActivity {

    ImageView btn_back_itinerary;
    EditText numberOfTravelers, Description, inicialDate, finalDate;
    String oldnumberOfTravelers, oldDescription;
    Button save;
    private DatabaseReference dbReference;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    String id, userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_itinerary_popup);

        progressDialog = new ProgressDialog(this);

        id = getIntent().getExtras().getString("ItineraryId");
        userId = getIntent().getExtras().getString("UserId");

        numberOfTravelers = findViewById(R.id.edit_number_of_travelers_edit);
        Description = findViewById(R.id.edit_description_edit);
        save = findViewById(R.id.btn_edit_info_itinerary);
        btn_back_itinerary = findViewById(R.id.btn_go_back_fromedit);

        auth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Itineraries");

        btn_back_itinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditItinerary.this, ItineraryPageActivity.class);
                i.putExtra("ItineraryId", id);
                i.putExtra("UserId", userId);
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterItineraryInformations();
            }
        });

    }

    private void alterItineraryInformations() {

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("Itineraries");
        dbReference.child(auth.getCurrentUser().getUid()).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> ItineraryMap = new HashMap<>();

                if(!Description.getText().toString().isEmpty()){
                    ItineraryMap.put("Description",Description.getText().toString());
                }
                if(!numberOfTravelers.getText().toString().isEmpty()){
                    ItineraryMap.put("numberOfTravelers",numberOfTravelers.getText().toString());
                }

                dbReference.child(auth.getCurrentUser().getUid()).child(id).updateChildren(ItineraryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        numberOfTravelers.setText("");
                        Description.setText("");
                    }

                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditItinerary.this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(EditItinerary.this, ItineraryPageActivity.class);
                        i.putExtra("ItineraryId", id);
                        i.putExtra("UserId", userId);
                        startActivity(i);
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(EditItinerary.this, "Não foi possível alterar os dados", Toast.LENGTH_SHORT).show();
            }
        });

    }
}