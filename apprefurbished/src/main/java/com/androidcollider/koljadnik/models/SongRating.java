package com.androidcollider.koljadnik.models;

import android.support.v4.util.Pair;

import com.androidcollider.koljadnik.constants.Settings;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@IgnoreExtraProperties
public class SongRating extends RealmObject {

    @PrimaryKey
    private int idSong;
    private long rating;
    private long updatedAt;

    private long localRating;

    // Default constructor required for calls to
    // DataSnapshot.getValue(SongType.class) and Realm
    public SongRating() {

    }

    public SongRating(int idSong, long rating, long updatedAt) {
        this.idSong = idSong;
        this.rating = rating;
        this.updatedAt = updatedAt;
    }

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getLocalRating() {
        return localRating;
    }

    public void setLocalRating(long localRating) {
        this.localRating = localRating;
    }


    public long getTotalRating() {
        return rating + localRating;
    }

    public long getRating() {
        return rating;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("songId", idSong);
        result.put("rating", rating);
        result.put("updatedAt", updatedAt);
        return result;
    }

    @Override
    public String toString() {
        return "idSong=" + idSong + "  " +
                "rating=" + rating + "  " +
                "localRating=" + localRating + "  " +
                "updatedAt=" + updatedAt;
    }

    public static SongRating findInListById(List<SongRating> songRatings, int id) {
        for (SongRating songRating : songRatings) {
            if (songRating.getIdSong() == id) {
                return songRating;
            }
        }
        return null;
    }

    public static Pair<Long, Long> findMinMax(List<SongRating> songRatings) {
        long minRating = 0;
        long maxRating = 0;
        if (songRatings.size() > 0) {
            minRating = songRatings.get(0).getTotalRating();
            maxRating = songRatings.get(0).getTotalRating();
        }
        for (SongRating songRating : songRatings) {
            if (songRating.getTotalRating() > maxRating) {
                maxRating = songRating.getTotalRating();
            }
            if (songRating.getTotalRating() < minRating) {
                minRating = songRating.getTotalRating();
            }
        }
        return new Pair<>(minRating, maxRating);
    }

    public int getRatingByMinMax(long min, long max) {
        if (getTotalRating() > Settings.RATING_DEFAULT_LIMIT) {
            double clearRating = getTotalRating() - min;
            double onePoint = (double) (max - min) / 5d;
            return (int) ((clearRating - 1) / onePoint) + 1;
        } else {
            return Settings.DEFAULT_RATING;
        }
    }


    public void increaseLocalRating() {
        localRating++;
    }


    public static List<SongRating> generateSongRatingsList(JSONArray jsonArray) throws JSONException {
        List<SongRating> songTypes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            songTypes.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return songTypes;
    }

    public static SongRating fromJson(JSONObject jsonObject) throws JSONException {
        return new SongRating(jsonObject.getInt("songId"),
                jsonObject.getLong("rating"),
                jsonObject.getLong("updatedAt"));
    }
}
