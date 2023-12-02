package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplicationflyaway.Adapter.NotesAdapter;
import com.example.myapplicationflyaway.Model.Notes;
import com.example.myapplicationflyaway.Model.Upload;
import com.example.myapplicationflyaway.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    Button add_note;
    String itineraryId, itineraryName, itineraryDate;
    TextView txt_placeNem,txt_date;
    private FirebaseAuth mAuth;
    DatabaseReference referenceNotes;
    GridView gridView;
    ImageButton bt_back;
    NotesAdapter notesAdapter;
    ArrayList<Notes> notesList;
    String userId;
    int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        gridView = findViewById(R.id.gridview_notes);
        add_note = findViewById(R.id.add_notes);
        bt_back = findViewById(R.id.btn_back_from_notes);
        txt_placeNem = findViewById(R.id.txt_placeName);
        txt_date =findViewById(R.id.txt_date);

        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter( notesList, NotesActivity.this);
        gridView.setAdapter(notesAdapter);

        itineraryId = getIntent().getExtras().getString("itineraryId");
        itineraryName = getIntent().getExtras().getString("itineraryName");
        itineraryDate = getIntent().getExtras().getString("itineraryDate");

        txt_placeNem.setText(itineraryName);
        txt_date.setText(itineraryDate);
        referenceNotes = FirebaseDatabase.getInstance().getReference("Notes").child(userId).child(itineraryId);



        try{
            referenceNotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                   Notes notes = dataSnapshot.getValue(Notes.class);
                    notesList.add(notes);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotesActivity.this, ItineraryPageActivity.class);
                i.putExtra("ItineraryId", itineraryId);
                startActivity(i);
                finish();
            }
        });


        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotesActivity.this, AddNotesActivity.class);
                i.putExtra("itineraryId", itineraryId);
                startActivity(i);
                finish();
            }
        });


    }
}