package com.example.myapplicationflyaway.Model;

import android.media.Image;

public class Place {

    String name, description;
    Double cost;
    String id;
    Image img;

    public void Place() {}

    public Place(String name, String description, Double cost, String id, Image img) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.id = id;
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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
