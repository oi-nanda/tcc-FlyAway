package com.example.myapplicationflyaway.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplicationflyaway.Adapter.MyItineraryAdapter;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.example.myapplicationflyaway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyitinerariesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private ArrayList<Itinerary> itineraryList;
    MyItineraryAdapter myAdapter;
    Itinerary itinerary;
    DatabaseReference reference;
    ConstraintLayout itinerary_card;
   View view;
   ImageView btn_back_profile;

    public MyitinerariesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        view = inflater.inflate(R.layout.fragment_myitineraries, container, false);
        btn_back_profile = view.findViewById(R.id.back_profilePage);

        recyclerView = view.findViewById(R.id.itineraries_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reference = FirebaseDatabase.getInstance().getReference().child("Itineraries").child(mAuth.getCurrentUser().getUid());

        itineraryList = new ArrayList<Itinerary>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Itinerary itinerary = snapshot1.getValue(Itinerary.class);
                    itineraryList.add(itinerary);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myAdapter = new MyItineraryAdapter(getContext(), itineraryList);
        recyclerView.setAdapter(myAdapter);

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



        return view;
    }
}