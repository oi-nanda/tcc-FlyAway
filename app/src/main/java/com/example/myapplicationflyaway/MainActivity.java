package com.example.myapplicationflyaway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myapplicationflyaway.Activity.CreateItineraryActivity;
import com.example.myapplicationflyaway.Activity.LoginActivity;
import com.example.myapplicationflyaway.Fragments.HomeFragment;
import com.example.myapplicationflyaway.Fragments.ProfileFragment;
import com.example.myapplicationflyaway.Fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    Dialog myDialog;
    public static int home_footer = 1000052;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            Intent i = new Intent(getApplicationContext(), CreateItineraryActivity.class);
            startActivity(i);
            finish();
        });
    }



    public boolean onClickGoToHome(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, HomeFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
        return true;
    }

    private boolean onClickGoToProfile(MenuItem menuItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, ProfileFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
        return true;
    }
    private boolean onClickGoToSettings(MenuItem menuItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, SettingsFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
        return true;
    }
    private boolean onClickExit(MenuItem menuItem) {
        ShowPopup(view);
        return true;
    }

    public void ShowPopup(View v){
        Button confirm, cancel;

        myDialog.setContentView(R.layout.exit_popup);
        confirm = myDialog.findViewById(R.id.button_confirm);
        cancel = myDialog.findViewById(R.id.button_cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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

}