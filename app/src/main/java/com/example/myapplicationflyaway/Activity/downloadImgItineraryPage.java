package com.example.myapplicationflyaway.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Fragments.ProfileFragment;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class downloadImgItineraryPage extends AppCompatActivity {

    View view;
    private ImageView itineraryImageView;
    private FloatingActionButton selectImage, uploadImage;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private ImageView btn_back_itineraryPage;
    private String itineraryId, userId;
    private Uri imageUri;
    private StorageReference storageItineraryPicsRef;
    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_img_itinerary_page);
        itineraryId = getIntent().getExtras().getString("ItineraryId");
        userId = getIntent().getExtras().getString("UserId");

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Itineraries");
        storageItineraryPicsRef = FirebaseStorage.getInstance().getReference();
        itineraryImageView = findViewById(R.id.edt_itinerary_cover_pic);

        btn_back_itineraryPage = findViewById(R.id.back_itineraryPage_fromEditPic);

        uploadImage = findViewById(R.id.floatingActionButton_confirm_edit_itinerary_pic);
        selectImage = findViewById(R.id.floatingActionButton_edit_itinerary_pic);

        btn_back_itineraryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(downloadImgItineraryPage.this, ItineraryPageActivity.class);
                i.putExtra("ItineraryId",itineraryId);
                i.putExtra("UserId", userId);
                startActivity(i);
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(downloadImgItineraryPage.this, CropperActivityItinerary.class);
                intent.putExtra("DATA", result.toString());
                intent.putExtra("ItineraryId",itineraryId);
                startActivityForResult(intent, 101);
                imageUri = result;
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadItineraryImage(imageUri);
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
            itineraryImageView.setImageURI(resulturi);
        }


    }

    private void uploadItineraryImage(Uri imageUri) {
        StorageReference fileRef = storageItineraryPicsRef.child("Cover Itineraries").child(mAuth.getCurrentUser().getUid()).child(itineraryId + ".jpg");

        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(downloadImgItineraryPage.this, "Imagem alterada com sucesso", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(downloadImgItineraryPage.this, ItineraryPageActivity.class);
                i.putExtra("ItineraryId",itineraryId);
                i.putExtra("UserId", userId);
                startActivity(i);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(downloadImgItineraryPage.this, "Erro ao alterar imagem", Toast.LENGTH_SHORT).show();
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
                                    userMap.put("img", downloadUrl);

                                    databaseReference.child(mAuth.getCurrentUser().getUid()).child(itineraryId).updateChildren(userMap);
                                }
                            });
                }
            }
        });


    }
}