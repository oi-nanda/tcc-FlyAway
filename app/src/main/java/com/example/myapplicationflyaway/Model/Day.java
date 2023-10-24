package com.example.myapplicationflyaway.Model;

import android.media.Image;

public class Day {
    String placeName[], description[], day;
    Image imgDay[];

    public Day(String[] placeName, String[] description, String day, Image[] imgDay) {
        this.placeName = placeName;
        this.description = description;
        this.day = day;
        this.imgDay = imgDay;
    }

    public String[] getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String[] placeName) {
        this.placeName = placeName;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public Image[] getImgDay() {
        return imgDay;
    }

    public void setImgDay(Image[] imgDay) {
        this.imgDay = imgDay;
    }
}
