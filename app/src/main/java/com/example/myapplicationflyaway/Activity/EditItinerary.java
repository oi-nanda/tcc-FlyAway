package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplicationflyaway.Fragments.ProfileFragment;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.databinding.ActivityCreateItineraryBinding;
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
    final Calendar d1 = Calendar.getInstance();
    final Calendar d2 = Calendar.getInstance();
    final Calendar calendario = Calendar.getInstance();
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_itinerary);
        binding = ActivityEditItineraryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);

        id = getIntent().getExtras().getString("ItineraryIdAtual");
        oldinicialDate = getIntent().getExtras().getString("inicialDate");
        oldFinalDate = getIntent().getExtras().getString("finalDate");
        oldnumberOfTravelers = getIntent().getExtras().getString("numberOfTravelers");
        oldDescription = getIntent().getExtras().getString("description");

        numberOfTravelers = findViewById(R.id.edit_number_of_travelers);
        Description = findViewById(R.id.edit_description);
        inicialDate = findViewById(R.id.data1edit);
        finalDate = findViewById(R.id.data2edit);
        save = findViewById(R.id.button_edit_info_itinerary);
        inicialDate.setText(oldinicialDate);
        finalDate.setText(oldFinalDate);
        Description.setText(oldDescription);


        btn_back_itinerary = findViewById(R.id.btn_back_itinerary);

        auth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Itineraries");

        DatePickerDialog.OnDateSetListener datai = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                calendario.set(Calendar.YEAR, i);
                calendario.set(Calendar.MONTH,i2);
                calendario.set(Calendar.DAY_OF_MONTH,i3);
                d1.set(Calendar.YEAR, i);
                d1.set(Calendar.MONTH,i2);
                d1.set(Calendar.DAY_OF_MONTH,i3);
                Date dataCalendario = calendario.getTime();
                SimpleDateFormat data = new SimpleDateFormat("dd/MM/yy");
                inicialDate.setText(data.format(dataCalendario));
                inicialDate.setTextColor(androidx.appcompat.R.style.Base_TextAppearance_AppCompat_Body2);
            }
        };

        DatePickerDialog.OnDateSetListener dataf = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                calendario.set(Calendar.YEAR, i);
                calendario.set(Calendar.MONTH,i2);
                calendario.set(Calendar.DAY_OF_MONTH,i3);
                Date dataCalendario = calendario.getTime();
                d2.set(Calendar.YEAR, i);
                d2.set(Calendar.MONTH,i2);
                d2.set(Calendar.DAY_OF_MONTH,i3);
                SimpleDateFormat data = new SimpleDateFormat("dd/MM/yy");
                finalDate.setText(data.format(dataCalendario));
                finalDate.setTextColor(androidx.appcompat.R.style.Base_TextAppearance_AppCompat_Body2);
            }
        };

        inicialDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();
                int ano = calendario.get(calendario.YEAR);
                int mes = calendario.get(calendario.MONTH);
                int dia = calendario.get(calendario.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(EditItinerary.this,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, datai,ano,mes,dia);
                d.show();
            }
        });

        finalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();
                int ano = calendario.get(calendario.YEAR);
                int mes = calendario.get(calendario.MONTH);
                int dia = calendario.get(calendario.DAY_OF_MONTH);

                DatePickerDialog d = new DatePickerDialog(EditItinerary.this,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, dataf,ano,mes,dia);
                d.show();
            }
        });



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
                if ((d2.after(d1) || d1.equals(d2)) && (d1.after(currentDate) || d1.compareTo(currentDate) == 0)) {
                    Intent i = new Intent(EditItinerary.this, ItineraryPageActivity.class);
                    i.putExtra("ItineraryId", id);
                    startActivity(i);
                }
                else{
                    if (d1.after(d2)) {
                        binding.data2edit.setError("Data final inválida");
                        Toast.makeText(EditItinerary.this, "A data final deve vir após a data inicial", Toast.LENGTH_SHORT).show();
                    }
                    if (currentDate.after(d1)) {
                        binding.data1edit.setError("Data incial inválida");
                        Toast.makeText(EditItinerary.this, "A data inicial não pode anteceder o dia de hoje", Toast.LENGTH_SHORT).show();

                    }
                }
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
                if (!inicialDate.getText().toString().isEmpty()) {
                    if ((d2.after(d1) || d1.equals(d2)) && (d1.after(currentDate) || d1.compareTo(currentDate) == 0)) {
                        userMap.put("inicialDate", inicialDate.getText().toString());
                    }
                } else {
                    userMap.put("inicialDate", oldinicialDate);
                }
                if (!finalDate.getText().toString().isEmpty()) {
                    if ((d2.after(d1) || d1.equals(d2)) && (d1.after(currentDate) || d1.compareTo(currentDate) == 0)) {
                        userMap.put("finalDate", finalDate.getText().toString());
                    }
                } else {
                    userMap.put("finalDate", oldFinalDate);
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