package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.myapplicationflyaway.Fragments.MyitinerariesFragment;
import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.Model.ItinerarySave;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ItineraryPageActivity extends AppCompatActivity {

    private TextView txt_itinerary_name,txt_people, txt_inicial_date, txt_final_date, txt_description, totaldedias,edit_description_popup,edit_number_of_travelers_popup;
    private Button publicar;
    ImageButton btn_back, btn_edit;
    ImageView btn_img, btn_delete, img,gpt_assisten_btn ;

    private RecyclerView recyclerView;
    private ArrayList<Day> daylist;
    private FirebaseUser user;
    Button button_edit_info_itinerary_popup;

    MyAdapter myAdapter;
    String itineraryId, userId, itineraryName, dateTravel, id, numberOfTravelers, desc, date1, date2;
    private FirebaseAuth mAuth;

    private ImageView itinerary_pic, back_popup;
    String uid;
    ImageButton floatingActionButton;
  
    DatabaseReference reference, publicItinerariesReference;

    LinearLayout btn_clima, btn_galeria, btn_notes, btn_financas;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.itinerary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        itineraryId = getIntent().getExtras().getString("ItineraryId");
        userId = getIntent().getExtras().getString("UserId");

        String id = String.valueOf(item.getItemId());
        String id1 = "o";
        String id2, id4,id5;
        id1 = String.valueOf(R.id.delete_itinerary);
        id2 = String.valueOf(R.id.edit_itinerary_menu);
        id4 = String.valueOf(R.id.add_cover);
        id5 = String.valueOf(R.id.publicate);

        if(id1.contains(id)){
            deleteItinerary();
        }
        if(id2.contains(id)){
            Intent i = new Intent(ItineraryPageActivity.this, EditItinerary.class);
            i.putExtra("ItineraryId", itineraryId);
            i.putExtra("UserId", userId);
            startActivity(i);
            finish();
        }

        if(id4.contains(id)){
            Intent c = new Intent(ItineraryPageActivity.this, downloadImgItineraryPage.class);
                c.putExtra("ItineraryId", itineraryId);
                c.putExtra("UserId", userId);
                startActivity(c);
                finish();
                return true;
        }
        if(id5.contains(id)){
            try {
                    publicItinerariesReference = FirebaseDatabase.getInstance().getReference().child("PublicItineraries");

                    AlertDialog.Builder buider = new AlertDialog.Builder(ItineraryPageActivity.this);
                    buider.setTitle("Compartilhar roteiro");
                    buider.setMessage("Seu roteiro ficará visível para todos os usuários");

                    buider.setPositiveButton("Publicar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            publicItinerariesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String userId = mAuth.getCurrentUser().getUid();

                                    ItinerarySave itinerary = new ItinerarySave(id, itineraryName, date1, date2, numberOfTravelers, userId);
                                    String key = publicItinerariesReference.push().getKey();
                                    publicItinerariesReference.child(key).setValue(itinerary).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ItineraryPageActivity.this, "Publicado", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                    });
                    buider.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ItineraryPageActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                        }
                    });
                    buider.show();

                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_page);

        mAuth = FirebaseAuth.getInstance();

        txt_people = findViewById(R.id.pessoas);
        txt_inicial_date = findViewById(R.id.datai);
        txt_final_date = findViewById(R.id.dataf);
        txt_description = findViewById(R.id.description);
        itinerary_pic = findViewById(R.id.cover_1);
        btn_clima = findViewById(R.id.btn_clima);
        btn_back = findViewById(R.id.btn_back_myItinerariesList);
        btn_galeria = findViewById(R.id.btn_galeria);
        btn_notes = findViewById(R.id.btn_notes);
        btn_financas = findViewById(R.id.btn_financas);
        MyitinerariesFragment myitinerariesFragment = new MyitinerariesFragment();
        gpt_assisten_btn =findViewById(R.id.gpt_assisten_btn);
        txt_itinerary_name = findViewById(R.id.localdoroteiro);


        recyclerView = findViewById(R.id.trip_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        daylist = new ArrayList<Day>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itineraryId = getIntent().getExtras().getString("ItineraryId");
            userId = getIntent().getExtras().getString("UserId");

            reference = FirebaseDatabase.getInstance().getReference("Itineraries").child(userId).child(itineraryId);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    id = snapshot.child("id").getValue().toString();
                    txt_inicial_date.setText(snapshot.child("inicialDate").getValue().toString());
                    date1 = snapshot.child("inicialDate").getValue().toString();
                    date2 = snapshot.child("finalDate").getValue().toString();
                    txt_final_date.setText(snapshot.child("finalDate").getValue().toString());
                    itineraryName = snapshot.child("placeName").getValue().toString();
                    numberOfTravelers = snapshot.child("numberOfTravelers").getValue().toString();
                    txt_itinerary_name.setText(itineraryName);

                    dateTravel = snapshot.child("inicialDate").getValue().toString() + " até " + snapshot.child("finalDate").getValue().toString();

                    if (snapshot.child("Description").getValue() == null) {
                    } else {
                        desc = snapshot.child("Description").getValue().toString();
                        txt_description.setText((snapshot.child("Description").getValue().toString()));
                    }

                    if (snapshot.child("img").getValue() == null) {
                    } else {
                        String image = snapshot.child("img").getValue().toString();
                        Picasso.get().load(image).into(itinerary_pic);
                    }
                    if (snapshot.hasChild("Days")) {
                        reference.child("Days").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Day day = dataSnapshot.getValue(Day.class);
                                    daylist.add(day);
                                }
                                myAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        myAdapter = new MyAdapter(ItineraryPageActivity.this, daylist);
                        recyclerView.setAdapter(myAdapter);
                    }

                    txt_people.setText((snapshot.child("numberOfTravelers").getValue().toString()));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, myitinerariesFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });
        gpt_assisten_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(ItineraryPageActivity.this, ChatActivity.class);
                b.putExtra("itineraryId", itineraryId);
                b.putExtra("userId", userId);
                startActivity(b);
                finish();
            }
        });


        btn_financas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this,FinancesActivity.class);
                i.putExtra("ItineraryId",itineraryId);
                startActivity(i);
            }
        });

        btn_clima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this, ClimaActivity.class);
                i.putExtra("itineraryId", itineraryId);
                i.putExtra("UserId", userId);
                i.putExtra("ItineraryName",itineraryName);
                startActivity(i);

            }
        });

        btn_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this, GaleryPhotosActivity.class);
                i.putExtra("itineraryId", itineraryId);
                i.putExtra("UserId", userId);
                startActivity(i);
                finish();
            }
        });

        btn_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this, NotesActivity.class);
                i.putExtra("itineraryId", itineraryId);
                i.putExtra("UserId", userId);
                i.putExtra("itineraryName", itineraryName);
                i.putExtra("itineraryDate", dateTravel);
                startActivity(i);
            }
        });
    }

    public void deleteItinerary(){
        AlertDialog.Builder buider = new AlertDialog.Builder(ItineraryPageActivity.this);
        buider.setTitle("Tem certeza?");
        buider.setMessage("Se deletar seu roteiro, não poderá recuperá-lo");

        buider.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, MyitinerariesFragment.class, null)
                        .commit();

                Toast.makeText(ItineraryPageActivity.this, "Roteiro deletado com sucesso", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("Itineraries")
                        .child(userId).child(itineraryId).removeValue();
            }
        });

        buider.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ItineraryPageActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        buider.show();
    }

}