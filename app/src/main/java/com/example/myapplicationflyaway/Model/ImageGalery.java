package com.example.myapplicationflyaway.Model;

import java.util.ArrayList;

public class ImageGalery {
    private String itemDesc, itemId;
    private ArrayList<String> imageUrl;

    public ImageGalery() {
    }

    public ImageGalery(String itemDesc, String itemId, ArrayList<String> imageUrl) {
        this.itemDesc = itemDesc;
        this.itemId = itemId;
        this.imageUrl = imageUrl;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }
}
