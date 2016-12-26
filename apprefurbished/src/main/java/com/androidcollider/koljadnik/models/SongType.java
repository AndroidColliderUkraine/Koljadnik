package com.androidcollider.koljadnik.models;

import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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


    public static List<SongType> generateSongTypesList(JSONObject jsonObject) throws JSONException {
        List<SongType> songTypes = new ArrayList<>();
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            songTypes.add(SongType.fromJson(jsonObject.getJSONObject(key)));
        }
        return songTypes;
    }

    public static SongType fromJson(JSONObject jsonObject) throws JSONException {
        return new SongType(jsonObject.getInt("id"),
                jsonObject.getString("name"),
                jsonObject.getLong("updatedAt"));
    }
}
