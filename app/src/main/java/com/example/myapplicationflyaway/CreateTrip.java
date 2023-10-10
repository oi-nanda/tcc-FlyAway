package com.example.myapplicationflyaway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class CreateTrip extends AppCompatActivity {
    EditText data1, data2;
    TextView npessoas;
    SearchView local;
    Button start;
    final Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_itinerary);

        start = findViewById(R.id.buttonCreate);

        npessoas = findViewById(R.id.npessoas);
        local = findViewById(R.id.searchview);
        data1=(EditText) findViewById(R.id.data1);
        data2=(EditText) findViewById(R.id.data2);

        DatePickerDialog.OnDateSetListener datai = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                calendario.set(Calendar.YEAR, i);
                calendario.set(Calendar.MONTH,i2);
                calendario.set(Calendar.DAY_OF_MONTH,i3);
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
                DatePickerDialog d = new DatePickerDialog(CreateTrip.this,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, datai,ano,mes,dia);
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

                DatePickerDialog d = new DatePickerDialog(CreateTrip.this,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, dataf,ano,mes,dia);
                d.show();
            }
        });



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ItineraryPageActivity.class);
//                i.putExtra("datainicial",data1);
//                i.putExtra("datafinal",data2);
//                i.putExtra("pessoasnaviagem",npessoas.toString());
//                i.putExtra("local",local);
                startActivity(i);
                Toast.makeText(CreateTrip.this, "Roteiro criado!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}