package com.example.myapplicationflyaway.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.denzcoskun.imageslider.constants.ScaleTypes;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    View view;

    GoogleSignInClient signInClient;
    GoogleSignInOptions gso;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageSlider imageSlider = view.findViewById(R.id.image_slider);

        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.piramides_maias, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.egito, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.cristo, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.peru, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.italia, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.india, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.china, ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
        return view;
    }
}