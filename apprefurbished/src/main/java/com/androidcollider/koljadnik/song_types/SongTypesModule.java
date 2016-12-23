package com.androidcollider.koljadnik.song_types;

import com.androidcollider.koljadnik.storage.SongsDataSource;

import dagger.Module;
import dagger.Provides;

@Module
public class SongTypesModule {

    @Provides
    public SongTypesActivityMVP.Presenter provideSongTypesActivityPresenter(SongTypesActivityMVP.Model model) {
        return new SongTypesActivityPresenter(model);
    }

    @Provides
    public SongTypesActivityMVP.Model provideSongTypesActivityModel(SongsDataSource repository) {
        return new SongTypesActivityModel(repository);
    }


}
