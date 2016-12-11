package com.androidcollider.koljadnik.song_types;

/**
 * Created by pseverin on 11.12.16.
 */

public class SongTypeViewModel {

    public final SongType songType;
    public final int quantity;

    public SongTypeViewModel(SongType songType, int quantity) {
        this.songType = songType;
        this.quantity = quantity;
    }
}
