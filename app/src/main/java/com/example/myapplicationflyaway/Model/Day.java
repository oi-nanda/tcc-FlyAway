package com.example.myapplicationflyaway.Model;

import android.media.Image;

public class Day {
    String dayname, description, id;
    Image imgDay;

    public Day(String dayname, String description, String id, Image imgDay) {
        this.dayname = dayname;
        this.description = description;
        this.id = id;
        this.imgDay = imgDay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Day(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDayname() {
        return dayname;
    }

    public void setDayname(String dayname) {
        this.dayname = dayname;
    }

    public Image getImgDay() {
        return imgDay;
    }

    public void setImgDay(Image imgDay) {
        this.imgDay = imgDay;
    }
}
