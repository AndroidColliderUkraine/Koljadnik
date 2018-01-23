package com.androidcollider.koljadnik.song_details;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.androidcollider.koljadnik.constants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.utils.ChordTags;
import com.androidcollider.koljadnik.utils.ChordUtils;
import com.androidcollider.koljadnik.utils.SessionSettingsManager;

public class SongDetailsActivityPresenter implements SongDetailsActivityMVP.Presenter {

    public final static String ARGS_SONG_TEXT = "song_text";

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
        UiAction uiAction = model.getSong(updateViewListener);
        if (uiAction == UiAction.BLOCK_UI && view != null) {
            view.blockUi();
        }
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
        UiAction uiAction = model.getShareData(new OnReadListener<ShareModel>() {
            @Override
            public void onSuccess(ShareModel result) {
                if (view != null) {
                    view.unblockUi();
                    view.startShareActivity(result.shareActivtyTitle, result.shareTitle, result.shareText);
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
        if (uiAction == UiAction.BLOCK_UI && view != null) {
            view.blockUi();
        }
    }

    @Override
    public void clickOnSmsBtn() {
        UiAction uiAction = model.getSongText(new OnReadListener<String>() {
            @Override
            public void onSuccess(String result) {
                if (view != null) {
                    view.unblockUi();
                    view.startSmsActivity(result);
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
        if (uiAction == UiAction.BLOCK_UI && view != null) {
            view.blockUi();
        }
    }

    @Override
    public void onAutoscrollChanged(int progress) {
        if (view != null) {
            view.updateScrollSpeed(Math.round(progress / 8));
        }
    }

    @Override
    public void onSizeChanged(int progress) {
        if (view != null) {
            int size = Math.round(((float) (MAX_TEXT_SIZE - MIN_TEXT_SIZE) / 100f) * progress + MIN_TEXT_SIZE);
            view.changeTextSize(size,
                sessionSettingsManager.getTextSize() < MAX_TEXT_SIZE,
                sessionSettingsManager.getTextSize() > MIN_TEXT_SIZE);
        }
    }

    @Override
    public void clickOnChordPlusBtn() {
        if (view != null && songText != null) {
            songText = ChordUtils.upAccord(songText);
            view.updateText(songText);
        }
    }

    @Override
    public void clickOnChordMinusBtn() {
        if (view != null && songText != null) {
            songText = ChordUtils.downAccord(songText);
            view.updateText(songText);
        }
    }

    @Override
    public void onSaveInstantState(Bundle outState) {
        outState.putString(ARGS_SONG_TEXT, songText);
    }

    @Override
    public void onRestoreInstantState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(ARGS_SONG_TEXT)){
            songText = savedInstanceState.getString(ARGS_SONG_TEXT);
        }
    }

    private String songText;

    private OnReadListener<SongDetailsViewModel> updateViewListener = new OnReadListener<SongDetailsViewModel>() {
        @Override
        public void onSuccess(SongDetailsViewModel songDetailsViewModel) {
            if (songText == null) {
                songText = songDetailsViewModel.getText();
            } else {
                songDetailsViewModel.setText(songText);
            }
            if (view != null) {
                view.unblockUi();
                view.updateView(songDetailsViewModel);
                view.updateChordBlockVisibility(songDetailsViewModel.getText().contains(ChordTags.CHORD_TAG_OPEN));
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
