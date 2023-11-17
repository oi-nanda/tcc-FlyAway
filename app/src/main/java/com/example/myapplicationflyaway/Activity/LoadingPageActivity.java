package com.example.myapplicationflyaway.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myapplicationflyaway.Components.OpenSlidesLayout;
import com.example.myapplicationflyaway.MainActivity;
import com.example.myapplicationflyaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoadingPageActivity extends AppCompatActivity {

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(LoadingPageActivity.this, OpenSlidesLayout.class));
                    finish();
                }
            }, 2000);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
