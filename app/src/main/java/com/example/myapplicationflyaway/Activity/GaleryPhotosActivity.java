package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Adapter.GaleryAdapter;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.example.myapplicationflyaway.Model.Upload;
import com.example.myapplicationflyaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GaleryPhotosActivity extends AppCompatActivity {

    ImageButton btn_back_itinerary_page;
    ImageView image_default;
    private FirebaseAuth mAuth;
    Button button_add_image;
    GaleryAdapter galeryAdapter;
    String itineraryId;
    DatabaseReference reference;
    String userId;
    RecyclerView recyclerView;
    ArrayList<Upload> photosList;
    TextView txt_default;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galery_photos);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        recyclerView = findViewById(R.id.viewholder_photos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(GaleryPhotosActivity.this));
        txt_default = findViewById(R.id.txt_default);

        photosList = new ArrayList<Upload>();

        button_add_image = findViewById(R.id.button_add_image);
        btn_back_itinerary_page = findViewById(R.id.btn_back_itinerary_page);
        image_default = findViewById(R.id.image_default);


        itineraryId = getIntent().getExtras().getString("itineraryId");

            try{
                reference = FirebaseDatabase.getInstance().getReference("Galery").child(userId).child(itineraryId);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Upload upload = dataSnapshot.getValue(Upload.class);
                            photosList.add(upload);
                        }
                        galeryAdapter.notifyDataSetChanged();

                        if(photosList.size() == 0 ){
                            image_default.setImageResource(R.drawable.default_galery);
                            txt_default.setText("Você ainda não possui imagens adicionadas...");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                galeryAdapter = new GaleryAdapter(GaleryPhotosActivity.this, photosList);
                recyclerView.setAdapter(galeryAdapter);



            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "errp", Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }


        btn_back_itinerary_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GaleryPhotosActivity.this, ItineraryPageActivity.class);
                i.putExtra("itineraryId", itineraryId);
                startActivity(i);
            }
        });

        button_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GaleryPhotosActivity.this, GaleryActivity.class);
                i.putExtra("itineraryId", itineraryId);
                startActivity(i);
            }
        });

    }

    
}