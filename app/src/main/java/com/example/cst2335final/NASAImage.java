package com.example.cst2335final;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class NASAImage implements Comparable<NASAImage>{
    private String date;
    private String title;
    private String description;
    Bitmap image;
    private long id;

    public NASAImage(long id, String title, String description, String date, Bitmap image){
        this.date = date;
        this.title = title;
        this.description = description;
        this.id = id;
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

    @Override
    public int compareTo(NASAImage img) {
        return date.compareTo(img.getDate());
    }
}
