package com.androidcollider.koljadnik.objects;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by pseverin on 24.12.14.
 */
public class Song {

    public static long current_min_rating;
    public static long current_max_rating;

    private int id;
    private String name;
    private long rating;
    private int id_type;
    private String text;
    private ArrayList<String> remarks;
    private String source;
    private ArrayList<String> comments;

    /*private Bitmap chord;
    private Bitmap note;*/

    public Song(int id, String name, long rating, int id_type,
                String text, ArrayList<String> remarks, String source, ArrayList<String> comments) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.id_type = id_type;
        this.text = text;
        this.remarks = remarks;
        this.source = source;
        this.comments = comments;
    }

    public Song(int id, long rating, String name) {
        this.id = id;
        this.rating = rating;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getRating() {
        return rating;
    }

    public int getId_type() {
        return id_type;
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getRemarks() {
        return remarks;
    }

    public String getSource() {
        return source;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRemarks(ArrayList<String> remarks) {
        this.remarks = remarks;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }
}
