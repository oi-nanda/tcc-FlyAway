package com.example.myapplicationflyaway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class MeuCardRoteiroAdapter extends ArrayAdapter<Roteiro> {
    public MeuCardRoteiroAdapter(@NonNull Context context, int resource) {super(context, resource);}


    @NotNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext().getApplicationContext());
            convertView = inflater.inflate(R.layout.roteiro_card_item_layout, parent, false);

            TextView titulo = convertView.findViewById(R.id.title_card);
            TextView data = convertView.findViewById(R.id.date_card);
            TextView lugar = convertView.findViewById(R.id.place_card);
            ImageView salvo = convertView.findViewById(R.id.save_card);
            ImageView imagem_local = convertView.findViewById(R.id.image_card);

            titulo.setText(getItem(position).getTitulo());
            data.setText(getItem(position).getData());
            lugar.setText(getItem(position).getLugar());

            salvo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });




        }
        return convertView;
    }
}
