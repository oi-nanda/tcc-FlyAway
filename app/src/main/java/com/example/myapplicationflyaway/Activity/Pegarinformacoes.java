package com.example.myapplicationflyaway.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplicationflyaway.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Pegarinformacoes extends AsyncTask<Object,String,String> {

    String googleNearByPlacesData;
    GoogleMap googleMap;
    String url;

    private GoogleMap.OnMarkerClickListener onMarkerClickListener;

    Pegarinformacoes (Context context) {}

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONObject getLocation = jsonObject1.getJSONObject("geometry")
                        .getJSONObject("location");

                String lat = getLocation.getString("lat");
                String lng = getLocation.getString("lng");
                String placeId = jsonObject1.getString("place_id");

                JSONObject getName = jsonArray.getJSONObject(i);
                String name = getName.getString("name");

                LatLng latLng = new LatLng(Double.parseDouble(lat),
                        Double.parseDouble(lng));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(placeId);
                markerOptions.position(latLng);
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

            ;

            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    protected String doInBackground(Object... objects) {
        try{
            googleMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            BaixarUrl downloadUrl = new BaixarUrl();
            googleNearByPlacesData = downloadUrl.rUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleNearByPlacesData;
    }

//    public void CriarPopup (){
//
//        Places.initialize(getApplicationContext(), getString(R.string.my_map_api_key));
//        placesClient = Places.createClient(this);
//        final String placeId = placeIdReturn;
//
//        List<Place.Field> placeFields = Arrays.asList(
//                Place.Field.ID,
//                Place.Field.NAME,
//                Place.Field.ADDRESS,
//                Place.Field.PHONE_NUMBER,
//                Place.Field.OPENING_HOURS,
//                Place.Field.PHOTO_METADATAS,
//                Place.Field.RATING
//        );
//    }
}
