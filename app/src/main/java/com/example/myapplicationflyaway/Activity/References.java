package com.example.myapplicationflyaway.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplicationflyaway.R;

public class References extends AppCompatActivity {

    TextView ref1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_references);

        String r1 = "<a href=\"https://br.freepik.com/vetores-gratis/homem-em-aventura-de-ferias-turismo-internacional-turismo-mundial-programa-de-intercambio-estudantil-turista-com-mochila-viajando-para-o-exterior_12145592.htm#&position=0&from_view=search&track=ais&uuid=560ea7b5-ea11-4d21-9004-41e2ebf448eb\">Imagem de vectorjuice</a> no Freepik";
        ref1.setText(r1);
    }
}