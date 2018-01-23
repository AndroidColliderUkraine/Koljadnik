package com.androidcollider.koljadnik.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@IgnoreExtraProperties
public class Song extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
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

    public Song(int id, String name, int idType, String text, String remarks, String source, long updatedAt) {
        this.id = id;
        this.name = name;
        this.idType = idType;
        this.text = text;
        this.remarks = remarks;
        this.source = source;
        this.updatedAt = updatedAt;
    }

    public Song(int id, String name, int idType, String text, String remarks, String source, List<Comment> comments, long updatedAt) {
        this.id = id;
        this.name = name;
        this.idType = idType;
        this.text = text;
        this.remarks = remarks;
        this.source = source;
        this.comments = new RealmList<>();
        this.comments.addAll(comments);
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("idType", idType);
        result.put("text", text);
        result.put("remarks", remarks);
        result.put("source", source);
        result.put("comments", comments);
        result.put("updatedAt", updatedAt);
        return result;
    }

    @Override
    public String toString() {
        String comm = "";
        if (comments != null) {
            comm = comments.toString();
        }
        return "id=" + id + "  " +
                "name=" + name + "  " +
                "idType=" + idType + "  " +
                "text=" + text + "  " +
                "remarks=" + remarks + "  " +
                "source=" + source + "  " +
                "comments=" + comm;
    }

    public static Song findInListById(List<Song> songs, int id) {
        for (Song song : songs) {
            if (song.getId() == id) {
                return song;
            }
        }
        return null;
    }

    public static List<Song> generateSongList(JSONArray jsonArray) throws JSONException {
        List<Song> songList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            songList.add(Song.fromJson(jsonArray.getJSONObject(i)));
        }
        return songList;
    }

    public static Song fromJson(JSONObject jsonObject) throws JSONException {
        return new Song(jsonObject.getInt("id"),
                jsonObject.getString("name"),
                jsonObject.getInt("idType"),
                jsonObject.getString("text"),
                jsonObject.getString("remarks"),
                jsonObject.getString("source"),
                new ArrayList<>(),
                jsonObject.getLong("updatedAt"));
    }
}
