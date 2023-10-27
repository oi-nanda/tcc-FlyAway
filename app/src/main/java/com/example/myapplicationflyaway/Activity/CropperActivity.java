package com.example.myapplicationflyaway.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationflyaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class CropperActivity extends AppCompatActivity {

    String result;
    Uri fileUri;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);
        mAuth = FirebaseAuth.getInstance();
        readIntent();

        String dest_uri = new StringBuilder(mAuth.getCurrentUser().getUid()).append(".jpg").toString();

        UCrop.Options options = new UCrop.Options();

        UCrop.of(fileUri,Uri.fromFile(new File(getCacheDir(), dest_uri)))
                .withOptions(options)
                .withAspectRatio(0,0)
                .useSourceImageAspectRatio()
                .withMaxResultSize(2000,2000)
                .start(CropperActivity.this);
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
