package com.example.myapplicationflyaway.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.autofill.SaveRequest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplicationflyaway.Adapter.SavePageAdapter;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.example.myapplicationflyaway.Model.ItinerarySave;
import com.example.myapplicationflyaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class SavesPageFregment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private ArrayList<ItinerarySave> itinerarySaveList;
    SavePageAdapter adapter;
    DatabaseReference reference;
    View view;
    ImageView btn_back_profile;

    public SavesPageFregment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saves_page_fregment, container, false);
        mAuth = FirebaseAuth.getInstance();
        btn_back_profile = view.findViewById(R.id.back_profilePage);

        recyclerView = view.findViewById(R.id.save_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reference = FirebaseDatabase.getInstance().getReference().child("ItinerariesSave").child(mAuth.getCurrentUser().getUid());

        itinerarySaveList = new ArrayList<ItinerarySave>();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                  ItinerarySave itinerarySave = snapshot1.getValue(ItinerarySave.class);
                  itinerarySaveList.add(itinerarySave);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new SavePageAdapter(getContext(), itinerarySaveList);
        recyclerView.setAdapter(adapter);

        btn_back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, ProfileFragment.class, null)
                        .commit();
            }
        });

        return view;
    }
}