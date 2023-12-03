package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Adapter.ItineraryCardAdapter;
import com.example.myapplicationflyaway.Adapter.MyItineraryAdapter;
import com.example.myapplicationflyaway.Fragments.MyitinerariesFragment;
import com.example.myapplicationflyaway.Fragments.ProfileFragment;
import com.example.myapplicationflyaway.ItineraryAdapter;
import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.Model.ItinerarySave;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.DescriptorProtos;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Locale;

public class ItineraryPageActivity extends AppCompatActivity {

    private TextView txt_people, txt_inicial_date, txt_final_date, txt_description, totaldedias,edit_description_popup,edit_number_of_travelers_popup;
    private Button publicar;
    ImageButton btn_back, btn_edit;
    ImageView btn_img, btn_delete, img;

    private RecyclerView recyclerView;
    private ArrayList<Day> daylist;
    private FirebaseUser user;


    Button button_edit_info_itinerary_popup;

    MyAdapter myAdapter;
    String itineraryId, userId, itineraryName, dateTravel, id, numberOfTravelers, desc, date1, date2;
    private FirebaseAuth mAuth;
    private ImageView itinerary_pic, back_popup;
    String uid;
    FloatingActionButton floatingActionButton;

    DatabaseReference reference, publicItinerariesReference;

    LinearLayout btn_clima, btn_galeria, btn_notes;


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
        btn_back = findViewById(R.id.btn_back_myItineraries);
        btn_galeria = findViewById(R.id.btn_galeria);
        btn_edit = findViewById(R.id.btn_edit);
        btn_img = findViewById(R.id.btn_img);
        btn_notes = findViewById(R.id.btn_notes);
        btn_delete = findViewById(R.id.btn_img_delete);

        publicar = findViewById(R.id.btn_publicar);
        floatingActionButton = findViewById(R.id.floatingActionButtontoChat);

        recyclerView = findViewById(R.id.trip_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        daylist = new ArrayList<Day>();


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
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this, ChatActivity.class);
                startActivity(i);
                finish();
            }
        });


        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    publicItinerariesReference = FirebaseDatabase.getInstance().getReference().child("PublicItineraries");

                    AlertDialog.Builder buider = new AlertDialog.Builder(ItineraryPageActivity.this);
                    buider.setTitle("Tem certeza?");
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

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, MyitinerariesFragment.class, null)
                        .commit();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(ItineraryPageActivity.this, EditItineraryPopup.class);
//                i.putExtra("ItineraryId", itineraryId);
//                i.putExtra("description", txt_description.getText().toString());
//                i.putExtra("numberOfTravelers", txt_people.getText().toString());
//                startActivity(i);
                onButtonShowPopupWindowClick(view);

            }
        });


        btn_clima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this, ClimaActivity.class);
                i.putExtra("itineraryId", itineraryId);
                startActivity(i);
                /////////////////////

                ////////////////////////
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder buider = new AlertDialog.Builder(ItineraryPageActivity.this);
                buider.setTitle("Tem certeza?");
                buider.setMessage("Se deletar seu roteiro, não poderá recuperá-lo");

                buider.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent i = new Intent(ItineraryPageActivity.this, MyitinerariesFragment.class);
//                        startActivity(i);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, MyitinerariesFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("name")
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
        });



        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ItineraryPageActivity.this, downloadImgItineraryPage.class);
                i.putExtra("ItineraryId", itineraryId);
                startActivity(i);
            }
        });

        btn_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this, GaleryPhotosActivity.class);
                i.putExtra("itineraryId", itineraryId);
                startActivity(i);
                finish();

            }
        });

        btn_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItineraryPageActivity.this, NotesActivity.class);
                i.putExtra("itineraryId", itineraryId);
                i.putExtra("itineraryName", itineraryName);
                i.putExtra("itineraryDate", dateTravel);
                startActivity(i);
            }
        });
    }

    public void onButtonShowPopupWindowClick(View view){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_edit_itinerary_popup, null);
        back_popup = popupView.findViewById(R.id.imageView14);
        edit_description_popup = popupView.findViewById(R.id.edit_description);
        edit_number_of_travelers_popup = popupView.findViewById(R.id.edit_number_of_travelers);
        button_edit_info_itinerary_popup = popupView.findViewById(R.id.button_edit_info_itinerary);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        back_popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        button_edit_info_itinerary_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("Itineraries");
                dbReference.child(mAuth.getCurrentUser().getUid()).child(itineraryId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, Object> ItineraryMap = new HashMap<>();

                        if(!edit_description_popup.getText().toString().isEmpty()){
                            ItineraryMap.put("Description",edit_description_popup.getText().toString());
                        }
                        if(!edit_number_of_travelers_popup.getText().toString().isEmpty()){
                            ItineraryMap.put("numberOfTravelers",edit_number_of_travelers_popup.getText().toString());
                        }

                        dbReference.child(mAuth.getCurrentUser().getUid()).child(itineraryId).updateChildren(ItineraryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                edit_description_popup.setText("");
                                edit_number_of_travelers_popup.setText("");
                            }

                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                popupWindow.dismiss();
                                Toast.makeText(ItineraryPageActivity.this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(ItineraryPageActivity.this,ItineraryPageActivity.class);
//                                i.putExtra("ItineraryId",itineraryId);
//                                startActivity(i);
                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        popupWindow.dismiss();
                        Toast.makeText(ItineraryPageActivity.this, "Não foi possível alterar os dados", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }



}