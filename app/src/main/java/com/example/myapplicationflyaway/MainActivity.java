package com.example.myapplicationflyaway;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ViewPager nSliderViewPager;
    LinearLayout nDotLayout;
    Button backbtn, skipbtn, nextbtn;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backbtn = findViewById(R.id.buttonBack);
        nextbtn = findViewById(R.id.buttonNext);
        skipbtn = findViewById(R.id.buttonSkip);


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
                    Intent i = new Intent(MainActivity.this, FirstPageActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this, FirstPageActivity.class);
                startActivity(i);
                finish();
            }
        });



        nSliderViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        nDotLayout = (LinearLayout) findViewById(R.id.indicatorLayout);

        viewPagerAdapter = new ViewPagerAdapter(this);

        nSliderViewPager.setAdapter(viewPagerAdapter);

        setUpindicator(0);
        nSliderViewPager.addOnPageChangeListener(viewListener);
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

        dots[position].setTextColor(getResources().getColor(R.color.lighteryellow, getApplicationContext().getTheme()));

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
            if(position==3){
                nextbtn.setText("EMBARCAR!");
                skipbtn.setVisibility(View.INVISIBLE);
            }
            else {
                nextbtn.setText("PRÓXIMO");
                skipbtn.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem(int i ){
        return nSliderViewPager.getCurrentItem() + i;
    }

}