package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Model.Notes;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddNotesActivity extends AppCompatActivity {

    Button save_note;
    String title, subtitle, content;
    EditText inputNoteTitle, inputNoteSubtitle, inputContent;
    DatabaseReference referenceNotes;
    FirebaseAuth mAuth;
    String itineraryId, user2, itineraryName, itineraryDate;
    ImageView btn_back_fomNotesSave;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        mAuth = FirebaseAuth.getInstance();
        save_note = findViewById(R.id.button);
        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = findViewById(R.id.editText);
        inputContent = findViewById(R.id.inputNote);
        btn_back_fomNotesSave = findViewById(R.id.btn_back_fomNotesSave);
        String user = mAuth.getCurrentUser().getUid();

        itineraryId = getIntent().getExtras().getString("itineraryId");
        user2 = getIntent().getExtras().getString("UserId");
        itineraryName = getIntent().getExtras().getString("itineraryName");
        itineraryDate = getIntent().getExtras().getString("itineraryDate");

        referenceNotes = FirebaseDatabase.getInstance().getReference().child("Notes").child(user).child(itineraryId);


        btn_back_fomNotesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNotesActivity.this, NotesActivity.class);
                intent.putExtra("itineraryId", itineraryId);
                intent.putExtra("UserId", user2);
                intent.putExtra("itineraryName", itineraryName);
                intent.putExtra("itineraryDate", itineraryDate);
                startActivity(intent);
                finish();
            }
        });

        save_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = inputNoteTitle.getText().toString();
                subtitle = inputNoteSubtitle.getText().toString();
                content = inputContent.getText().toString();

                referenceNotes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Notes notes = new Notes(title, subtitle, content);
                        String key = referenceNotes.push().getKey();
                        referenceNotes.child(key).setValue(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(AddNotesActivity.this, "Anotação criada com sucesso", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(AddNotesActivity.this, NotesActivity.class);
                                intent.putExtra("itineraryId", itineraryId);
                                intent.putExtra("UserId", user2);
                                intent.putExtra("itineraryName", itineraryName);
                                intent.putExtra("itineraryDate", itineraryDate);                                startActivity(intent);
                                finish();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}