package com.androidcollider.koljadnik.repository;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.song_types.SongType;

import java.util.List;

public interface SongTypesDataSource {

    void getSongTypes(OnReadListener<List<SongType>> onReadListener);
}
