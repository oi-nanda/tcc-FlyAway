package com.example.myapplicationflyaway.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Model.Upload;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class DownloadCoverDayActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String itineraryId, dayname;
    private ImageView dayImageView;
    private FloatingActionButton selectImage, uploadImage;
    private StorageReference storageItineraryPicsRef;
    private DatabaseReference databaseReference;
    private Uri imageUri;
    private StorageReference fileRef;

    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_cover_day);
        itineraryId = getIntent().getExtras().getString("ItineraryId");
        dayname = getIntent().getExtras().getString("DayName");

         fileRef = storageItineraryPicsRef.child("Cover Days").child(mAuth.getCurrentUser().getUid()).child(itineraryId).child(dayname);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Itineraries");
        storageItineraryPicsRef = FirebaseStorage.getInstance().getReference();
        dayImageView = findViewById(R.id.edt_day_cover_pic);

        uploadImage = findViewById(R.id.floatingActionButton_confirm_edit_day_pic);
        selectImage = findViewById(R.id.floatingActionButton_edit_day_pic);
        databaseReference.child(mAuth.getCurrentUser().getUid()).child(itineraryId).child("Days").child(dayname);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    imageUri = data.getData();
                    dayImageView.setImageURI(imageUri);
                }
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri != null){
                    uploadFirebase(imageUri);
                }
            }
        });

    }
    private void uploadFirebase(Uri imageUri) {

        final StorageReference imageReference = fileRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl= uri.toString();
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("imgDay", uri.toString());

                        String key = databaseReference.push().getKey();
                        databaseReference.child(key).setValue(userMap);
                        Log.d("aaaa", "deu certo");
//                        Intent intent = new Intent(DownloadCoverDayActivity.this, DayPageActivity.class);
//                        intent.putExtra("ItineraryId", itineraryId);
//                        intent.putExtra("DayName", dayname);
//                        startActivity(intent);
//                        finish();
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


    private void uploadItineraryImage(Uri imageUri) {

        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(DownloadCoverDayActivity.this, "Imagem alterada com sucesso", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(DownloadCoverDayActivity.this, DayPageActivity.class);
                i.putExtra("ItineraryId",itineraryId);
                i.putExtra("DayName", dayname);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DownloadCoverDayActivity.this, "Erro ao adicionar capa", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    task.getResult().getStorage().getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl= uri.toString();
                                    HashMap<String, Object> userMap = new HashMap<>();
                                    userMap.put("imgDay", downloadUrl);

                                }
                            });
                }
            }
        });

    }
}