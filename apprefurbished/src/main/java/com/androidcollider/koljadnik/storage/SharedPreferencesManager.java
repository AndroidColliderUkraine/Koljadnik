package com.androidcollider.koljadnik.storage;

import android.content.SharedPreferences;


public class SharedPreferencesManager {
    public final static String LAST_UPDATE = "last_update";
    public final static String ALREADY_PARSED_DATA_FROM_LOCAL = "already_parsed_data_from_local";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public long getLastUpdateForClass(Class cl){
        return sharedPreferences.getLong(LAST_UPDATE + cl.getSimpleName(), 100);
    }

    public void setLastUpdateForClass(Class cl, long timestamp){
        sharedPreferences.edit().putLong(LAST_UPDATE + cl.getSimpleName(), timestamp).apply();
    }

    public void setAlreadyParsedDataFromLocal(boolean isParsed){
        sharedPreferences.edit().putBoolean(ALREADY_PARSED_DATA_FROM_LOCAL, isParsed).apply();
    }

    public boolean isAlreadyParsedDataFromLocal(){
        return sharedPreferences.getBoolean(ALREADY_PARSED_DATA_FROM_LOCAL, false);
    }
}
