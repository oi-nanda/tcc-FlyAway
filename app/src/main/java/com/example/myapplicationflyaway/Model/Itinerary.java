package com.example.myapplicationflyaway.Model;

import android.media.Image;
import android.widget.ImageView;

import java.io.Serializable;

public class Itinerary implements Serializable {

    String placeName, inicialDate, finalDate, numberOfTravelers, description, userId, id;
    Day day[];

    ImageView img;

    public Itinerary() {}

    public Itinerary(String id, String placeName, String inicialDate, String finalDate, String numberOfTravelers, String description,ImageView img) {
        this.placeName = placeName;
        this.id = id;
        this.inicialDate = inicialDate;
        this.finalDate = finalDate;
        this.numberOfTravelers = numberOfTravelers;
        this.description = description;
        this.img = img;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Day[] getDay() {
        return day;
    }

    public void setDay(Day[] day) {
        this.day = day;
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
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

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNumberOfTravelers(String numberOfTravelers) {
        this.numberOfTravelers = numberOfTravelers;
    }
}
