package com.example.myapplicationflyaway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ItineraryPageActivity extends AppCompatActivity {

    private ListView list;
    private MeuCardRoteiroAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_page);

        adapter = new MeuCardRoteiroAdapter(getApplicationContext(),android.R.layout.simple_list_item_1);

        list = findViewById(R.id.trip_list);
        list.setAdapter(adapter);

        //   public Roteiro(String titulo, String data, String lugar, Boolean salvo, int imageId) {
        //        this.titulo = titulo;
        //        this.data = data;
        //        this.lugar = lugar;
        //        this.salvo = salvo;
        //        this.imageId = imageId;
        //    }

        Roteiro roteiro = new Roteiro ("titulo","datayeah","japao",false,1234);
        adapter.add(roteiro);

    }
}