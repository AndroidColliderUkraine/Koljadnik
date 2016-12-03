package com.androidcollider.koljadnik.root;

import com.androidcollider.koljadnik.song_types.SongTypesActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SongTypesActivity songTypesActivity);
}
