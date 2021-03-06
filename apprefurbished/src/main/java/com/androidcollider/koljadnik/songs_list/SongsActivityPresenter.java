package com.androidcollider.koljadnik.songs_list;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.androidcollider.koljadnik.listeners.CallStartProgressDialog;
import com.androidcollider.koljadnik.listeners.OnReadListener;

import java.util.ArrayList;
import java.util.List;

public class SongsActivityPresenter implements SongsActivityMVP.Presenter {

    public final static String ARGS_IS_CHORD_FILTERED_NAME = "is_chord_filtered";

    @Nullable
    private SongsActivityMVP.View view;
    private SongsActivityMVP.Model model;

    private String searchStr = "";
    private OrderType orderType = OrderType.BY_ALPHABET;
    private boolean onlyWithChords = false;
    private List<SongItemViewModel> currentSongs = new ArrayList<>();

    public SongsActivityPresenter(SongsActivityMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(SongsActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void initData() {
        model.getSongsBySearchAndOrdered(searchStr, orderType, updateAdapterListener, callDialogCallback);
    }

    @Override
    public void updateFilterState(Bundle savedState) {
        if (savedState != null && savedState.containsKey(ARGS_IS_CHORD_FILTERED_NAME)){
            onlyWithChords = savedState.getBoolean(ARGS_IS_CHORD_FILTERED_NAME);
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
        this.orderType = orderType;
        model.getSongsBySearchAndOrdered(searchStr, orderType, new OnReadListener<List<SongItemViewModel>>() {
            @Override
            public void onSuccess(List<SongItemViewModel> songTypes) {
                if (view != null) {
                    view.unblockUi();
                    currentSongs = songTypes;
                    view.updateAdapter(filterSongs(currentSongs), onlyWithChords);
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
        }, callDialogCallback);
    }

    @Override
    public void proceedSearch(String searchStr) {
        this.searchStr = searchStr;
        model.getSongsBySearchAndOrdered(searchStr, orderType, updateAdapterListener, callDialogCallback);
    }

    @Override
    public void clickOnFabAll() {
        if (onlyWithChords) {
            onlyWithChords = false;
            if (view != null){
                view.updateAdapter(filterSongs(currentSongs), onlyWithChords);
            }
        }
    }

    @Override
    public void clickOnFabWithChords() {
        if (!onlyWithChords) {
            onlyWithChords = true;
            if (view != null){
                view.updateAdapter(filterSongs(currentSongs), onlyWithChords);
            }
        }
    }

    @Override
    public void onSaveInstantState(Bundle outState) {
        outState.putBoolean(ARGS_IS_CHORD_FILTERED_NAME, onlyWithChords);
    }

    private OnReadListener<List<SongItemViewModel>> updateAdapterListener = new OnReadListener<List<SongItemViewModel>>() {
        @Override
        public void onSuccess(List<SongItemViewModel> songTypes) {
            if (view != null) {
                view.unblockUi();
                currentSongs = songTypes;
                view.updateAdapter(filterSongs(currentSongs), onlyWithChords);
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

    private CallStartProgressDialog callDialogCallback = new CallStartProgressDialog() {
        @Override
        public void onCall() {
            if (view != null){
                view.blockUi();
            }
        }
    };

    private List<SongItemViewModel> filterSongs(List<SongItemViewModel> src){
        if (!onlyWithChords){
            return src;
        } else {
            List<SongItemViewModel> filteredList = new ArrayList<>();
            for (SongItemViewModel item : src) {
                if (item.hasNota){
                    filteredList.add(item);
                }
            }
            return filteredList;
        }
    }
}
