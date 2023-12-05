package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.myapplicationflyaway.Model.Place;
import com.example.myapplicationflyaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class DayPageActivity extends AppCompatActivity {
    String itineraryId, dayname, userId;
    DatabaseReference reference;
    Button button3;
    private FirebaseAuth mAuth;
    MyAdapterPlace myAdapterPlace;
    private RecyclerView recyclerView;
    private ArrayList<Place> placelist;
    private ImageView buttonCreatePlace,back_popup;
    Button button_edit_info_itinerary_popup;
    ImageButton btn_back_itineraryfromDay;
    private TextView data,valordodia,nomedodia,description,edit_description_popup;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_page);

        data = findViewById(R.id.data);
        valordodia = findViewById(R.id.valordodia);
        nomedodia = findViewById(R.id.nomedodia);
        description = findViewById(R.id.description);
        placelist = new ArrayList<Place>();
        buttonCreatePlace = findViewById(R.id.buttonCreatePlace);
        btn_back_itineraryfromDay = findViewById(R.id.btn_back_itineraryfromDay);
        button3 = findViewById(R.id.button3);

        recyclerView = findViewById(R.id.places_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();



        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setTitle("");



        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            itineraryId = getIntent().getExtras().getString("ItineraryId");
            dayname = getIntent().getExtras().getString("DayName");
            userId = getIntent().getExtras().getString("UserId");

            reference = FirebaseDatabase.getInstance().getReference("Itineraries").child(mAuth.getCurrentUser().getUid()).child(itineraryId)
                    .child("Days").child(dayname);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    data.setText(snapshot.child("date").getValue().toString());

                    if (snapshot.child("img").getValue()==null){}
                    else{
                        String image = snapshot.child("img").getValue().toString();
                    }
                    if(snapshot.hasChild("Places")){
                        reference.child("Places").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                     Place place = dataSnapshot.getValue(Place.class);
                                    placelist.add(place);
                                }
                                Log.d("lugar", "erro3");
                               myAdapterPlace.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Log.d("lugar", "erro4");
                      myAdapterPlace = new MyAdapterPlace(DayPageActivity.this, placelist);
                        Log.d("lugar", "erro5");
                      recyclerView.setAdapter(myAdapterPlace);

                    }
                    nomedodia.setText((snapshot.child("dayname").getValue().toString()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        buttonCreatePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DayPageActivity.this, TestingAutoComplete.class);
                i.putExtra("ItineraryId",itineraryId);
                i.putExtra("DayName",dayname);
                startActivity(i);
            }
        });

        btn_back_itineraryfromDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DayPageActivity.this, ItineraryPageActivity.class);
                i.putExtra("ItineraryId",itineraryId);
                i.putExtra("UserId",dayname);
                startActivity(i);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DayPageActivity.this, LocaisPorProximidade.class);
                i.putExtra("itineraryId", itineraryId);
                i.putExtra("UserId", userId);
                startActivity(i);
                finish();
            }
        });



    }

    public void onButtonShowPopupWindowClick(View view){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_edit_day_popup, null);
        back_popup = popupView.findViewById(R.id.imageView14);
        edit_description_popup = popupView.findViewById(R.id.edit_description);
        button_edit_info_itinerary_popup = popupView.findViewById(R.id.button_edit_info_itinerary);


        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        back_popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

    }

}