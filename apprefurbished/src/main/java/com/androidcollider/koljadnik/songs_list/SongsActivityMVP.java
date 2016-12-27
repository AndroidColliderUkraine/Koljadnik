package com.androidcollider.koljadnik.songs_list;


import com.androidcollider.koljadnik.common.CoomonView;
import com.androidcollider.koljadnik.contants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.Song;

import java.util.List;

public interface SongsActivityMVP {

    interface View extends CoomonView{
        void updateAdapter(List<SongItemViewModel> songItemViewModels);
        void showErrorToast(String text);
        void showSongDetailsUI(int songId);
        void switchOrderMenuVisibility();
    }

    interface Presenter {
        void setView(View view);
        void initData();
        void openSongDetailsUI(Object object);
        void clickOnOrderMenuBtn();
        void clickOnOrderBtn(OrderType orderType);
        void proceedSearch(String searchStr);
    }

    interface Model {
        UiAction getSongsByTypeId(final OnReadListener<List<SongItemViewModel>> listener);
        UiAction getSongsOrdered(OrderType orderType, final OnReadListener<List<SongItemViewModel>> listener);
        UiAction getSongsBySearch(String searchStr, OnReadListener<List<SongItemViewModel>> listener);
        void increaseRating(int songId);
    }
}
