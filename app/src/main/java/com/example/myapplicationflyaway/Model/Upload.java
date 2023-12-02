package com.example.myapplicationflyaway.Model;

import android.net.Uri;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String idImage;
    private String idItinerary;
    private String idUser;


    public Upload() {
    }


    public Upload(String mName, String mImageUrl) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

}
