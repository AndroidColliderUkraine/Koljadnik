package com.androidcollider.koljadnik.songs;

import com.androidcollider.koljadnik.storage.SongsDataSource;

import dagger.Module;
import dagger.Provides;

@Module
public class SongsModule {

    @Provides
    public SongsActivityMVP.Presenter provideSongsActivityPresenter(SongsActivityMVP.Model model) {
        return new SongsActivityPresenter(model);
    }

    @Provides
    public SongsActivityMVP.Model provideSongTypesActivityModel(SongsDataSource repository) {
        return new SongsActivityModel(repository);
    }

    /*@Singleton
    @Provides
    public SongsDataSource provideSongRepository(SongsRealmDataSource songsRealmDataSource,
                                                 SongsFirebaseDataSource songsFirebaseDataSource,
                                                 SharedPreferencesManager sharedPreferencesManager) {
        return new SongsRepository(songsRealmDataSource, songsFirebaseDataSource, sharedPreferencesManager);
    }*/
}
