package com.androidcollider.koljadnik.song_types;

import com.androidcollider.koljadnik.root.SharedPreferencesManager;
import com.androidcollider.koljadnik.storage.SongsDataSource;
import com.androidcollider.koljadnik.storage.SongsFirebaseDataSource;
import com.androidcollider.koljadnik.storage.SongsRealmDataSource;
import com.androidcollider.koljadnik.storage.SongsRepository;

import javax.inject.Singleton;

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

    @Singleton
    @Provides
    public SongsDataSource provideSongRepository(SongsRealmDataSource songsRealmDataSource,
                                                 SongsFirebaseDataSource songsFirebaseDataSource,
                                                 SharedPreferencesManager sharedPreferencesManager) {
        return new SongsRepository(songsRealmDataSource, songsFirebaseDataSource, sharedPreferencesManager);
    }
}
