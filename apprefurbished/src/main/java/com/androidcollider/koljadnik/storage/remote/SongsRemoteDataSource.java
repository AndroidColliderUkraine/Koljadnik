package com.androidcollider.koljadnik.storage.remote;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.LocationEvent;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongRating;
import com.androidcollider.koljadnik.models.SongType;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public interface SongsRemoteDataSource {
    void getSongTypes(long lastUpdate, OnReadListener<List<SongType>> onReadListener);

    void getSongs(long lastUpdate, OnReadListener<List<Song>> onReadListener);

    void getSongRatings(long lastUpdate, OnReadListener<List<SongRating>> onReadListener);

    void updateSongRatings(List<SongRating> songRatings, OnWriteListener onWriteListener);

    void addLocationEvents(List<LocationEvent> locationEvents, OnSuccessListener<Void> onSuccessListener);
}
