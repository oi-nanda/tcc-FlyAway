package com.example.myapplicationflyaway.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplicationflyaway.Adapter.HourlyAdapter;
import com.example.myapplicationflyaway.Fragments.ProfileFragment;
import com.example.myapplicationflyaway.Model.Hourly;
import com.example.myapplicationflyaway.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ClimaActivity extends AppCompatActivity {

    String stCity, eiCountry;
    TextView tvResult;

    Button next_days;
    private HourlyAdapter hourlyAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Hourly> hourlyList;
    String idItinerary;

    Hourly hourly;
    ImageButton btn_back_itinerary;
    ImageView imagem_clima;
    TextView description_weather,place_weather,weather_temp,max_min_weather,rain_weather, wind_weather, humididty_weather, feelslike_weather;




    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String urlForecast = "https://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "40c25d052d9c49b65c27f00af4d65a6f";
    DecimalFormat df = new DecimalFormat("#.##");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);
        idItinerary = getIntent().getExtras().getString("ItineraryId");

        description_weather = findViewById(R.id.description_weather);
        place_weather = findViewById(R.id.name_place_weather);
        weather_temp = findViewById(R.id.weather_temp);
        feelslike_weather = findViewById(R.id.feelslike_weather);
        max_min_weather = findViewById(R.id.max_min_weather);
        rain_weather = findViewById(R.id.rain_weather);
        wind_weather = findViewById(R.id.wind_weather);
        humididty_weather = findViewById(R.id.humididty_weather);
        imagem_clima = findViewById(R.id.imagem_clima);
        btn_back_itinerary = findViewById(R.id.btn_back_itinerary);


        recyclerView = findViewById(R.id.hourly_viewholder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ClimaActivity.this));

        hourlyList = new ArrayList<Hourly>();

        String tempUrl = "";
        String tempFiveDaysUrl = "";
        String city = "roma";
        String country = "italia";

        if(city.equals("")){

            Toast.makeText(ClimaActivity.this, "Não foi possivel encontrar o clima para a cidade informada", Toast.LENGTH_LONG).show();
        }else{
            if(!country.equals("")){
                tempUrl = url + "?q=" + city + "," + country  +"&lang=pt_br" +"&appid=" + appid;
                tempFiveDaysUrl = urlForecast + "?q=" + city + "," + country + "&cnt=" + 5 + "&lang=pt_br" + "&appid=" + appid;
            }else{
                tempUrl = url + "?q=" + city  +"&lang=pt_br"  + "&appid=" + appid;
                tempFiveDaysUrl = urlForecast + "?q=" + city + "&cnt="+ 5 + "&lang=pt_br" +"&appid=" + appid;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                String output = "";
                try{
                    JSONObject jsonResponse = new JSONObject(response);

                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");

                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double tempMax = jsonObjectMain.getDouble("temp_max") - 273.15;
                    double tempMin = jsonObjectMain.getDouble("temp_min") - 273.15;
                    double feelslike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");

                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    float wind = jsonObjectWind.getInt("speed");

                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");

                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");

                    String cityName = jsonResponse.getString("name");


                    int a = (int) temp;
                    int b = (int) feelslike;
                    int c = (int) tempMax;
                    int d = (int) tempMin;
                    float mult = (float) (wind * 3.6);

                    if(description.contains("chuva") && !description.contains("tempestade")){

                        imagem_clima.setImageResource(R.drawable.heavy_rain);
                    }
                    if(description.contains("tempestade")){

                        imagem_clima.setImageResource(R.drawable.storm);
                    }
                    if(description.contains("chuvisco")){

                        imagem_clima.setImageResource(R.drawable.garoa);
                    }
                    if(description.contains("neve") || description.contains("granizo")){

                        imagem_clima.setImageResource(R.drawable.freezing_rain);
                    }
                    if(description.contains("limpo") ){

                        imagem_clima.setImageResource(R.drawable.clear_sky);
                    }
                    if(description.contains("nuvens") ){
                        imagem_clima.setImageResource(R.drawable.sol);
                    }
                    description_weather.setText(description);
                    weather_temp.setText(a + "°C");
                    place_weather.setText(cityName + " | " + countryName );
                    feelslike_weather.setText("Sensação de " + b+ "°C");
                    max_min_weather.setText("Máx: " + c + "° " + "Min: " +d + "°");
                    rain_weather.setText(clouds + "%");
                    humididty_weather.setText(humidity + "%");
                    wind_weather.setText(mult + "km/h");

                } catch (JSONException e){
                    e.printStackTrace();
                }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });

            StringRequest stringRequestFutureWeather = new StringRequest(Request.Method.POST, tempFiveDaysUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                       JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("list");
                        String pic = "";
                        for(int i = 0; i<5; i++) {
                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(i);
                            JSONArray jsonArrayWeather = jsonObjectWeather.getJSONArray("weather");
                            JSONObject jsonObject = jsonArrayWeather.getJSONObject(0);
                            String description = jsonObject.getString("description");
                            String time = jsonObjectWeather.getString("dt_txt");

                            JSONObject jsonObjectMain = jsonObjectWeather.getJSONObject("main");
                            double temp = jsonObjectMain.getDouble("temp") - 273.15;
                            int a = (int) temp;

                            if(description.contains("chuva") && !description.contains("tempestade")){

                                imagem_clima.setImageResource(R.drawable.heavy_rain);
                            }
                            if(description.contains("tempestade")){

                              pic = "tempestade";
                            }
                            if(description.contains("chuvisco")){

                                pic = "chuvisco";
                            }
                            if(description.contains("neve") || description.contains("granizo")){

                                pic = "neve";
                            }
                            if(description.contains("limpo") ){

                                pic = "limpo";
                            }
                            if(description.contains("nuvens") ){
                                pic = "nuvens";
                            }


                            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date data = formato.parse(time);
                            formato.applyPattern("HH:mm");
                            String dataFormatada = formato.format(data);


                            Hourly hourly1 = new Hourly(dataFormatada, description, a, pic);

                            hourlyList.add(hourly1);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(ClimaActivity.this, LinearLayoutManager.HORIZONTAL, false));

                    hourlyAdapter = new HourlyAdapter( ClimaActivity.this);
                    recyclerView.setAdapter(hourlyAdapter);
                    hourlyAdapter.setItemList(hourlyList);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueueDois = Volley.newRequestQueue(getApplicationContext());
            requestQueueDois.add(stringRequest);
            requestQueueDois.add(stringRequestFutureWeather);

        }


        btn_back_itinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ItineraryPageActivity.class);
                i.putExtra("ItineraryId",idItinerary);
                startActivity(i);
                finish();
            }
        });



    }

}

