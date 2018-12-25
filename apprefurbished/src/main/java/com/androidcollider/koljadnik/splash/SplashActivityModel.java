package com.androidcollider.koljadnik.splash;

import com.androidcollider.koljadnik.storage.SongsDataSource;

/**
 * @author Severyn Parkhomenko <pseverin@ukr.net>
 */
public class SplashActivityModel implements SplashActivityMVP.Model {

    private SongsDataSource songsDataSource;

    public SplashActivityModel(SongsDataSource songsDataSource) {
        this.songsDataSource = songsDataSource;
    }

    @Override
    public void tryToLoadDataFromLocalFile() {
        songsDataSource.tryToLoadDataFromLocalFile();
    }
}
