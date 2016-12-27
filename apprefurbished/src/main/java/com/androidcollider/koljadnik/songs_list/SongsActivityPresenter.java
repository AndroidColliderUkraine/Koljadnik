package com.androidcollider.koljadnik.songs_list;

import android.support.annotation.Nullable;
import android.util.Log;

import com.androidcollider.koljadnik.contants.Settings;
import com.androidcollider.koljadnik.contants.UiAction;
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
        UiAction uiAction = model.getSongsByTypeId(updateAdapterListener);
        if (uiAction == UiAction.BLOCK_UI && view != null){
            view.blockUi();
        }
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
        UiAction uiAction = model.getSongsOrdered(orderType, new OnReadListener<List<SongItemViewModel>>() {
            @Override
            public void onSuccess(List<SongItemViewModel> songTypes) {
                if (view != null) {
                    view.unblockUi();
                    view.updateAdapter(songTypes);
                    view.switchOrderMenuVisibility();
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
    public void proceedSearch(String searchStr) {
        if (searchStr.length() >= Settings.SEARCH_LIMIT){
            UiAction uiAction = model.getSongsBySearch(searchStr, updateAdapterListener);
            if (uiAction == UiAction.BLOCK_UI && view != null){
                view.blockUi();
            }
            isSearching = true;
        } else {
            if (isSearching){
                UiAction uiAction =  model.getSongsByTypeId(updateAdapterListener);
                if (uiAction == UiAction.BLOCK_UI && view != null){
                    view.blockUi();
                }
            }
            isSearching = false;
        }
    }

    private OnReadListener<List<SongItemViewModel>> updateAdapterListener = new OnReadListener<List<SongItemViewModel>>() {
        @Override
        public void onSuccess(List<SongItemViewModel> songTypes) {
            if (view != null) {
                view.unblockUi();
                view.updateAdapter(songTypes);
            }
        }

        @Override
        public void onError(String error) {
            if (view != null) {
                view.unblockUi();
                view.showErrorToast(error);
            }
        }
    };
}
