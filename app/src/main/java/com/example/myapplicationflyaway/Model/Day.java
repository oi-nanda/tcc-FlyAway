package com.example.myapplicationflyaway.Model;

import android.media.Image;

public class Day {
    String dayname, description;
    Image imgDay;

    public Day(String dayname, String description,Image imgDay) {
        this.dayname = dayname;
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
