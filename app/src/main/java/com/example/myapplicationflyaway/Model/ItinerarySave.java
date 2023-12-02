package com.example.myapplicationflyaway.Model;

import java.io.Serializable;

public class ItinerarySave implements Serializable {
    String placeName, inicialDate, finalDate, mberOfTravelers, userId, id;

    public ItinerarySave(){}

    public ItinerarySave(String id, String placeName, String inicialDate, String finalDate, String mberOfTravelers, String userId) {
        this.placeName = placeName;
        this.inicialDate = inicialDate;
        this.finalDate = finalDate;
        this.mberOfTravelers = mberOfTravelers;
        this.userId = userId;
        this.id = id;
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

    public String getMberOfTravelers() {
        return mberOfTravelers;
    }

    public void setMberOfTravelers(String mberOfTravelers) {
        this.mberOfTravelers = mberOfTravelers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
