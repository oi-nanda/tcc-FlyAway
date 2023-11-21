package com.example.myapplicationflyaway.Model;

import java.time.LocalDateTime;

public class Hourly {

    private String time;
    private String description;
    private int temp;
    private String pic;

    public Hourly(String time, String description, int temp, String pic) {
        this.time = time;
        this.description = description;
        this.temp = temp;
        this.pic = pic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
