package com.androidcollider.koljadnik.storage;

import android.support.v4.util.Pair;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

public interface SongsDataSource {

    void getSongTypes(OnReadListener<List<SongType>> onReadListener);

    void getSongs(OnReadListener<List<Song>> onReadListener);


    void saveSongTypes(List<SongType> songTypes, OnWriteListener onWriteListener);

    void saveSongs(List<Song> songs, OnWriteListener onWriteListener);

    void updateSongs(List<Song> songs, OnWriteListener onWriteListener);



    void getSongsByType(int typeId, OnReadListener<List<Song>> onReadListener);

    void getSongById(int songId,OnReadListener<Song> onReadListener);

    void getMinMaxRating(OnReadListener<Pair<Long, Long>> onReadListener);

    void increaseSongLocalRating(Song song);
}
