package com.example.myapplicationflyaway.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.myapplicationflyaway.Adapter.MySavesAdapter;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.example.myapplicationflyaway.Model.ItinerarySave;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;

    ArrayList<ItinerarySave> itineraryArrayList;
    MySavesAdapter savesAdapter;
    DatabaseReference reference, savesReference;
   SearchView searchView;
   ItinerarySave itinerarySave;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.itinerary_saveList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reference = FirebaseDatabase.getInstance().getReference().child("Itineraries");
        savesReference = FirebaseDatabase.getInstance().getReference().child("PublicItineraries");
        ImageSlider imageSlider = view.findViewById(R.id.image_slider);

        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               filterList(newText);
                return true;
            }
        });

        itineraryArrayList = new ArrayList<ItinerarySave>();

        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.piramides_maias, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.egito, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.cristo, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.peru, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.italia, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.india, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.china, ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);


        savesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ItinerarySave itinerary = snapshot1.getValue(ItinerarySave.class);
                   itineraryArrayList.add(itinerary);
                }
                savesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        savesAdapter = new MySavesAdapter(getContext(), itineraryArrayList);
        recyclerView.setAdapter(savesAdapter);

        return view;
    }

    private void filterList(String text) {
        ArrayList<ItinerarySave> filteredList = new ArrayList<>();
        for(ItinerarySave itinerarySave : itineraryArrayList){
            if(itinerarySave.getPlaceName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(itinerarySave);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(), "Nenhum roteiro foi encontrado", Toast.LENGTH_SHORT).show();
        }else{
            savesAdapter.setFilteredList(filteredList);
        }
    }
}