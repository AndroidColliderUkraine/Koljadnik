package com.androidcollider.koljadnik.storage;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * This is used by Dagger to inject the required arguments into the {@link SongsDataSource}.
 */
@Module
public class SongsRepositoryModule {

    @Singleton
    @Provides
    //@Local
    SongsRealmDataSource provideSongsRealmDataSource() {
        return new SongsRealmDataSource(Realm.getDefaultInstance());
    }

    @Singleton
    @Provides
    //@Remote
    SongsFirebaseDataSource provideSongsFirebaseDataSource() {
        return new SongsFirebaseDataSource();
    }

    @Singleton
    @Provides
    public SongsDataSource provideSongRepository(SongsRealmDataSource songsRealmDataSource,
                                                 SongsFirebaseDataSource songsFirebaseDataSource,
                                                 SharedPreferencesManager sharedPreferencesManager,
                                                 Context context) {
        return new SongsRepository(songsRealmDataSource, songsFirebaseDataSource, sharedPreferencesManager, new AssetsTextDataManager(context));
    }
}
