package com.androidcollider.koljadnik.song_types;

import android.support.annotation.Nullable;

import com.androidcollider.koljadnik.contants.UiAction;
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
        UiAction uiAction = model.getSongTypes(new OnReadListener<List<SongTypeViewModel>>() {
            @Override
            public void onSuccess(List<SongTypeViewModel> songTypes) {
                if (songTypes != null && !songTypes.isEmpty()) {
                    if (view != null) {
                        view.unblockUi();
                        view.updateAdapter(songTypes);
                    }
                }
            }

            @Override
            public void onError(String error) {
                if (view != null) {
                    view.unblockUi();
                    view.showErrorToast(error);
                }
            }
        });
        if (uiAction == UiAction.BLOCK_UI && view != null){
            view.blockUi();
        }
    }

    @Override
    public void openSongListUI(Object tag1, Object tag2) {
        int typeId = (int) tag1;
        String typeName = (String) tag2;
        if (view != null) {
            view.showSongListUI(typeId, typeName);
        }
    }
}
