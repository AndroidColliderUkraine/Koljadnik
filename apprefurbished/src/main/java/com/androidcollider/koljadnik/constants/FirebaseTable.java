package com.androidcollider.koljadnik.constants;

public enum FirebaseTable {
    SONGS("songs"),
    SONG_TYPES("songTypes"),
    SONG_RATINGS("songRatings");

    public final String label;

    FirebaseTable(String label) {
        this.label = label;
    }
}
