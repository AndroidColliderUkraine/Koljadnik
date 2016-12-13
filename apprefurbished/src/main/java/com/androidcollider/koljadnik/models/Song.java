package com.androidcollider.koljadnik.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

@IgnoreExtraProperties
public class Song extends RealmObject {

    private int id;
    private String name;
    private long rating;
    private int idType;
    private String text;
    private String remarks;
    private String source;
    private RealmList<Comment> comments;
    private long updatedAt;

    // Default constructor required for calls to
    // DataSnapshot.getValue(SongType.class) and Realm
    public Song() {

    }

    public Song(int id, String name, long rating, int idType, String text, String remarks, String source, List<Comment> comments, long updatedAt) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.idType = idType;
        this.text = text;
        this.remarks = remarks;
        this.source = source;
        this.comments = new RealmList<>();
        comments.addAll(comments);
        this.updatedAt = updatedAt;
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

    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }

    public long getUpdatedAt() {
        return updatedAt;
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
