package com.example.myapplicationflyaway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainSearchPageActivity extends AppCompatActivity {

    Dialog myDialog;
    public static int home_footer = 1000052;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search_page);
        myDialog = new Dialog(this);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        Menu menu = navigationView.getMenu();
        MenuItem menuItemHome = menu.findItem(R.id.home_footer);
        MenuItem menuItemProfile = menu.findItem(R.id.profile_footer);
        MenuItem menuItemSettings = menu.findItem(R.id.settings);
        MenuItem menuItemExit = menu.findItem(R.id.exit);
        menuItemHome.setOnMenuItemClickListener(this::onClickGoToHome);
        menuItemProfile.setOnMenuItemClickListener(this::onClickGoToProfile);
        menuItemSettings.setOnMenuItemClickListener(this::onClickGoToSettings);
        menuItemExit.setOnMenuItemClickListener(this::onClickExit);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent i = new Intent (MainSearchPageActivity.this,CreateTrip.class);
            startActivity(i);
            finish();
        });
    }
    public void ShowPopup(View v){
        Button confirm, cancel;

        myDialog.setContentView(R.layout.exit_popup);
        confirm = myDialog.findViewById(R.id.button_confirm);
        cancel = myDialog.findViewById(R.id.button_cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainSearchPageActivity.this, FirstPageActivity.class);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onClickGoToHome(@NonNull MenuItem item) {
        Toast toast = Toast.makeText(getApplicationContext(), "Você já está na Home Page", Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }
    public boolean onClickGoToProfile(@NonNull MenuItem item) {
        Intent intent = new Intent(MainSearchPageActivity.this, ProfilePageActivity.class);
        startActivity(intent);
        return true;
    }
    public boolean onClickGoToSettings(@NonNull MenuItem item) {
        Intent intent = new Intent(MainSearchPageActivity.this, SettingsActivity.class);
        startActivity(intent);
        return true;
    }
    public boolean onClickExit(@NonNull MenuItem item) {
        ShowPopup(view);
        return true;
    }

}