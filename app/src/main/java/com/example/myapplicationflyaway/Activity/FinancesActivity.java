package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.myapplicationflyaway.Adapter.ViewPagerAdapter;
import com.example.myapplicationflyaway.Components.OpenSlidesLayout;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FinancesActivity extends AppCompatActivity {
    ViewPager nSliderViewPager;
    LinearLayout nDotLayout;
    Button btn_criar, btnadd, nextbtn;
    String itineraryId;
    DatabaseReference reference;
    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;
    Button btn_hotel, btn_lazer, btn_comida, btn_voos;
    TextView valorcomida, valorlazer, valorhotel, valorvoos;
    int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);
        itineraryId = getIntent().getExtras().getString("ItineraryId");
        btn_comida = findViewById(R.id.alimentacaotxt);
        btn_lazer = findViewById(R.id.lazertxt);
        btn_hotel = findViewById(R.id.hoteltxt);
        btn_voos = findViewById(R.id.vootxt);
        valorcomida = findViewById(R.id.Alimentacao);
        valorlazer = findViewById(R.id.Lazer);
        valorhotel = findViewById(R.id.hotell);
        valorvoos = findViewById(R.id.Voos);

        reference = FirebaseDatabase.getInstance().getReference("Itineraries").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(itineraryId)
                .child("Finances");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child("voos").child("cost"
                ).exists()){valorvoos.setText("R$ 0,00");}
                else {
                    valorvoos.setText("R$ " + snapshot.child("voos").child("cost"
                    ).getValue().toString());
                }

                if (!snapshot.child("hotel").child("cost"
                ).exists()){valorhotel.setText("R$ 0,00");}
                else {
                    valorhotel.setText("R$ " + snapshot.child("hotel").child("cost"
                    ).getValue().toString());
                }

                if (!snapshot.child("lazer").child("cost"
                ).exists()){valorlazer.setText("R$ 0,00");}
                else {
                    valorlazer.setText("R$ " + snapshot.child("lazer").child("cost"
                    ).getValue().toString());
                }

                if (!snapshot.child("comida").child("cost"
                ).exists()){valorcomida.setText("R$ 0,00");}
                else {
                    valorcomida.setText("R$ " + snapshot.child("comida").child("cost"
                    ).getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_voos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup("voos");
            }
        });

        btn_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup("hotel");
            }
        });

        btn_comida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup("comida");
            }
        });

        btn_lazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup("lazer");
            }
        });



//
//        for (int i = 0; i < (int)Datasnapshot.getChildrenCount();i++) {}


    }

    public void popup(String var){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_question_edit_popup, null);
        Button btn = popupView.findViewById(R.id.button_edit_info_itinerary);
        ImageView close_popup = popupView.findViewById(R.id.imageView14);
        ImageView imgPadrao = popupView.findViewById(R.id.imageView13);
//        if (var.equals("voos")){
//            imgPadrao.setImageResource(R.drawable.img_17);
//        }
//        if (var.equals("lazer")){
//            imgPadrao.setImageResource(R.drawable.img_16);
//        }
//        if (var.equals("hotel")){
//            imgPadrao.setImageResource(R.drawable.img_18);
//        }
//        if (var.equals("comida")){
//            imgPadrao.setImageResource(R.drawable.img_14);
//        }

        EditText edt = popupView.findViewById(R.id.edit_description);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        FrameLayout tempLayout = new FrameLayout(getApplicationContext());
        tempLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(tempLayout, Gravity.CENTER, 0, 0);


        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edt.getText().toString().equals(null) || edt.getText().toString().isEmpty()){
                    popupWindow.dismiss();
                }
                else{
//                    valor.setText(edt.getText().toString());

                    if (var.equals("voos")) {
                        reference = FirebaseDatabase.getInstance().getReference("Itineraries").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(itineraryId)
                                .child("Finances").child("voos");
                        valorvoos.setText("R$ "+edt.getText().toString());
                    }

                    if (var.equals("hotel")) {
                        cont++;
                        reference = FirebaseDatabase.getInstance().getReference("Itineraries").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(itineraryId)
                                .child("Finances").child("hotel");
                        valorvoos.setText("R$ "+edt.getText().toString());
                    }

                    if (var.equals("comida")) {
                        cont++;
                        reference = FirebaseDatabase.getInstance().getReference("Itineraries").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(itineraryId)
                                .child("Finances").child("comida");
                        valorvoos.setText("R$ "+edt.getText().toString());
                    }

                    if (var.equals("lazer")) {
                        cont++;
                        reference = FirebaseDatabase.getInstance().getReference("Itineraries").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(itineraryId)
                                .child("Finances").child("lazer");
                        valorlazer.setText("R$ "+edt.getText().toString());
                    }
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String, Object> PlaceMap = new HashMap<>();
                            PlaceMap.put("cost",edt.getText().toString());
                            reference.updateChildren(PlaceMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    popupWindow.dismiss();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
//                            popupWindow.dismiss();
                        }
                    });
                }
                return false;
            }
        });

        close_popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



}