package com.androidcollider.koljadnik.song_types;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.repository.SongTypesDataSource;

import java.util.List;

public class SongTypesActivityModel implements SongTypesActivityMVP.Model {

    private SongTypesDataSource songTypesRepository;

    public SongTypesActivityModel(SongTypesDataSource songTypesRepository) {
        this.songTypesRepository = songTypesRepository;
    }

    @Override
    public void getSongTypes(OnReadListener<List<SongType>> listener) {
        songTypesRepository.getSongTypes(listener);
    }
}
