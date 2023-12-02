package com.example.myapplicationflyaway.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    DatabaseReference databaseReferenceGalery;
    DatabaseReference galeria;
    String idImage;
    Uri uri;
    String id = UUID.randomUUID().toString();

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
        itineraryId = getIntent().getExtras().getString("itineraryId");
        String user = mAuth.getCurrentUser().getUid();
        btn_back = findViewById(R.id.btn_back_from_galery);

        storageReference = FirebaseStorage.getInstance().getReference().child("Galery Pics").child(mAuth.getCurrentUser().getUid()).child(itineraryId);


        databaseReferenceGalery = FirebaseDatabase.getInstance().getReference("Galery").child(user).child(itineraryId);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
               if(result.getResultCode() == Activity.RESULT_OK){
                   Intent data = result.getData();
                   imageUri = data.getData();
                   image_selected.setImageURI(imageUri);
               }
            }
        });

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });


        btn_add_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri != null){
                    uploadFirebase(imageUri);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GaleryActivity.this, GaleryPhotosActivity.class);
                intent.putExtra("itineraryId", itineraryId);
                startActivity(intent);
            }
        });

    }

    private void uploadFirebase(Uri imageUri) {
        String caption = add_description_image.getText().toString();
        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Upload upload = new Upload(caption, uri.toString());
                        String key = databaseReferenceGalery.push().getKey();
                        databaseReferenceGalery.child(key).setValue(upload);

                        Intent intent = new Intent(GaleryActivity.this, GaleryPhotosActivity.class);
                        intent.putExtra("itineraryId", itineraryId);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

}