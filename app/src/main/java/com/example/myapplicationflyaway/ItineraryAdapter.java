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

import org.w3c.dom.Text;

import java.util.Date;

public class ItineraryAdapter extends ArrayAdapter<String> {


    public ItineraryAdapter(@NonNull Context context, int resource) {
        super(context, resource, R.id.trip_list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext().getApplicationContext());
            convertView = inflater.inflate(R.layout.roteiro_card_item_layout, parent, false);
            String campo1adapter = getItem( getPosition(String.valueOf(R.id.title_card)) );
            String campo2adapter = getItem( getPosition(String.valueOf(R.id.title_card)) );
            TextView campo1 = convertView.findViewById(R.id.title_card);
            campo1.setText(campo1adapter);
        }
        return convertView;
    }
}
