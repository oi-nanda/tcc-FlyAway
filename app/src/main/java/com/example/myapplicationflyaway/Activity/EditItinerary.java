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
    ActivityEditItineraryBinding binding;
    EditText numberOfTravelers, Description, inicialDate, finalDate;
    String oldinicialDate, oldFinalDate, oldnumberOfTravelers, oldDescription;
    Button save;
    LinearLayout btn_conf;
    private DatabaseReference dbReference;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    final Calendar currentDate = Calendar.getInstance();
    final Calendar calendario = Calendar.getInstance();
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_itinerary_popup);
        binding = ActivityEditItineraryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);

        id = getIntent().getExtras().getString("ItineraryId");
        oldnumberOfTravelers = getIntent().getExtras().getString("numberOfTravelers");
        oldDescription = getIntent().getExtras().getString("description");

        numberOfTravelers = findViewById(R.id.edit_number_of_travelers);
        Description = findViewById(R.id.edit_description);
        save = findViewById(R.id.button_edit_info_itinerary);
        Description.setText(oldDescription);


    //    btn_back_itinerary = findViewById(R.id.btn_back_itinerary);

        auth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Itineraries");

        btn_back_itinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditItinerary.this, ItineraryPageActivity.class);
                i.putExtra("ItineraryId", id);
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterItineraryInformations();
                    Intent i = new Intent(EditItinerary.this, ItineraryPageActivity.class);
                    i.putExtra("ItineraryId", id);
                    startActivity(i);
            }
        });

    }

    private void alterItineraryInformations() {
        dbReference.child(auth.getCurrentUser().getUid()).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> userMap = new HashMap<>();

                progressDialog.setMessage("Editando roteiro do usuário");
                progressDialog.setTitle("Roteiro");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if (!numberOfTravelers.getText().toString().isEmpty()) {
                    userMap.put("numberOfTravelers", numberOfTravelers.getText().toString());
                } else {
                    userMap.put("numberOfTravelers", oldnumberOfTravelers);
                }
                if (!Description.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    userMap.put("Description", Description.getText().toString());
                } else {
                    progressDialog.dismiss();
                    userMap.put("Description", oldDescription);
                }

                dbReference.child(auth.getCurrentUser().getUid()).child(id).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Description.setText("");
                        finalDate.setText("");
                        inicialDate.setText("");
                        numberOfTravelers.setText("");
                    }

                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}