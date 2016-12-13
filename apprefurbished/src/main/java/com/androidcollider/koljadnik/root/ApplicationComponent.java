package com.androidcollider.koljadnik.root;

import com.androidcollider.koljadnik.song_types.SongTypesActivity;
import com.androidcollider.koljadnik.song_types.SongTypesModule;
import com.androidcollider.koljadnik.storage.SongsRepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        SongTypesModule.class,
        SharedPreferencesModule.class,
        SongsRepositoryModule.class})

public interface ApplicationComponent {

    void inject(SongTypesActivity songTypesActivity);
}
