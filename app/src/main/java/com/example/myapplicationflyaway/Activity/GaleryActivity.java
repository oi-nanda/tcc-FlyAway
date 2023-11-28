package com.example.myapplicationflyaway.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Model.Upload;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class GaleryActivity extends AppCompatActivity {

    ImageView pickImage;
    ImageView image_selected;
    EditText add_description_image;
    Uri imageUri;
    ArrayList<Uri> chooseImageList;
    ArrayList<String> UrlsList;
    AppCompatButton btn_add_photos;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    ActivityResultLauncher<String> mGetContent;
    String itineraryId;
    ImageButton btn_back;
    FirebaseDatabase databaseReferenceGalery;
    DatabaseReference galeria;
    String idImage;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galery);
        pickImage = findViewById(R.id.Choose_image);
        image_selected = findViewById(R.id.image_selected);
        btn_add_photos = findViewById(R.id.btn_add_photos);
        chooseImageList = new ArrayList<>();
        UrlsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        add_description_image = findViewById(R.id.add_description_image);
        storageReference = FirebaseStorage.getInstance().getReference();
        btn_back = findViewById(R.id.btn_back_from_galery);
        itineraryId = getIntent().getExtras().getString("itineraryId");
        databaseReferenceGalery = FirebaseDatabase.getInstance();
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mGetContent.launch("image/*");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(GaleryActivity.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);

                //uri = result

                imageUri = result;
            }
        });
        btn_add_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadGaleryImage(imageUri);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GaleryActivity.this, GaleryPhotosActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1 && requestCode== 101){
            String result = data.getStringExtra("RESULT");
            Uri resulturi= null;
            if(result!=null){
                resulturi=Uri.parse(result);
            }
            image_selected.setImageURI(resulturi);
        }

    }


    private void uploadGaleryImage(Uri imageUri) {

        StorageReference fileRef = storageReference.child("Galery Pics")
                .child(mAuth.getCurrentUser().getUid());

        if(imageUri != null && add_description_image.getText() != null){

           // uri = imageUri;
            Log.d("imageUri", String.valueOf(imageUri));
            Log.d("uri", String.valueOf(uri));
            fileRef.child(itineraryId).child(System.currentTimeMillis() + "." + getFileExtention(imageUri)).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(GaleryActivity.this, "Imagem adicionada com sucesso", Toast.LENGTH_SHORT).show();

                    AddOnDataBase();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GaleryActivity.this, "Erro ao fazer upload de imagem", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(GaleryActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();

        }


    }

    private void AddOnDataBase() {

        galeria = databaseReferenceGalery.getReference("Galery");
        String downloadUrl = uri.toString();
        String desc = add_description_image.getText().toString();
        String user = mAuth.getCurrentUser().getUid();

        Upload upload = new Upload(desc,downloadUrl);

       String id = UUID.randomUUID().toString();


        galeria.child(user).child(itineraryId).child(id).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(GaleryActivity.this, GaleryPhotosActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String getFileExtention(Uri imageUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));

    }


}