package com.androidcollider.koljadnik.storage;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongRating;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

public interface SongsLocalDataSource {
    void getSongTypes(OnReadListener<List<SongType>> onReadListener);

    List<SongType> getSongTypes();

    void getSongs(OnReadListener<List<Song>> onReadListener);

    List<Song> getSongs();

    List<SongRating> getSongRatings();

    void saveSongTypes(List<SongType> songTypes, OnWriteListener onWriteListener);

    void saveSongs(List<Song> songs, OnWriteListener onWriteListener);

    void saveSongRatings(List<SongRating> songs, OnWriteListener onWriteListener);

    void increaseSongLocalRating(SongRating songRating);
}
