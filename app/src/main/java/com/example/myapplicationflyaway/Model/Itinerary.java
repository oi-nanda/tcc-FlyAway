package com.example.myapplicationflyaway.Model;

import android.media.Image;
import android.widget.ImageView;

public class Itinerary {

    String placeName, inicialDate, finalDate, numberOfTravelers, description;
    Day day[];

    ImageView img;

    public Itinerary() {}

    public Itinerary(String placeName, String inicialDate, String finalDate, String numberOfTravelers, String description, Day[] day, ImageView img) {
        this.placeName = placeName;
        this.inicialDate = inicialDate;
        this.finalDate = finalDate;
        this.numberOfTravelers = numberOfTravelers;
        this.description = description;
        this.day = day;
        this.img = img;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getInicialDate() {
        return inicialDate;
    }

    public void setInicialDate(String inicialDate) {
        this.inicialDate = inicialDate;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public String getNumberOfTravelers() {
        return numberOfTravelers;
    }

    public void setNumberOfTravelers(String numberOfTravelers) {
        this.numberOfTravelers = numberOfTravelers;
    }
}
