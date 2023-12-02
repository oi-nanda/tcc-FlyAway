package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Fragments.HomeFragment;
import com.example.myapplicationflyaway.MainActivity;
import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.databinding.ActivityCreateItineraryBinding;
import com.example.myapplicationflyaway.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class CreateItineraryActivity extends AppCompatActivity {

    ActivityCreateItineraryBinding binding;
    String placeName, inicialDate, finalDate, numberOfTravelers;
    Button add;
    final Calendar currentDate = Calendar.getInstance();
    final Calendar d1 = Calendar.getInstance();
    final Calendar d2 = Calendar.getInstance();
    Day day[];
    ProgressDialog progressDialog;
    FirebaseDatabase db;
    DatabaseReference reference;

    FirebaseUser user;

    String uid;

    String id;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    EditText data1, data2;
    TextView npessoas;
    SearchView local;
    Button start, button2;
    ImageButton btn_back_home;
    final Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_create_itinerary);
        binding = ActivityCreateItineraryBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        binding.buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchView simpleSearchView = (SearchView) findViewById(R.id.searchview);
                CharSequence query = simpleSearchView.getQuery();
                placeName = String.valueOf(query);
                inicialDate = binding.data1.getText().toString();
                finalDate = binding.data2.getText().toString();
                numberOfTravelers = binding.npessoas.getText().toString();

                progressDialog.setMessage("Criando roteiro do usuário");
                progressDialog.setTitle("Roteiro");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if (!placeName.isEmpty() && !inicialDate.isEmpty() && !finalDate.isEmpty() && !numberOfTravelers.isEmpty()) {


                    if ((d2.after(d1) || d1.equals(d2)) && (d1.after(currentDate) || d1.compareTo(currentDate) == 0)){
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        uid = user.getUid().toString();
                        id = UUID.randomUUID().toString();

                    Itinerary itinerary = new Itinerary(id,placeName,inicialDate, finalDate, numberOfTravelers, null, null);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Itineraries");

                    reference.child(uid).child(id).setValue(itinerary).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            binding.searchview.setQuery("", false);
                            binding.data1.setText("");
                            binding.data2.setText("");
                            binding.npessoas.setText("");
                            Toast.makeText(CreateItineraryActivity.this, "Roteiro criado com sucesso!",Toast.LENGTH_SHORT).show();
                            sendUserToItineraryPage();
                        }
                    });
                    }
                    else {
                      if (d1.after(d2)){
                          progressDialog.dismiss();
                          binding.data2.setError("Data final inválida");
                          Toast.makeText(CreateItineraryActivity.this, "A data final deve vir após a data inicial", Toast.LENGTH_SHORT).show();
                      }
                      if (currentDate.after(d1)) {
                          progressDialog.dismiss();
                          binding.data1.setError("Data incial inválida");
                          Toast.makeText(CreateItineraryActivity.this, "A data inicial não pode anteceder o dia de hoje", Toast.LENGTH_SHORT).show();
                      }
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(CreateItineraryActivity.this, "Erro ao criar o roteiro", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateItineraryActivity.this, TestingMapsActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_back_home = findViewById(R.id.btn_back_home);
        local = findViewById(R.id.searchview);
        data1=(EditText) findViewById(R.id.data1);
        data2=(EditText) findViewById(R.id.data2);

        btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });


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
                data1.setText(data.format(dataCalendario));
                data1.setTextColor(androidx.appcompat.R.style.Base_TextAppearance_AppCompat_Body2);
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
                data2.setText(data.format(dataCalendario));
                data2.setTextColor(androidx.appcompat.R.style.Base_TextAppearance_AppCompat_Body2);
            }
        };

        data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();
                int ano = calendario.get(calendario.YEAR);
                int mes = calendario.get(calendario.MONTH);
                int dia = calendario.get(calendario.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(CreateItineraryActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, datai,ano,mes,dia);
                d.show();
            }
        });

        data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();
                int ano = calendario.get(calendario.YEAR);
                int mes = calendario.get(calendario.MONTH);
                int dia = calendario.get(calendario.DAY_OF_MONTH);

                DatePickerDialog d = new DatePickerDialog(CreateItineraryActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, dataf,ano,mes,dia);
                d.show();
            }
        });
    }

    private void sendUserToItineraryPage() {
        Intent intent = new Intent(CreateItineraryActivity.this, ItineraryPageActivity.class);
        intent.putExtra("ItineraryId", id);
        intent.putExtra("UserId", mAuth.getCurrentUser().getUid());
        startActivity(intent);
        finish();
    }
}