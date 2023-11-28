package com.example.myapplicationflyaway.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Activity.CropperActivity;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileImageFragment extends Fragment {

   View view;
   private CircleImageView profileImageView;
   private FloatingActionButton selectImage, uploadImage;

   private DatabaseReference databaseReference;
   private FirebaseAuth mAuth;

   private ImageView btn_back_profilePage;


   private Uri imageUri;
   private StorageReference storageProfilePicsRef;
   ActivityResultLauncher<String> mGetContent;

    public EditProfileImageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_edit_image_profile, container, false);

       mAuth = FirebaseAuth.getInstance();
       databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference();
       profileImageView = view.findViewById(R.id.edt_profile_pic);


       btn_back_profilePage = view.findViewById(R.id.back_profilePage);

       uploadImage = view.findViewById(R.id.floatingActionButton_confirm_edit_profile_pic);
       selectImage = view.findViewById(R.id.floatingActionButton_edit_profile_pic);

       btn_back_profilePage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FragmentManager fragmentManager = getParentFragmentManager();
               fragmentManager.beginTransaction()
                       .replace(R.id.main_fragment, ProfileFragment.class, null)
                       .setReorderingAllowed(true)
                       .addToBackStack("name")
                       .commit();
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
                Intent intent = new Intent(getContext(), CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
                imageUri = result;
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage(imageUri);
            }
        });

        return view;
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

            profileImageView.setImageURI(resulturi);
        }

    }
    private void uploadProfileImage(Uri imageUri) {
        StorageReference fileRef = storageProfilePicsRef.child("Profile Pic")
                .child(mAuth.getCurrentUser().getUid() + ".jpg");

        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Imagem alterada com sucesso", Toast.LENGTH_SHORT).show();

                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_fragment, ProfileFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("erro", String.valueOf(e));
                    Toast.makeText(getContext(), "Erro ao alterar imagem", Toast.LENGTH_SHORT).show();
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
                                    userMap.put("image", downloadUrl);

                                    databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
                                }
                            });
                }
            }
        });


    }


}