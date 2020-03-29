package com.example.cst2335final;

import android.graphics.Bitmap;

public class NASAImage {
    private String date;
    private String title;
    private String description;
    Bitmap image;
    private long id;

    public NASAImage(String date, String title, String description, Bitmap image){
        this.date = date;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return image;
    }

    public long getId() {
        return id;
    }
}
