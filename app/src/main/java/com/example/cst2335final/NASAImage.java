package com.example.cst2335final;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * A simple class that holds all the parameters of a NASA image object
 */
public class NASAImage implements Comparable<NASAImage>{
    private String date;
    private String title;
    private String description;
    Bitmap image;
    private long id;

    /**
     * constructor initializing all the fields
     * @param id - the database id of this image
     * @param title - the title of this image
     * @param description - the explanation of the image
     * @param date - the date published to the nasa site
     * @param image - the bitmap of the image
     */
    public NASAImage(long id, String title, String description, String date, Bitmap image){
        this.date = date;
        this.title = title;
        this.description = description;
        this.id = id;
        this.image = image;
    }

    public NASAImage(String date) {
        this.date = date;
        this.title = "";
        this.description = "";
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the image
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * @return the database id
     */
    public long getId() {
        return id;
    }

    /**
     * NASAImage items are on default compared by their dates
     * @param img - the NASAImage to compare with
     * @return the comparison value
     */
    @Override
    public int compareTo(NASAImage img) {
        return date.compareTo(img.getDate());
    }
}
