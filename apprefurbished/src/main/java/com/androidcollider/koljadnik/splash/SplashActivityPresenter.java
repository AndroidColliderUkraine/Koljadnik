package com.androidcollider.koljadnik.splash;

import android.support.annotation.Nullable;

/**
 * @author Severyn Parkhomenko <pseverin@ukr.net>
 */
public class SplashActivityPresenter implements SplashActivityMVP.Presenter {

    private SplashActivityMVP.Model model;
    @Nullable
    private SplashActivityMVP.View view;

    public SplashActivityPresenter(SplashActivityMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(@Nullable SplashActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void init() {
        model.tryToLoadDataFromLocalFile();
        if (view != null) {
            view.startAnimationAndShowSongTypesUI();
        }
    }
}
