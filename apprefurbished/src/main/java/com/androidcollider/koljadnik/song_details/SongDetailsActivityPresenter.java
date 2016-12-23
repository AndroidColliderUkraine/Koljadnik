package com.androidcollider.koljadnik.song_details;

import android.support.annotation.Nullable;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.utils.SessionSettingsManager;

public class SongDetailsActivityPresenter implements SongDetailsActivityMVP.Presenter {

    public static final int MIN_TEXT_SIZE = 10;
    public static final int MAX_TEXT_SIZE = 28;

    @Nullable
    private SongDetailsActivityMVP.View view;
    private SongDetailsActivityMVP.Model model;

    private SessionSettingsManager sessionSettingsManager;

    public SongDetailsActivityPresenter(SongDetailsActivityMVP.Model model,
                                        SessionSettingsManager sessionSettingsManager) {
        this.model = model;
        this.sessionSettingsManager = sessionSettingsManager;
    }

    @Override
    public void setView(SongDetailsActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void initData() {
        model.getSong(updateViewListener);
        updateTextSize();
    }

    @Override
    public void clickOnPlusBtn() {
        sessionSettingsManager.increaseTextSize();
        updateTextSize();
    }

    @Override
    public void clickOnMinusBtn() {
        sessionSettingsManager.reduceTextSize();
        updateTextSize();
    }

    private void updateTextSize() {
        if (view != null) {
            view.changeTextSize(sessionSettingsManager.getTextSize(),
                    sessionSettingsManager.getTextSize() < MAX_TEXT_SIZE,
                    sessionSettingsManager.getTextSize() > MIN_TEXT_SIZE);
        }
    }

    @Override
    public void clickOnShareBtn() {
        model.getShareData(new OnReadListener<ShareModel>() {
            @Override
            public void onSuccess(ShareModel result) {
                if (view != null) {
                    view.startShareActivity(result.shareActivtyTitle, result.shareTitle, result.shareText);
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
    public void clickOnSmsBtn() {
        model.getSongText(new OnReadListener<String>() {
            @Override
            public void onSuccess(String result) {
                if (view != null) {
                    view.startSmsActivity(result);
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

    private OnReadListener<SongDetailsViewModel> updateViewListener = new OnReadListener<SongDetailsViewModel>() {
        @Override
        public void onSuccess(SongDetailsViewModel songDetailsViewModel) {
            if (view != null) {
                view.updateView(songDetailsViewModel);
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
