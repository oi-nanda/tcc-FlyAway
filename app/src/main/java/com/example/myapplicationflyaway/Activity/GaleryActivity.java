package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Adapter.ViewPagerAdapter;
import com.example.myapplicationflyaway.Adapter.ViewgaleryAdapter;
import com.example.myapplicationflyaway.Fragments.ProfileFragment;
import com.example.myapplicationflyaway.Model.ImageGalery;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import io.grpc.Context;

public class GaleryActivity extends AppCompatActivity {

    ImageView pickImage;
    ViewPager viewPager;
    EditText add_description_image;
    Uri imageUri;
    ArrayList<Uri> chooseImageList;
    ArrayList<String> UrlsList;
    AppCompatButton btn_add_photos;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galery);
        pickImage = findViewById(R.id.Choose_image);
        viewPager = findViewById(R.id.view_pager);
        btn_add_photos = findViewById(R.id.btn_add_photos);
        chooseImageList = new ArrayList<>();
        UrlsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        add_description_image = findViewById(R.id.add_description_image);
        storageReference = FirebaseStorage.getInstance().getReference();


        btn_add_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uploadimages();
            }
        });
        
        
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckPermission();

                PickImageFromgallery();
            }
        });
    }

    private void Uploadimages() {

        for(int i=0; i<chooseImageList.size(); i++){
            Uri IndividualImage = chooseImageList.get(i);
            if(IndividualImage != null){
                StorageReference imageFolder = storageReference.child("Galery").child(mAuth.getCurrentUser().getUid());
                final StorageReference imageName = imageFolder.child("Image"+i+": "+ IndividualImage.getLastPathSegment());
                imageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(GaleryActivity.this, "Imagem adicionada com sucesso", Toast.LENGTH_SHORT).show();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GaleryActivity.this, "Erro ao fazer upload imagem", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }else{
                Toast.makeText(GaleryActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private void StorageLink(ArrayList<String> urlsList) {

        String desc = add_description_image.getText().toString();

        if(!TextUtils.isEmpty(desc) && imageUri != null){
            ImageGalery imageGalery = new ImageGalery(desc, "", UrlsList);
           // storageReference.child()
        }
    }

    private void CheckPermission() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(GaleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(GaleryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

            }else{
                PickImageFromgallery();
            }
        }else{
            PickImageFromgallery();
        }
    }

    private void PickImageFromgallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data != null && data.getClipData() != null){
            int count = data.getClipData().getItemCount();
            for(int i=0; i<count; i++){
                imageUri = data.getClipData().getItemAt(i).getUri();
                chooseImageList.add(imageUri);
                SetAdapter();
            }
        }
    }

    private void SetAdapter() {
        ViewgaleryAdapter adapter = new ViewgaleryAdapter(this, chooseImageList);
        viewPager.setAdapter(adapter);

    }
}