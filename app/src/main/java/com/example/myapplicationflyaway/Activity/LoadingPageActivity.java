package com.example.myapplicationflyaway.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myapplicationflyaway.Components.OpenSlidesLayout;
import com.example.myapplicationflyaway.R;

public class LoadingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadingPageActivity.this, OpenSlidesLayout.class));
                finish();
            }
        }, 2000);

    }
}
