package com.androidcollider.koljadnik.song_types;


import com.androidcollider.koljadnik.models.SongType;

public class SongTypeViewModel {

    public final SongType songType;
    public final int quantity;

    public SongTypeViewModel(SongType songType, int quantity) {
        this.songType = songType;
        this.quantity = quantity;
    }
}
