package com.androidcollider.koljadnik.song_types;

import android.support.annotation.Nullable;
import android.util.Log;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

public class SongTypesActivityPresenter implements SongTypesActivityMVP.Presenter {

    @Nullable
    private SongTypesActivityMVP.View view;
    private SongTypesActivityMVP.Model model;

    private SongTypeAdapter songTypeAdapter;

    public SongTypesActivityPresenter(SongTypesActivityMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(SongTypesActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void initData() {
        model.getSongTypes(new OnReadListener<List<SongType>>(){
            @Override
            public void onSuccess(List<SongType> songTypes) {
                if (songTypes != null && !songTypes.isEmpty()) {
                    songTypeAdapter = new SongTypeAdapter(songTypes);

                    if (view != null) {
                        view.setAdapterToList(songTypeAdapter);
                        view.setLinearLayoutManager();
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.i("SongTypes", error);
            }
        });
    }
}
