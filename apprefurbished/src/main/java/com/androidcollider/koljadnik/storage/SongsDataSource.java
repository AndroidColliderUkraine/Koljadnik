package com.androidcollider.koljadnik.storage;

import android.support.v4.util.Pair;

import com.androidcollider.koljadnik.contants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

public interface SongsDataSource {

    UiAction getSongTypes(OnReadListener<List<SongType>> onReadListener);

    UiAction getSongs(OnReadListener<List<Song>> onReadListener);

    UiAction getSongsByType(int typeId, OnReadListener<List<Song>> onReadListener);

    UiAction getSongById(int songId,OnReadListener<Song> onReadListener);

    UiAction getMinMaxRating(OnReadListener<Pair<Long, Long>> onReadListener);

    void increaseSongLocalRating(Song song);

    void tryToLoadDataFromLocalFile();
}
