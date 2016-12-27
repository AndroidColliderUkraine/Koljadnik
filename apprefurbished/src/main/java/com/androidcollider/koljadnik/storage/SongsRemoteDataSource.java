package com.androidcollider.koljadnik.storage;

import android.support.v4.util.Pair;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

public interface SongsRemoteDataSource {
    void getSongTypes(long lastUpdate, OnReadListener<List<SongType>> onReadListener);

    void getSongs(long lastUpdate, OnReadListener<List<Song>> onReadListener);

    void updateSongs(List<Song> songs, OnWriteListener onWriteListener);

}
