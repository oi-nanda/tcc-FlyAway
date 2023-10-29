package com.example.myapplicationflyaway.Model;

import android.media.Image;

public class Day {
    String day, description;
    Image imgDay;

    public Day(String day, String description,Image imgDay) {
        this.day = day;
        this.description = description;
        this.imgDay = imgDay;
    }

    public Day(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Image getImgDay() {
        return imgDay;
    }

    public void setImgDay(Image imgDay) {
        this.imgDay = imgDay;
    }
}
