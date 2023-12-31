package com.example.myapplicationflyaway.Model;

import android.media.Image;

import java.io.Serializable;

public class Place implements Serializable {

    String name, description;
    String cost;
    String id,iddia,iditinerary;
    Image img;

    public Place() {}


    public Place(String name, String description, String cost, String id, Image img, String iddia, String iditinerary) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.id = id;
        this.img = img;
        this.iddia = iddia;
        this.iditinerary = iditinerary;
    }

    public String getIddia() {
        return iddia;
    }

    public void setIddia(String iddia) {
        this.iddia = iddia;
    }

    public String getIditinerary() {
        return iditinerary;
    }

    public void setIditinerary(String iditinerary) {
        this.iditinerary = iditinerary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
    public String getCost() {
        return cost;
    }
}
