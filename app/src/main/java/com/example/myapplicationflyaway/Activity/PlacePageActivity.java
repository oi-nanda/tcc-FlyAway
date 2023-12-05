package com.example.myapplicationflyaway.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Fragments.MyitinerariesFragment;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.LocalTime;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.IsOpenRequest;
import com.google.android.libraries.places.api.net.IsOpenResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PlacePageActivity extends FragmentActivity {
    private PlacesClient placesClient;
    String itineraryId, placeIdReturn, Dayname;
    TextView phoneNumber, Address, isopen, placename, valor, review,description,horarios;
    ImageView foto;
    FirebaseUser user;
    String uid;
    Button removeratracao;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_page);
        Bundle bundle = getIntent().getExtras();
        foto = findViewById(R.id.cover_place);
        Address = findViewById(R.id.endereco);
        phoneNumber = findViewById(R.id.telefone);
        placename = findViewById(R.id.nameplace);
        isopen = findViewById(R.id.abertooufechado);
        valor = findViewById(R.id.valor);
        review = findViewById(R.id.textView31);
        description = findViewById(R.id.textView26);
        horarios = findViewById(R.id.textView30);
        removeratracao = findViewById(R.id.button2);

        itineraryId = getIntent().getStringExtra("ItineraryId");
        placeIdReturn = getIntent().getStringExtra("PlaceId");
        Dayname = getIntent().getStringExtra("Dayname");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Itineraries").child(uid).child(itineraryId)
                .child("Days").child(Dayname).child("Places").child(placeIdReturn);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String costst = snapshot.child("cost").getValue().toString();
                String descst = snapshot.child("description").getValue().toString();
                valor.setText("R$ "+costst);
                description.setText(descst);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        valor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });

        Places.initialize(getApplicationContext(), getString(R.string.my_map_api_key));
        placesClient = Places.createClient(this);
        final String placeId = placeIdReturn;

            List<Place.Field> placeFields = Arrays.asList(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.PHONE_NUMBER,
                    Place.Field.OPENING_HOURS,
                    Place.Field.PHOTO_METADATAS,
                    Place.Field.RATING
            );

            FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {

                Place place = response.getPlace();

                if(place.getOpeningHours() == null || place.getOpeningHours().toString().isEmpty()){
                    horarios.setText("");
                }
                else{
//                    List<Period> periodos = place.getOpeningHours().getPeriods();
//                    StringBuilder horariosdeabertura = new StringBuilder("Horários de Funcionamento:\n");
//
//                    for (Period period : periodos) {
//                        SimpleDateFormat simple = new SimpleDateFormat("HH:mm", Locale.getDefault());
//                        String openTime = String.valueOf(period.getOpen().getTime().getHours());
//                        String closeTime = String.valueOf(period.getClose().getTime().getHours());
//         //               LocalTime openTime = LocalTime.of(period.getOpen().getTime().getHours(), period.getOpen().getTime().getMinutes()); // Horário de abertura
////                        LocalTime closeTime = period.getClose().getTime(); // Horário de fechamento
//                        String horariofechar = simple.format(closeTime)+" h";
//                        String horarioabrir = simple.format(openTime)+ " h";
//                        String diadeabrir = "";
//
//                        if (period.getOpen().getDay().toString().equals("SUNDAY")) { diadeabrir="segunda";}
//                        if (period.getOpen().getDay().toString().equals("MONDAY")) { diadeabrir="terça";}
//                        if (period.getOpen().getDay().toString().equals("TUESDAY")) { diadeabrir="quarta";}
//                        if (period.getOpen().getDay().toString().equals("WEDNESDAY")) { diadeabrir="quinta";}
//                        if (period.getOpen().getDay().toString().equals("THURSDAY")) { diadeabrir="sexta";}
//                        if (period.getOpen().getDay().toString().equals("FRIDAY")) { diadeabrir="sábado";}
//                        if (period.getOpen().getDay().toString().equals("SATURDAY")) { diadeabrir="domingo";}
//
//
//                        horariosdeabertura
//                                .append(diadeabrir)
//                                .append(", Abre: ")
//                                .append(openTime+" h")
//                                .append(", Fecha: ")
//                                .append(closeTime+" h")
//                                .append("\n")
//                                .append("\n");
//
//                    }
//
//                    String openingHours = horariosdeabertura.toString();
                  //  horarios.setText(openingHours);
                }

                if (place.getAddress() != null && !place.getAddress().isEmpty()) {
                    Address.setText(place.getAddress());
                } else {
                    Address.setText("Endereço indisponível");
                }

                if (place.getRating() != null && !place.getRating().toString().isEmpty()) {
                    review.setText(place.getRating().toString());
                } else {
                    review.setText("Review indisponível");
                }

                if (place.getPhoneNumber() != null && !place.getPhoneNumber().isEmpty()) {
                    phoneNumber.setText(place.getPhoneNumber());
                } else {
                    phoneNumber.setText("Sem número de telefone");
                }

                final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
                Drawable alternativeImage = getResources().getDrawable(R.drawable.img_13);
                if (metadata == null || metadata.isEmpty()) {
                    foto.setImageDrawable(alternativeImage);
                }
                else{
                    final PhotoMetadata photoMetadata = metadata.get(0);
                    final String attributions = photoMetadata.getAttributions();
                    final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .setMaxWidth(1500)
                            .setMaxHeight(1600)
                            .build();

                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                        Bitmap bitmap = fetchPhotoResponse.getBitmap();
                        foto.setImageBitmap(bitmap);
                    }).addOnFailureListener((exception) -> {
                        foto.setImageDrawable(alternativeImage);
                        if (exception instanceof ApiException) {
                            final ApiException apiException = (ApiException) exception;
                            final int statusCode = apiException.getStatusCode();
                            // TODO: Handle error with given status code.
                        }
                    });
                }

                @NonNull
                Calendar isOpenCalendar = Calendar.getInstance();


                if (place.getName() != null && !place.getName().isEmpty()) {
                    placename.setText(place.getName());
                } else {
                    Toast.makeText(this,"Não foi possível visualizar as informações do lugar", Toast.LENGTH_SHORT).show();
                }


                if (place.getOpeningHours() == null){
                    isopen.setText("Sem horário de funcionamento");
                }
                else {
                    IsOpenRequest isOpenRequest;

                    try {
                        isOpenRequest = IsOpenRequest.newInstance(place, isOpenCalendar.getTimeInMillis());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        return;
                    }
                    Task<IsOpenResponse> isOpenTask = placesClient.isOpen(isOpenRequest);

//                    AtomicReference<Boolean> isOpen = null;
                    isOpenTask.addOnSuccessListener(
                            (isOpenResponse) -> {
//                                isOpen.set(isOpenResponse.isOpen())
                                boolean isOpen = isOpenResponse.isOpen();
                                if (isOpen) {
                                    isopen.setText("Aberto");
                                    isopen.setTextColor(Color.GREEN);
                                } else {
                                    isopen.setText("Fechado");
                                    isopen.setTextColor(Color.RED);
                                }
                            }
                    );
                    isOpenTask.addOnFailureListener(
                            (isOpenResponse) -> {
                                isopen.setText("Informações indisponíveis");
                            }
                    );

                }



            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                }
            });




        removeratracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder buider = new AlertDialog.Builder(PlacePageActivity.this);
                buider.setTitle("Tem certeza?");
                buider.setMessage("Este lugar será removido do seu roteiro");

                buider.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(PlacePageActivity.this,DayPageActivity.class);
                        i.putExtra("DayName",Dayname);
                        i.putExtra("ItineraryId",itineraryId);
                        Toast.makeText(PlacePageActivity.this, "Lugar deletado com sucesso", Toast.LENGTH_SHORT).show();

                        FirebaseDatabase.getInstance().getReference().child("Itineraries")
                                .child(uid).child(itineraryId).child("Days").child(Dayname)
                                .child("Places").child(placeIdReturn).removeValue();
                    }
                });

                buider.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PlacePageActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
                buider.show();

            }
        });
    }

    public void popup (){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_question_edit_popup, null);
        Button btn = popupView.findViewById(R.id.button_edit_info_itinerary);
        ImageView close_popup = popupView.findViewById(R.id.imageView14);
        EditText edt = popupView.findViewById(R.id.edit_description);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        FrameLayout tempLayout = new FrameLayout(getApplicationContext());
        tempLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(tempLayout, Gravity.CENTER, 0, 0);


        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edt.getText().toString() == null || edt.getText().toString().isEmpty()){
                    popupWindow.dismiss();
                }
                else{
                    valor.setText(edt.getText().toString());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String, Object> PlaceMap = new HashMap<>();
                            PlaceMap.put("cost",edt.getText().toString());
                            reference.updateChildren(PlaceMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    popupWindow.dismiss();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
//                            popupWindow.dismiss();
                        }
                    });
                }
                return false;
            }
        });

        close_popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}