package com.androidcollider.koljadnik.song_details;


import android.os.Bundle;

import com.androidcollider.koljadnik.common.CoomonView;
import com.androidcollider.koljadnik.constants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;

public interface SongDetailsActivityMVP {

    interface View extends CoomonView {
        void updateView(SongDetailsViewModel songDetailsViewModel);

        void changeTextSize(int size, boolean showPlus, boolean showMinus);

        void showErrorToast(String text);

        void startSmsActivity(String smsText);

        void startShareActivity(String shareActivityTitle, String shareTitle, String shareText);

        void updateScrollSpeed(int speed);

        void updateText(String songText);

        void updateChordBlockVisibility(boolean isVisible);
    }

    interface Presenter {
        void setView(View view);

        void initData();

        void clickOnPlusBtn();

        void clickOnMinusBtn();

        void clickOnShareBtn();

        void clickOnSmsBtn();

        void onAutoscrollChanged(int progress);

        void onSizeChanged(int progress);

        void clickOnChordPlusBtn();

        void clickOnChordMinusBtn();

        void onSaveInstantState(Bundle outState);

        void onRestoreInstantState(Bundle savedInstanceState);
    }

    interface Model {
        UiAction getSong(final OnReadListener<SongDetailsViewModel> listener);

        UiAction getSongText(final OnReadListener<String> listener);

        UiAction getShareData(final OnReadListener<ShareModel> listener);

        void addLocationEvent(int songId, String lat, String lng);
    }
}
