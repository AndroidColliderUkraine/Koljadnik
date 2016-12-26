package com.androidcollider.koljadnik.splash;

import com.androidcollider.koljadnik.song_types.SongTypesActivityMVP;
import com.androidcollider.koljadnik.song_types.SongTypesActivityModel;
import com.androidcollider.koljadnik.song_types.SongTypesActivityPresenter;
import com.androidcollider.koljadnik.storage.SongsDataSource;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashModule {

    @Provides
    public SplashActivityMVP.Presenter provideSplashActivityPresenter(SplashActivityMVP.Model model) {
        return new SplashActivityPresenter(model);
    }

    @Provides
    public SplashActivityMVP.Model provideSplashActivityModel(SongsDataSource repository) {
        return new SplashActivityModel(repository);
    }
}
