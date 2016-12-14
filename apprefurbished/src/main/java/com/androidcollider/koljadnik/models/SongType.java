package com.androidcollider.koljadnik.models;

import com.google.firebase.database.IgnoreExtraProperties;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@IgnoreExtraProperties
public class SongType extends RealmObject{

    @PrimaryKey
    private int id;
    private String name;
    private long updatedAt;

    // Default constructor required for calls to
    // DataSnapshot.getValue(SongType.class) and Realm
    public SongType() {
    }

    public SongType(int id, String name, long updatedAt) {
        this.id = id;
        this.name = name;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
}
