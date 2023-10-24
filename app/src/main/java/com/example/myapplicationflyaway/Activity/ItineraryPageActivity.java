package com.example.myapplicationflyaway.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.myapplicationflyaway.Adapter.ItineraryCardAdapter;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.Model.Itinerary;

public class ItineraryPageActivity extends AppCompatActivity {

    private ListView list;
    private ItineraryCardAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_page);

        adapter = new ItineraryCardAdapter(getApplicationContext(),android.R.layout.simple_list_item_1);

        list = findViewById(R.id.trip_list);
        list.setAdapter(adapter);


    }
}