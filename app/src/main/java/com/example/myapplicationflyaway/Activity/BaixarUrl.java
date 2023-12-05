package com.example.myapplicationflyaway.Activity;

import android.util.Log;

import com.google.api.Http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaixarUrl {

public String rUrl(String url) throws IOException {
    String urlData = "";
    HttpURLConnection httpConnection = null;
    InputStream inputStream = null;

    try {
        URL url1 = new URL(url);
        httpConnection = (HttpURLConnection) url1.openConnection();
        httpConnection.connect();

        inputStream = httpConnection.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder strBuffer = new StringBuilder();

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            strBuffer.append(line);
        }

        urlData = strBuffer.toString();
        bufferedReader.close();

    } catch (Exception exception) {
        Log.d("Exception", exception.toString());
    } finally {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.d("InputStreamException", e.toString());
            }
        }
        if (httpConnection != null) {
            httpConnection.disconnect();
        }
    }
    return urlData;
}
}
