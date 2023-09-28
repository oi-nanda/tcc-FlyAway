package com.example.myapplicationflyaway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CreateAccountActivity extends AppCompatActivity {

    ImageView back_loginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        back_loginPage = findViewById(R.id.back_loginPage);
        back_loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, FirstPageActivity.class);
                startActivity(intent);
            }
        });

    }



}