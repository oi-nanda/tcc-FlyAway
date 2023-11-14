package com.example.myapplicationflyaway.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplicationflyaway.Model.Users;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class EditProfileInfoFragment extends Fragment {

    View view;

    ImageView btn_back_profile;
    EditText username, email, bio, phone, location;
    Button save;
    LinearLayout btn_conf;
    private DatabaseReference dbReference;
    private FirebaseAuth auth;


    public EditProfileInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     view = inflater.inflate(R.layout.fragment_edit_profile_info, container, false);

        btn_conf = view.findViewById(R.id.btn_configuracaoPage);
        username = view.findViewById(R.id.edit_profile_info_username);
        bio = view.findViewById(R.id.edit_profile_info_bio);
        phone = view.findViewById(R.id.edit_profile_info_telefone);
        location = view.findViewById(R.id.edit_profile_info_localizacao);
        save = view.findViewById(R.id.button_edit_info_profile);

        btn_back_profile =view.findViewById(R.id.btn_back_profile);

        auth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users");



        btn_back_profile.setOnClickListener(new View.OnClickListener() {
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterProfileInformations();
            }
        });

        return view;
    }

    private void alterProfileInformations() {
        dbReference.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> userMap = new HashMap<>();
                if(!username.getText().toString().isEmpty()){
                    userMap.put("username", username.getText().toString());
                }
                if(!bio.getText().toString().isEmpty()){
                    userMap.put("bio", bio.getText().toString());
                }
                if(!phone.getText().toString().isEmpty()){
                    userMap.put("telefone", phone.getText().toString());
                }
                if(!location.getText().toString().isEmpty()){
                    userMap.put("localizacao", location.getText().toString());
                }

                dbReference.child(auth.getCurrentUser().getUid()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        username.setText("");
                        bio.setText("");
                        phone.setText("");
                        location.setText("");
                    }

                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FragmentManager fragmentManager = getParentFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_fragment, ProfileFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("name")
                                .commit();

                        Toast.makeText(getContext(), "Alterado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
}