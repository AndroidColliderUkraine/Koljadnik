package com.androidcollider.koljadnik.storage;


import android.content.Context;

import com.androidcollider.koljadnik.storage.files.AssetsTextDataManager;
import com.androidcollider.koljadnik.storage.local.SongsLocalDataSource;
import com.androidcollider.koljadnik.storage.local.SongsRealmDataSource;
import com.androidcollider.koljadnik.storage.remote.SongsFirebaseDataSource;
import com.androidcollider.koljadnik.storage.remote.SongsRemoteDataSource;
import com.androidcollider.koljadnik.storage.shared_prefs.SharedPreferencesManager;
import com.androidcollider.koljadnik.utils.ConnectionInternetManager;

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
    SongsLocalDataSource provideSongsRealmDataSource() {
        return new SongsRealmDataSource(Realm.getDefaultInstance());
    }

    @Singleton
    @Provides
    SongsRemoteDataSource provideSongsFirebaseDataSource() {
        return new SongsFirebaseDataSource();
    }

    @Singleton
    @Provides
    public SongsDataSource provideSongRepository(SongsLocalDataSource songsRealmDataSource,
                                                 SongsRemoteDataSource songsFirebaseDataSource,
                                                 SharedPreferencesManager sharedPreferencesManager,
                                                 Context context) {
        return new SongsRepository(
                songsRealmDataSource,
                songsFirebaseDataSource,
                sharedPreferencesManager,
                new ConnectionInternetManager(context),
                new AssetsTextDataManager(context));
    }
}
