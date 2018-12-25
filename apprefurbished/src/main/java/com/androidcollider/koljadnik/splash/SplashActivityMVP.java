package com.androidcollider.koljadnik.splash;


import com.androidcollider.koljadnik.common.CoomonView;

public interface SplashActivityMVP {

    interface View extends CoomonView {
        void startAnimationAndShowSongTypesUI();
    }

    interface Presenter {
        void setView(View view);
        void init();
    }

    interface Model{
        void tryToLoadDataFromLocalFile();
    }
}
