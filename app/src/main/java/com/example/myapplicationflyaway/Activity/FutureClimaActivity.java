package com.example.myapplicationflyaway.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplicationflyaway.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FutureClimaActivity extends AppCompatActivity {

    ImageButton btn_back_climaPage;
    private final String url = "https://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "40c25d052d9c49b65c27f00af4d65a6f";
    //http://api.openweathermap.org/data/2.5/forecast/daily?q=london&units=metric&APPID=value&cnt=7

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.futureclima_activity);
//        String city = "canoas";
//        String country = "brazil";
//        String tempUrl = "";
//        tempUrl = url + "?q=" + city  +"&lang=pt_br" + "&cnt=7" + "&appid=" + appid;

        btn_back_climaPage = findViewById(R.id.btn_back_climaPage);

   //     String urlCompleta = url +"&lang=pt_br"+ "&cnt="+ 7 + "&appid=" + appid;
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("response", response);
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        RequestQueue requestQueueDois = Volley.newRequestQueue(getApplicationContext());
//        requestQueueDois.add(stringRequest);


        btn_back_climaPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ClimaActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}