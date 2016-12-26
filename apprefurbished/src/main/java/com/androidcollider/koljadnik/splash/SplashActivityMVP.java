package com.androidcollider.koljadnik.splash;


public interface SplashActivityMVP {

    interface View {
        void startAnimationAndShowSongTypesUI();
    }

    interface Presenter {
        void setView(View view);
        void init();
    }

    interface Model {
        void tryToLoadDataFromLocalFile();
    }
}
