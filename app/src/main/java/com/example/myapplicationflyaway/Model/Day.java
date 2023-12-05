package com.example.myapplicationflyaway.Model;

import android.media.Image;

public class Day {
    String dayname, description, id;
    Image imgDay;
    String date;

    String itineraryId;
    String valorv;

    public Day(String dayname, String description, String id, Image imgDay, String itineraryId, String date, String valorv) {
        this.dayname = dayname;
        this.description = description;
        this.id = id;
        this.imgDay = imgDay;
        this.itineraryId = itineraryId;
        this.date = date;
        this.valorv = valorv;
    }

    public String getValorv() {
        return valorv;
    }

    public void setValor(String valorv) {
        this.valorv = valorv;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(String itineraryId) {
        this.itineraryId = itineraryId;
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
