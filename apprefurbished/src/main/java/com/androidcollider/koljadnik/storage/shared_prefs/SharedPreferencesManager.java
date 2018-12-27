package com.androidcollider.koljadnik.storage.shared_prefs;

import android.content.SharedPreferences;

import com.androidcollider.koljadnik.BuildConfig;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongRating;
import com.androidcollider.koljadnik.models.SongType;


public class SharedPreferencesManager {
    public final static String LAST_UPDATE = "last_update";
    public final static String ALREADY_PARSED_DATA_FROM_LOCAL = "already_parsed_data_from_local";
    public final static String ALREADY_PARSED_SONGS_RATINGS_FROM_LOCAL = "already_parsed_songs_ratings_from_local";
    public final static String LAST_VERSION_CODE = "last_version_code";
    public final static String LOCATION_POPUP_SHOWS_COUNT = "location_popup_shows_count";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;

        if (BuildConfig.VERSION_CODE > getLastVersionCode()) {
            setLastUpdateForClass(SongRating.class, 100);
            setLastUpdateForClass(Song.class, 100);
            setLastUpdateForClass(SongType.class, 100);
            setLastVersionCode(BuildConfig.VERSION_CODE);
        }
    }

    public long getLastUpdateForClass(Class cl) {
        return sharedPreferences.getLong(LAST_UPDATE + cl.getSimpleName(), 100);
    }

    public void setLastUpdateForClass(Class cl, long timestamp) {
        sharedPreferences.edit().putLong(LAST_UPDATE + cl.getSimpleName(), timestamp).apply();
    }

    public int getLastVersionCode() {
        return sharedPreferences.getInt(LAST_VERSION_CODE, 0);
    }

    public void setLastVersionCode(int code) {
        sharedPreferences.edit().putInt(LAST_VERSION_CODE, code).apply();
    }

    public void setAlreadyParsedDataFromLocal(boolean isParsed) {
        sharedPreferences.edit().putBoolean(ALREADY_PARSED_DATA_FROM_LOCAL, isParsed).apply();
    }

    public boolean isAlreadyParsedDataFromLocal() {
        return sharedPreferences.getBoolean(ALREADY_PARSED_DATA_FROM_LOCAL, false);
    }

    public void setAlreadyParsedSongsRatingsFromLocal(boolean isParsed) {
        sharedPreferences.edit().putBoolean(ALREADY_PARSED_SONGS_RATINGS_FROM_LOCAL, isParsed).apply();
    }

    public boolean isAlreadyParsedSongsRatingsFromLocal() {
        return sharedPreferences.getBoolean(ALREADY_PARSED_SONGS_RATINGS_FROM_LOCAL, false);
    }

    public int getLocationPopupTriesCount() {
        return sharedPreferences.getInt(LOCATION_POPUP_SHOWS_COUNT, 0);
    }

    public void incrementLocationPopupTriesCount() {
        sharedPreferences.edit().putInt(LOCATION_POPUP_SHOWS_COUNT, getLocationPopupTriesCount()+1).apply();
    }
}
