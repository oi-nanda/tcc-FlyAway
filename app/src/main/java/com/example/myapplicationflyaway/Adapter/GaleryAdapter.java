package com.example.myapplicationflyaway.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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

public class GaleryAdapter extends RecyclerView.Adapter<GaleryAdapter.ViewHolder>{

   ArrayList<Upload> photosList;
   Context context;

   String authId;
    private FirebaseAuth mAuth;
   DatabaseReference reference;
   StorageReference storageReference;

    public GaleryAdapter(Context context,ArrayList<Upload> photosList) {
        this.photosList = photosList;
        this.context = context;
    }

    @NonNull
    @Override
    public GaleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.photos_galery, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GaleryAdapter.ViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        Upload upload = photosList.get(position);





        holder.photo_desc.setText(upload.getmName());

        Uri resulturi= null;
        resulturi=Uri.parse(upload.getmImageUrl());

        holder.photo.setImageURI(resulturi);
    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView photo_desc;
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            photo_desc = itemView.findViewById(R.id.photo_desc);
            photo = itemView.findViewById(R.id.photo);
        }
    }


}
