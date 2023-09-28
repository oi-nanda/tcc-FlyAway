package com.example.myapplicationflyaway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FirstPageActivity extends AppCompatActivity {

    private TextView go_to_create_account;
    private Button go_to_main_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);


        go_to_create_account = findViewById(R.id.text_click_here);
        go_to_main_page = findViewById(R.id.button_login);
        go_to_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        go_to_main_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageActivity.this, MainSearchPageActivity.class);
                startActivity(intent);
            }
        });

    }



}