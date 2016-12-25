package com.androidcollider.koljadnik.songs_list;

import android.support.annotation.Nullable;

import com.androidcollider.koljadnik.contants.Settings;
import com.androidcollider.koljadnik.listeners.OnReadListener;

import java.util.List;

public class SongsActivityPresenter implements SongsActivityMVP.Presenter {

    @Nullable
    private SongsActivityMVP.View view;
    private SongsActivityMVP.Model model;

    private boolean isSearching;


    public SongsActivityPresenter(SongsActivityMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(SongsActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void initData() {
        model.getSongsByTypeId(updateAdapterListener);
    }

    @Override
    public void openSongDetailsUI(Object tag) {
        int songId = (int) tag;
        if (view != null) {
            model.increaseRating(songId);
            view.showSongDetailsUI(songId);
        }
    }

    @Override
    public void clickOnOrderMenuBtn() {
        if (view != null) {
            view.switchOrderMenuVisibility();
        }
    }

    @Override
    public void clickOnOrderBtn(OrderType orderType) {
        model.getSongsOrdered(orderType, new OnReadListener<List<SongItemViewModel>>() {
            @Override
            public void onSuccess(List<SongItemViewModel> songTypes) {
                if (view != null) {
                    view.updateAdapter(songTypes);
                    view.switchOrderMenuVisibility();
                }
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
    public void proceedSearch(String searchStr) {
        if (searchStr.length() >= Settings.SEARCH_LIMIT){
            model.getSongsBySearch(searchStr, updateAdapterListener);
            isSearching = true;
        } else {
            if (isSearching){
                model.getSongsByTypeId(updateAdapterListener);
            }
            isSearching = false;
        }
    }

    private OnReadListener<List<SongItemViewModel>> updateAdapterListener = new OnReadListener<List<SongItemViewModel>>() {
        @Override
        public void onSuccess(List<SongItemViewModel> songTypes) {
            if (view != null) {
                view.updateAdapter(songTypes);
            }
        }

        @Override
        public void onError(String error) {
            if (view != null) {
                view.showErrorToast(error);
            }
        }
    };
}
