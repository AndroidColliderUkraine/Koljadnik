package com.androidcollider.koljadnik.contants;

public enum FirebaseTable {
    SONGS("songs"),
    SONG_TYPES("songTypes");

    public final String label;

    FirebaseTable(String label) {
        this.label = label;
    }
}
