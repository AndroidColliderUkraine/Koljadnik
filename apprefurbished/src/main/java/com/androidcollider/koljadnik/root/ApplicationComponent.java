package com.androidcollider.koljadnik.root;

import com.androidcollider.koljadnik.song_types.SongTypesActivity;
import com.androidcollider.koljadnik.song_types.SongTypesModule;
import com.androidcollider.koljadnik.songs.SongsActivity;
import com.androidcollider.koljadnik.songs.SongsModule;
import com.androidcollider.koljadnik.storage.SongsRepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        SongTypesModule.class,
        SharedPreferencesModule.class,
        SongsRepositoryModule.class,
        SongsModule.class})

public interface ApplicationComponent {

    void inject(SongTypesActivity songTypesActivity);

    void inject(SongsActivity songsActivity);
}
