package com.androidcollider.koljadnik.song_types;

import android.support.annotation.Nullable;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

public class SongTypesActivityModel implements SongTypesActivityMVP.Model {

    private SongTypesRepository songTypesRepository;

    public SongTypesActivityModel(SongTypesRepository songTypesRepository) {
        this.songTypesRepository = songTypesRepository;
    }

    @Override
    public void getSongTypes(OnReadListener<List<SongType>> listener) {
        songTypesRepository.getSongTypes(listener);
    }
}
