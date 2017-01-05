package com.androidcollider.koljadnik.songs_list;

import android.support.annotation.Nullable;

import com.androidcollider.koljadnik.listeners.CallStartProgressDialog;
import com.androidcollider.koljadnik.listeners.OnReadListener;

import java.util.List;

public class SongsActivityPresenter implements SongsActivityMVP.Presenter {

    @Nullable
    private SongsActivityMVP.View view;
    private SongsActivityMVP.Model model;

    private String searchStr = "";
    private OrderType orderType = OrderType.BY_ALPHABET;


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
        }, callDialogCallback);
    }

    @Override
    public void proceedSearch(String searchStr) {
        this.searchStr = searchStr;
        model.getSongsBySearchAndOrdered(searchStr, orderType, updateAdapterListener, callDialogCallback);
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

    private CallStartProgressDialog callDialogCallback = new CallStartProgressDialog() {
        @Override
        public void onCall() {
            if (view != null){
                view.blockUi();
            }
        }
    };
}
