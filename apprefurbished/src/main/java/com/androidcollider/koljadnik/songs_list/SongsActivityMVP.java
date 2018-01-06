package com.androidcollider.koljadnik.songs_list;


import com.androidcollider.koljadnik.common.CoomonView;
import com.androidcollider.koljadnik.listeners.CallStartProgressDialog;
import com.androidcollider.koljadnik.listeners.OnReadListener;

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
        void clickOnFabAll();
        void clickOnFabWithChords();
    }

    interface Model {
        void getSongsByTypeId(final OnReadListener<List<SongItemViewModel>> listener, final CallStartProgressDialog dialogCall);
        void getSongsBySearchAndOrdered(String searchStr, OrderType orderType, OnReadListener<List<SongItemViewModel>> listener, final CallStartProgressDialog dialogCall);
        void increaseRating(int songId);
    }
}
