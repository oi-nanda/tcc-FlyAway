package com.example.myapplicationflyaway.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplicationflyaway.Adapter.ViewPagerAdapter;
import com.example.myapplicationflyaway.Components.OpenSlidesLayout;
import com.example.myapplicationflyaway.R;

public class FinancesActivity extends AppCompatActivity {
    ViewPager nSliderViewPager;
    LinearLayout nDotLayout;
    ImageView backbtn, btnadd, nextbtn;


    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slides_finances);
        backbtn = findViewById(R.id.imageView25);
        nextbtn = findViewById(R.id.imageView24);
        btnadd = findViewById(R.id.imageView23);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(getItem(0)>0 ){
                    nSliderViewPager.setCurrentItem(getItem(-1), true);
                }


            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(getItem(0)<3){
                    nSliderViewPager.setCurrentItem(getItem(1), true);

                } else{
                }

            }
        });

        btnadd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(FinancesActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


//        nSliderViewPager = (ViewPager) findViewById(R.id.containerComponents);
//        nDotLayout = (LinearLayout) findViewById(R.id.indicatorLayout);
//
//        viewPagerAdapter = new ViewPagerAdapter(this);
//
//        nSliderViewPager.setAdapter(viewPagerAdapter);
//
//        setUpindicator(0);
//        nSliderViewPager.addOnPageChangeListener(viewListener);

    }
    public void setUpindicator(int position){

        dots = new TextView[4];
        nDotLayout.removeAllViews();

        for(int i =0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.black, getApplicationContext().getTheme()));
            nDotLayout.addView(dots[i]);

        }

        ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setUpindicator(position);

                if(position==0){
                    backbtn.setVisibility(View.INVISIBLE);
                }
                else {
                    backbtn.setVisibility(View.VISIBLE);
                }
//                if(position==3){
//                    nextbtn.setText("EMBARCAR!");
//                    skipbtn.setVisibility(View.INVISIBLE);
//                }
//                else {
//                    nextbtn.setText("PRÓXIMO");
//                    skipbtn.setVisibility(View.VISIBLE);
//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        dots[position].setTextColor(getResources().getColor(R.color.lighteryellow, getApplicationContext().getTheme()));

    }



    private int getItem(int i ){
        return nSliderViewPager.getCurrentItem() + i;
    }
}