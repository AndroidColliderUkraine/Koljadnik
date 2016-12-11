package com.androidcollider.koljadnik.songs;

import java.util.ArrayList;

/**
 * Created by pseverin on 24.12.14.
 */
public class Song {

    private int id;
    private String name;
    private long rating;
    private int idType;
    private String text;
    private String remarks;
    private String source;
    private ArrayList<String> comments;


    public Song(int id, String name, long rating, int idType,
                String text, String remarks, String source, ArrayList<String> comments) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.idType = idType;
        this.text = text;
        this.remarks = remarks;
        this.source = source;
        this.comments = comments;
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

    public int getIdType() {
        return idType;
    }

    public String getText() {
        return text;
    }

    public String getRemarks() {
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

    public void setIdType(int id_type) {
        this.idType = id_type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        String comm = "";
        if (comments != null) {
            comm = comments.toString();
        }
        return "id=" + id + "  " +
                "name=" + name + "  " +
                "rating=" + rating + "  " +
                "idType=" + idType + "  " +
                "text=" + text + "  " +
                "remarks=" + remarks + "  " +
                "source=" + source + "  " +
                "comments=" + comm;
    }
}
