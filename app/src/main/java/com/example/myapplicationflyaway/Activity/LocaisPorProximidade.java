package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;


import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.Manifest;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.databinding.ActivityLocaisPorProximidadeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocaisPorProximidade extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap mMap;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private  static  final int Request_code = 101;
    private double lat, lng;

    ImageButton atm, bank, hosp, res, store;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locais_por_proximidade);


        atm = findViewById(R.id.atm);
        bank = findViewById(R.id.bancos);
        hosp = findViewById(R.id.hospital);
        res = findViewById(R.id.restaurantes);
        store = findViewById(R.id.store);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
              this.getApplicationContext());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);

        mapFragment.getMapAsync(this);


        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder
                        ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");
                stringBuilder.append(lat+","+lng);
                stringBuilder.append("&radius=1000");
                stringBuilder.append("&type=tourist_attraction");
                stringBuilder.append("&keyword=tourist_attraction");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key="+getResources().getString(R.string.google_maps_key));

                String url = stringBuilder.toString();
                Object dataFectch[] = new Object[2];
                dataFectch[0]=mMap;
                dataFectch[1]=url;

                Pegarinformacoes pegarinformacoes = new Pegarinformacoes();
                pegarinformacoes.execute(dataFectch);
            }
        });

        hosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder
                        ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");
                stringBuilder.append(lat+","+lng);
                stringBuilder.append("&radius=1000");
                stringBuilder.append("&type=restaurant");
                stringBuilder.append("&keyword=restaurant");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key="+getResources().getString(R.string.google_maps_key));

                String url = stringBuilder.toString();
                Object dataFectch[] = new Object[2];
                dataFectch[0]=mMap;
                dataFectch[1]=url;

                Pegarinformacoes pegarinformacoes = new Pegarinformacoes();
                pegarinformacoes.execute(dataFectch);
            }
        });

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder
                        ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");
                stringBuilder.append(lat+","+lng);
                stringBuilder.append("&radius=1000");
                stringBuilder.append("&type=bus_station");
                stringBuilder.append("&keyword=bus_station");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key="+getResources().getString(R.string.google_maps_key));

                String url = stringBuilder.toString();
                Object dataFectch[] = new Object[2];
                dataFectch[0]=mMap;
                dataFectch[1]=url;

                Pegarinformacoes pegarinformacoes = new Pegarinformacoes();
                pegarinformacoes.execute(dataFectch);
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder
                        ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");
                stringBuilder.append(lat+","+lng);
                stringBuilder.append("&radius=1000");
                stringBuilder.append("&type=park");
                stringBuilder.append("&keyword=park");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key="+getResources().getString(R.string.google_maps_key));

                String url = stringBuilder.toString();
                Object dataFectch[] = new Object[2];
                dataFectch[0]=mMap;
                dataFectch[1]=url;

                Pegarinformacoes pegarinformacoes = new Pegarinformacoes();
                pegarinformacoes.execute(dataFectch);
            }
        });

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder
                        ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");
                stringBuilder.append(lat+","+lng);
                stringBuilder.append("&radius=1000");
                stringBuilder.append("&type=store");
                stringBuilder.append("&keyword=store");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key="+getResources().getString(R.string.google_maps_key));

                String url = stringBuilder.toString();
                Object dataFectch[] = new Object[2];
                dataFectch[0]=mMap;
                dataFectch[1]=url;

                Pegarinformacoes pegarinformacoes = new Pegarinformacoes();
                pegarinformacoes.execute(dataFectch);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LocalizacaoAtual();
    }

    private void LocalizacaoAtual(){
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION}, Request_code);
        return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);
        LocationCallback locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

                if (locationResult == null){
                    Toast.makeText(getApplicationContext(),"localizacao atual nula",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                for (Location location:locationResult.getLocations()){

                    if(location!=null){
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        Toast.makeText(getApplicationContext(), "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();

                    }
                }

            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){

                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    LatLng latLng = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("current location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

                }
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (Request_code){
            case Request_code: if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){LocalizacaoAtual();}
        }
    }
}