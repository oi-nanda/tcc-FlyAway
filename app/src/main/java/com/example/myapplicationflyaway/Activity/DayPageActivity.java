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
import android.widget.Toast;

import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.Model.Place;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    String itineraryId, dayname;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private ImageButton btn_edit;
    MyAdapterPlace myAdapterPlace;
    private RecyclerView recyclerView;
    private ArrayList<Place> placelist;
    private ImageView buttonCreatePlace,back_popup;
    Button button_edit_info_itinerary_popup;
    ImageButton btn_back_itineraryfromDay;
    private TextView data,valordodia,nomedodia,description,edit_description_popup;


    @Override
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

            reference = FirebaseDatabase.getInstance().getReference("Itineraries").child(mAuth.getCurrentUser().getUid()).child(itineraryId)
                    .child("Days").child(dayname);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    data.setText(snapshot.child("date").getValue().toString());

                    if (snapshot.child("img").getValue()==null){}
                    else{
                        String image = snapshot.child("img").getValue().toString();
                 //       Picasso.get().load(image).into(itinerary_pic);
                    }
                    if(snapshot.hasChild("Places")){
                        reference.child("Places").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Place place = dataSnapshot.getValue(Place.class);
                                    placelist.add(place);
                                }
                               myAdapterPlace.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                       myAdapterPlace = new MyAdapterPlace(DayPageActivity.this, placelist);
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



    }

    public void onButtonShowPopupWindowClick(View view){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_edit_day_popup, null);
        back_popup = popupView.findViewById(R.id.imageView14);
        edit_description_popup = popupView.findViewById(R.id.edit_description);
        button_edit_info_itinerary_popup = popupView.findViewById(R.id.button_edit_info_itinerary);


        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
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