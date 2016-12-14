package com.androidcollider.koljadnik.songs;

import android.support.annotation.Nullable;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.song_types.SongTypeViewModel;
import com.androidcollider.koljadnik.song_types.SongTypesActivityMVP;

import java.util.List;

public class SongsActivityPresenter implements SongsActivityMVP.Presenter {

    @Nullable
    private SongsActivityMVP.View view;
    private SongsActivityMVP.Model model;


    public SongsActivityPresenter(SongsActivityMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(SongsActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void initData(int typeId) {
        model.getSongsByTypeId(typeId, new OnReadListener<List<SongItemViewModel>>() {
            @Override
            public void onSuccess(List<SongItemViewModel> songTypes) {
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
    public void openSongDetailsUI(Object tag) {
        int songId = (int) tag;
        if (view != null) {
            view.showSongDetailsUI(songId);
        }
    }

    public void onSongsLoaded(List<SongItemViewModel> songTypes) {
        if (songTypes != null && !songTypes.isEmpty()) {
            if (view != null) {
                view.updateAdapter(songTypes);
            }
        }
    }
}
