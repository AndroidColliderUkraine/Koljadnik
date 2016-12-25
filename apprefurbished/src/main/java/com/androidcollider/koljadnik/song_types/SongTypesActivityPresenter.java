package com.androidcollider.koljadnik.song_types;

import android.support.annotation.Nullable;

import com.androidcollider.koljadnik.listeners.OnReadListener;

import java.util.List;

public class SongTypesActivityPresenter implements SongTypesActivityMVP.Presenter {

    @Nullable
    private SongTypesActivityMVP.View view;
    private SongTypesActivityMVP.Model model;


    public SongTypesActivityPresenter(SongTypesActivityMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(SongTypesActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void initData() {
        model.getSongTypes(new OnReadListener<List<SongTypeViewModel>>() {
            @Override
            public void onSuccess(List<SongTypeViewModel> songTypes) {
                onSongsLoaded(songTypes);
            }

            @Override
            public void onError(String error) {
                if (view != null) {
                    view.showErrorToast(error);
                }
            }
        });
    }

    @Override
    public void openSongListUI(Object tag) {
        int typeId = (int) tag;
        if (view != null) {
            view.showSongListUI(typeId);
        }
    }

    public void onSongsLoaded(List<SongTypeViewModel> songTypes) {
        if (songTypes != null && !songTypes.isEmpty()) {
            if (view != null) {
                view.updateAdapter(songTypes);
            }
        }
    }
}