package com.androidcollider.koljadnik.root;

import android.content.SharedPreferences;


public class SharedPreferencesManager {
    public final static String LAST_UPDATE = "last_update";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public long getLastUpdateForClass(Class cl){
        return sharedPreferences.getLong(LAST_UPDATE + cl.getSimpleName(), 0);
    }

    public void setLastUpdateForClass(Class cl, long timestamp){
        sharedPreferences.edit().putLong(LAST_UPDATE + cl.getSimpleName(), timestamp).apply();
    }
}
