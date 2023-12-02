package com.example.myapplicationflyaway.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplicationflyaway.Model.Notes;
import com.example.myapplicationflyaway.Model.Upload;
import com.example.myapplicationflyaway.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NotesAdapter extends BaseAdapter {

    ArrayList<Notes> notesArrayList;
    Context context;
    LayoutInflater inflater;

    public NotesAdapter(ArrayList<Notes> notesArrayList, Context context) {
        this.notesArrayList = notesArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(inflater == null){
            inflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.component_notes, null);
        }

        TextView title = convertView.findViewById(R.id.txt_title_notes);
        TextView subtitle = convertView.findViewById(R.id.txt_subtitle);
        TextView content = convertView.findViewById(R.id.txt_datetime);

        title.setText(notesArrayList.get(position).getTitle());
        subtitle.setText(notesArrayList.get(position).getSubtitle());
        content.setText(notesArrayList.get(position).getContent());


        return convertView;
    }
}
