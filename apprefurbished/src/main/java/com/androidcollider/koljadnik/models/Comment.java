package com.androidcollider.koljadnik.models;

import com.google.firebase.database.IgnoreExtraProperties;

import io.realm.RealmObject;

@IgnoreExtraProperties
public class Comment extends RealmObject{

    private String text;

    // Default constructor required for calls to
    // DataSnapshot.getValue(SongType.class) and Realm
    public Comment() {
    }

    public Comment(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
