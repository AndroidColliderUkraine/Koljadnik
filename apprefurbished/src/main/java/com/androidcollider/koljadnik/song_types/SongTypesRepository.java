package com.androidcollider.koljadnik.song_types;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

public interface SongTypesRepository {

    void getSongTypes(OnReadListener<List<SongType>> onReadListener);
}
