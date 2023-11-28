package com.example.myapplicationflyaway.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplicationflyaway.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private TextView txt_username, txt_email, txt_email_profile, txt_telefone, txt_bio, txt_localizacao;
    View view;
    LinearLayout btn_goSettings, btn_myInineraries;
    DatabaseReference reference;
    FirebaseUser user;
    FloatingActionButton edt_profile_pic;
    String uid;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button button_edit_profile;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private CircleImageView profile_pic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        mAuth = FirebaseAuth.getInstance();

        btn_myInineraries = view.findViewById(R.id.btn_myItineraries);
        btn_goSettings = view.findViewById(R.id.btn_configuracaoPage);
        txt_username = view.findViewById(R.id.txt_username);
        txt_email = view.findViewById(R.id.txt_email);
        txt_telefone = view.findViewById(R.id.txt_telefone);
        txt_bio = view.findViewById(R.id.txt_bio);
        txt_localizacao = view.findViewById(R.id.txt_localizacao);
        button_edit_profile = view.findViewById(R.id.button_edit_profile);
        profile_pic = view.findViewById(R.id.profile_pic);

        edt_profile_pic = view.findViewById(R.id.edt_profile_pic);
        reference = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(getContext(), gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if(account!=null){
            String Name = account.getDisplayName();
            String Mail = account.getEmail();

            txt_username.setText(Name);
            txt_email.setText(Mail);

        }


        edt_profile_pic.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, EditProfileImageFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name")
                    .commit();
        });

        btn_goSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, SettingsFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });
        btn_myInineraries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, MyitinerariesFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        button_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, EditProfileInfoFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getUserinfo();
                txt_username.setText(snapshot.child("Users").child(uid).child("username").getValue().toString());
                txt_email.setText(snapshot.child("Users").child(uid).child("email").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void getUserinfo() {
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0)
                {

                    if (snapshot.hasChild("image")){
                       String image = snapshot.child("image").getValue().toString();
                       Picasso.get().load(image).into(profile_pic);
                    }
                    if (snapshot.hasChild("bio")){
                        String bio = snapshot.child("bio").getValue().toString();
                        txt_bio.setText(bio);
                    }
                    if (snapshot.hasChild("telefone")){
                        String telefone = snapshot.child("telefone").getValue().toString();
                        txt_telefone.setText(telefone);
                    }
                    if (snapshot.hasChild("localizacao")){
                        String localizacao = snapshot.child("localizacao").getValue().toString();
                        txt_localizacao.setText(localizacao);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}