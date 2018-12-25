package com.androidcollider.koljadnik.storage;

import com.androidcollider.koljadnik.constants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongRating;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

public interface SongsDataSource {

    UiAction getSongTypes(OnReadListener<List<SongType>> onReadListener);

    UiAction getSongs(OnReadListener<List<Song>> onReadListener);

    UiAction getSongsByType(int typeId, OnReadListener<List<Song>> onReadListener);

    UiAction getSongById(int songId,OnReadListener<Song> onReadListener);

    UiAction getRatings(OnReadListener<List<SongRating>> onReadListener);

    void increaseSongLocalRating(Song song);

    void tryToLoadDataFromLocalFile();
}
