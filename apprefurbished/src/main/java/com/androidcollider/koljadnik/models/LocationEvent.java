package com.androidcollider.koljadnik.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@IgnoreExtraProperties
public class LocationEvent extends RealmObject {

    @PrimaryKey
    private String id;
    private int songId;
    private String lat;
    private String lng;
    private String timestamp;

    public LocationEvent() {
    }

    public LocationEvent(int songId, String lat, String lng) {
        this.id = UUID.randomUUID().toString();
        this.songId = songId;
        this.lat = lat;
        this.lng = lng;
        this.timestamp = String.valueOf(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public int getSongId() {
        return songId;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("songId", songId);
        result.put("lat", lat);
        result.put("lng", lng);
        result.put("timestamp", timestamp);
        return result;
    }

}
