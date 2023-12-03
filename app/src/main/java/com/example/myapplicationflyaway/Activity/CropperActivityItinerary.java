package com.example.myapplicationflyaway.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.myapplicationflyaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class CropperActivityItinerary extends AppCompatActivity {
    String result;
    Uri fileUri;
    private FirebaseAuth mAuth;
    String itineraryId;

    DatabaseReference  databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper_itinerary);
        mAuth = FirebaseAuth.getInstance();
        readIntent();

        itineraryId = getIntent().getExtras().getString("ItineraryId");
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Itineraries").child(mAuth.getCurrentUser().getUid())
//                .child(itineraryId).child("ItineraryPic");

        String dest_uri = new StringBuilder(itineraryId).append(".jpg").toString();


        UCrop.Options options = new UCrop.Options();

        UCrop.of(fileUri, Uri.fromFile(new File(getCacheDir(), dest_uri)))
                .withOptions(options)
                .withAspectRatio(0,0)
                .useSourceImageAspectRatio()
                .withMaxResultSize(2000,2000)
                .start(CropperActivityItinerary.this);
    }

    private void readIntent() {
        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            result = intent.getStringExtra("DATA");
            fileUri = Uri.parse(result);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode==UCrop.REQUEST_CROP){
            final Uri resultUri = UCrop.getOutput(data);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("RESULT", resultUri+"");
            setResult(-1, returnIntent);
            finish();
        }else if(resultCode==UCrop.RESULT_ERROR){
            final Throwable cropError = UCrop.getError(data);            }
    }

}