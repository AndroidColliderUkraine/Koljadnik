package com.androidcollider.koljadnik.repository;


import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.song_types.SongType;

import java.util.List;

public class SongTypesMemoryDataSource implements SongTypesDataSource {

    @Override
    public void getSongTypes(OnReadListener<List<SongType>> onReadListener) {
    }
}
