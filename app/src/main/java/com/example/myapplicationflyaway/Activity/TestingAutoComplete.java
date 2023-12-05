package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Model.Day;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.IsOpenRequest;
import com.google.android.libraries.places.api.net.IsOpenResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class TestingAutoComplete extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static String TAG = "Info";
    String itineraryId, dayName;
    FusedLocationProviderClient fused;
    public static int LOCATION_REQUEST_CODE = 100;
    Location localizacaoAtual;
    private PlacesClient placesClient;
    private ImageView close_popup, foto;
    private TextView endereco, celular,nomedolugar,status, openinghours;
    private Marker marker;
    private Button button_add_places;

    private DatabaseReference dbRefPlace;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_auto_complete);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itineraryId = getIntent().getExtras().getString("ItineraryId");
            dayName = getIntent().getExtras().getString("DayName");}

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        String apiKey = getString(R.string.my_map_api_key);

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),apiKey);
        }

        placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autoComplete);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG, Place.Field.ADDRESS,Place.Field.PHONE_NUMBER,Place.Field.PHOTO_METADATAS));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getAddress());
                Geocoder geocoder = new Geocoder((TestingAutoComplete.this));
                List<Address> address = null;
                try{
//                  address=geocoder.getFromLocationName(place.getName(),1);
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

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker arg0) {
                        FrameLayout tempLayout = new FrameLayout(getApplicationContext());
                        tempLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                        onButtonShowPopupWindowClick(place, tempLayout);
                        return true;
                    }
                });
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "Ocorreu um erro: " + status);
            }
        });

        fused = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

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
//    LatLng canoas = new LatLng(-29.9177,-51.1839);
//        if (mMap != null) {
//            mMap.addMarker(new MarkerOptions().position(canoas).title("Canoas"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(canoas, 14));
//        } else {
//            Log.e(TAG, "Map is null. Unable to add marker.");
//        }

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

    public void onButtonShowPopupWindowClick(Place place, View v){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_show_place_details_popup, null);
        endereco = popupView.findViewById(R.id.endereco);
        celular  = popupView.findViewById(R.id.telefone);
        nomedolugar = popupView.findViewById(R.id.place_name);
        close_popup = popupView.findViewById(R.id.imageView14);
        status = popupView.findViewById(R.id.status);
        foto = popupView.findViewById(R.id.foto);
        openinghours = popupView.findViewById(R.id.openinghours);


//        final List<Period> hoursopen = place.getOpeningHours().getPeriods();
//        hoursopen.toString();
        //////////////////////////////////////////////////

        @NonNull
        Calendar isOpenCalendar = Calendar.getInstance();
        String placeId = place.getId();
        List<Place.Field> placeFields = new ArrayList<>(Arrays.asList(
                Place.Field.BUSINESS_STATUS,
                Place.Field.CURRENT_OPENING_HOURS,
                Place.Field.ID,
                Place.Field.OPENING_HOURS,
                Place.Field.UTC_OFFSET,
                Place.Field.PHONE_NUMBER
        ));

        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        Task<FetchPlaceResponse> placeTask = placesClient.fetchPlace(request);

        placeTask.addOnSuccessListener(
                (placeResponse) -> {
                    Place lugar = placeResponse.getPlace();

                    if (lugar.getOpeningHours() == null){
                        status.setText("Sem horário de funcionamento");
                    }
                    else {
                        IsOpenRequest isOpenRequest;

                        try {
                            isOpenRequest = IsOpenRequest.newInstance(lugar, isOpenCalendar.getTimeInMillis());
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
                                        status.setText("Aberto");
                                        status.setTextColor(Color.GREEN);
                                    } else {
                                        status.setText("Fechado");
                                        status.setTextColor(Color.RED);
                                    }
                                }
                        );
                        isOpenTask.addOnFailureListener(
                                (isOpenResponse) -> {
                                    status.setText("Informações indisponíveis");
                                }
                        );

                    }

                });

        if (place.getAddress() != null && !place.getAddress().isEmpty()) {
            endereco.setText(place.getAddress());
        } else {
            endereco.setText("Endereço indisponível");
        }

        if (place.getPhoneNumber() != null && !place.getPhoneNumber().isEmpty()) {
            celular.setText(place.getPhoneNumber());
        } else {
            celular.setText("Sem número de telefone");
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
                    .setMaxWidth(500)
                    .setMaxHeight(200)
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

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        close_popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        if (place.getName() != null && !place.getName().isEmpty()) {
            nomedolugar.setText(place.getName());
        } else {
            Toast.makeText(this,"Não foi possível visualizar as informações do lugar", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        }

        button_add_places = popupView.findViewById(R.id.button_add_place);

        button_add_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlace(place);
            }
        });


        if (v.getParent() instanceof ViewGroup) {
            ((ViewGroup) v.getParent()).removeView(v);
        }
    }

    public void createPlace(Place place){

        String placeId = place.getId();
        String placeName = place.getName();

        dbRefPlace = FirebaseDatabase.getInstance().getReference("Itineraries");
        mAuth = FirebaseAuth.getInstance();

        com.example.myapplicationflyaway.Model.Place newplace = new com.example.myapplicationflyaway.Model.Place(
                place.getName(),"",0.00,place.getId(),null,dayName,itineraryId
        );
        Log.i("msg",dayName+"   "+itineraryId);

        dbRefPlace.child(mAuth.getCurrentUser().getUid()).child(itineraryId).child("Days").child(dayName).child("Places").child(place.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbRefPlace.child(mAuth.getCurrentUser().getUid()).child(itineraryId).child("Days").child(dayName).child("Places").child(place.getId()).setValue(newplace).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
                Toast.makeText(TestingAutoComplete.this, "Lugar adicionado com sucesso", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(TestingAutoComplete.this,DayPageActivity.class);
                i.putExtra("DayName",dayName);
                i.putExtra("ItineraryId",itineraryId);
                startActivity(i);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}