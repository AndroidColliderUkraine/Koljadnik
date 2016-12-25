package com.androidcollider.koljadnik.song_details;


import com.androidcollider.koljadnik.listeners.OnReadListener;

public interface SongDetailsActivityMVP {

    interface View {
        void updateView(SongDetailsViewModel songDetailsViewModel);

        void changeTextSize(int size, boolean showPlus, boolean showMinus);

        void showErrorToast(String text);

        void startSmsActivity(String smsText);

        void startShareActivity(String shareActivityTitle, String shareTitle, String shareText);
    }

    interface Presenter {
        void setView(View view);

        void initData();

        void clickOnPlusBtn();

        void clickOnMinusBtn();

        void clickOnShareBtn();

        void clickOnSmsBtn();
    }

    interface Model {
        void getSong(final OnReadListener<SongDetailsViewModel> listener);

        void getSongText(final OnReadListener<String> listener);

        void getShareData(final OnReadListener<ShareModel> listener);
       /* void getSongWithIncreasedTextSize(int songId, OnReadListener<SongDetailsViewModel> listener);
        void getSongWithReducedTextSize(int songId, OnReadListener<SongDetailsViewModel> listener);
        void getSongText(int songId, OnReadListener<SongDetailsViewModel> listener);*/
    }
}
