package com.example.myapplicationflyaway.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplicationflyaway.Activity.LoginActivity;
import com.example.myapplicationflyaway.Model.Upload;
import com.example.myapplicationflyaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GaleryAdapter extends BaseAdapter {

   ArrayList<Upload> photosList;
   Context context;
    private FirebaseAuth mAuth;
    String userId;
   LayoutInflater inflater;
    public GaleryAdapter(Context context,ArrayList<Upload> photosList) {
        this.photosList = photosList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return photosList.size();
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
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.photos_galery, null);
        }

        ImageView imageView = convertView.findViewById(R.id.photo);
        TextView textView = convertView.findViewById(R.id.photo_desc);

        Glide.with(context).load(photosList.get(position).getmImageUrl()).into(imageView);
        textView.setText(photosList.get(position).getmName());


        return convertView;
    }


}
