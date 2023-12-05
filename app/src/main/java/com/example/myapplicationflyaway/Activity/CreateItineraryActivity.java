package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Fragments.HomeFragment;
import com.example.myapplicationflyaway.MainActivity;
import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.databinding.ActivityCreateItineraryBinding;
import com.example.myapplicationflyaway.databinding.ActivityMainBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.IsOpenRequest;
import com.google.android.libraries.places.api.net.IsOpenResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class CreateItineraryActivity extends FragmentActivity implements OnMapReadyCallback {

    ActivityCreateItineraryBinding binding;
    String placeName, inicialDate, finalDate, numberOfTravelers, lugar;
    Button add;
    final Calendar currentDate = Calendar.getInstance();
    final Calendar d1 = Calendar.getInstance();
    final Calendar d2 = Calendar.getInstance();
    Day day[];
    ProgressDialog progressDialog;
    FirebaseDatabase db;
    DatabaseReference reference, dbRefDay;

    FirebaseUser user;

    String uid;

    String id;
    Long totaldedias;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    EditText data1, data2;
    TextView npessoas;
    SearchView local;
    Button start,button2;
    String dt;

    ImageButton btn_back_home;

    Fragment autoCompleteCriar;
    final Calendar calendario = Calendar.getInstance();

    private GoogleMap mMap;
    private static String TAG = "Info";
    FusedLocationProviderClient fused;
    public static int LOCATION_REQUEST_CODE = 100;
    Location localizacaoAtual;
    private PlacesClient placesClient;
    private ImageView close_popup, foto;
    private TextView endereco, celular,nomedolugar,status, openinghours;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateItineraryBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.imageViewCriar);
        mapFragment.getMapAsync(this);

        String apiKey = getString(R.string.my_map_api_key);

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),apiKey);
        }

        placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autoCompleteCriar);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG, Place.Field.ADDRESS,Place.Field.PHONE_NUMBER,Place.Field.PHOTO_METADATAS));
        autocompleteSupportFragment.setTypeFilter(TypeFilter.CITIES);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Geocoder geocoder = new Geocoder((CreateItineraryActivity.this));
                List<Address> address = null;
                try{
                    address= geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude,1);

                }
                catch (IOException e){
                    e.printStackTrace();
                }

                Address addresses = address.get(0);
                LatLng latLng = new LatLng(addresses.getLatitude(),addresses.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                placeName = place.getName();

            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "Ocorreu um erro: " + status);
            }
        });

        fused = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();

        binding.buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicialDate = binding.data1.getText().toString();
                finalDate = binding.data2.getText().toString();
                numberOfTravelers = binding.npessoas.getText().toString();

                progressDialog.setMessage("Criando roteiro do usuário");
                progressDialog.setTitle("Roteiro");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if (!placeName.isEmpty() && !inicialDate.isEmpty() && !finalDate.isEmpty() && !numberOfTravelers.isEmpty()) {


                    if ((d2.after(d1) || d1.equals(d2)) && (d1.after(currentDate) || d1.compareTo(currentDate) == 0)){
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        uid = user.getUid().toString();
                        id = UUID.randomUUID().toString();

                    Itinerary itinerary = new Itinerary(id,placeName,inicialDate, finalDate, numberOfTravelers, null, null);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Itineraries");
                    dbRefDay = FirebaseDatabase.getInstance().getReference("Itineraries").child(uid).child(id).child("Days");

                    reference.child(uid).child(id).setValue(itinerary).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                            binding.searchview.setQuery("", false);
                            binding.data1.setText("");
                            binding.data2.setText("");
                            binding.npessoas.setText("");
                            Toast.makeText(CreateItineraryActivity.this, "Roteiro criado com sucesso!",Toast.LENGTH_SHORT).show();
                            sendUserToItineraryPage();
                        }
                    });

                        String datainicial = (inicialDate).substring(0,6);
                        String datainicial2 = (inicialDate).substring(6);
                        String datafinal = (finalDate).substring(0,6);
                        String datafinal2 = (finalDate).substring(6);

                        String datainicialnew = datainicial+"20"+datainicial2;
                        String datafinalnew = datafinal+"20"+datafinal2;

                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        sdf.setLenient (false);
                        try {
                            Date d1 = sdf.parse(datainicialnew);
                            Date d2 = sdf.parse(datafinalnew);

                            Long diff = d2.getTime() - d1.getTime();

                            totaldedias = ((diff/(100*60*60*24))/10)+1;

                            for (int i=1;i<=totaldedias;i++){
                                String dayname = "dia " + i;
                                String idday = UUID.randomUUID().toString();

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(d1);
                                if (i==1){}
                                else{
                                    calendar.add(Calendar.DATE, i-1);
                                }
                                SimpleDateFormat simple = new SimpleDateFormat("dd-MM-yyyy");
                                dt = simple.format(calendar.getTime());

                                Day day = new Day(dayname,"",idday,null,id,dt,0.00);
                                dbRefDay.child(dayname).setValue(day).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                    }
                                });
                            }

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    else {
                      if (d1.after(d2)){
                          progressDialog.dismiss();
                          binding.data2.setError("Data final inválida");
                          Toast.makeText(CreateItineraryActivity.this, "A data final deve vir após a data inicial", Toast.LENGTH_SHORT).show();
                      }
                      if (currentDate.after(d1)) {
                          progressDialog.dismiss();
                          binding.data1.setError("Data incial inválida");
                          Toast.makeText(CreateItineraryActivity.this, "A data inicial não pode anteceder o dia de hoje", Toast.LENGTH_SHORT).show();
                      }
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(CreateItineraryActivity.this, "Erro ao criar o roteiro", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_back_home = findViewById(R.id.btn_back_home);
//        local = findViewById(R.id.searchview);
        data1=(EditText) findViewById(R.id.data1);
        data2=(EditText) findViewById(R.id.data2);

        btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        DatePickerDialog.OnDateSetListener datai = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                calendario.set(Calendar.YEAR, i);
                calendario.set(Calendar.MONTH,i2);
                calendario.set(Calendar.DAY_OF_MONTH,i3);
                d1.set(Calendar.YEAR, i);
                d1.set(Calendar.MONTH,i2);
                d1.set(Calendar.DAY_OF_MONTH,i3);
                Date dataCalendario = calendario.getTime();
                SimpleDateFormat data = new SimpleDateFormat("dd/MM/yy");
                data1.setText(data.format(dataCalendario));
                data1.setTextColor(androidx.appcompat.R.style.Base_TextAppearance_AppCompat_Body2);
            }
        };

        DatePickerDialog.OnDateSetListener dataf = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                calendario.set(Calendar.YEAR, i);
                calendario.set(Calendar.MONTH,i2);
                calendario.set(Calendar.DAY_OF_MONTH,i3);
                Date dataCalendario = calendario.getTime();
                d2.set(Calendar.YEAR, i);
                d2.set(Calendar.MONTH,i2);
                d2.set(Calendar.DAY_OF_MONTH,i3);
                SimpleDateFormat data = new SimpleDateFormat("dd/MM/yy");
                data2.setText(data.format(dataCalendario));
                data2.setTextColor(androidx.appcompat.R.style.Base_TextAppearance_AppCompat_Body2);

            }
        };

        data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();
                int ano = calendario.get(calendario.YEAR);
                int mes = calendario.get(calendario.MONTH);
                int dia = calendario.get(calendario.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(CreateItineraryActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, datai,ano,mes,dia);
                d.show();
            }
        });

        data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();
                int ano = calendario.get(calendario.YEAR);
                int mes = calendario.get(calendario.MONTH);
                int dia = calendario.get(calendario.DAY_OF_MONTH);

                DatePickerDialog d = new DatePickerDialog(CreateItineraryActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, dataf,ano,mes,dia);
                d.show();
            }
        });
    }

    private void sendUserToItineraryPage() {
        Intent intent = new Intent(CreateItineraryActivity.this, ItineraryPageActivity.class);
        intent.putExtra("ItineraryId", id);
        intent.putExtra("UserId", mAuth.getCurrentUser().getUid());
        startActivity(intent);
        finish();
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            GetUserLocation();
        } else {
            requestForpermissions();
        }
    }

    private void GetUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fused.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if( location != null){
                    double lat = location.getLatitude();
                    double longitude = location.getLongitude();

                    LatLng userLocation = new LatLng(lat, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Sua localização atual!"));

                }
            }

        });
    }

    private void requestForpermissions() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"localização permitida", Toast.LENGTH_SHORT).show();
                GetUserLocation();
            }
            else{
                Toast.makeText(this,"O acesso a localização atual foi negado. Para acessar o mapa, permita o acesso", Toast.LENGTH_SHORT).show();
            }
        }
    }


}