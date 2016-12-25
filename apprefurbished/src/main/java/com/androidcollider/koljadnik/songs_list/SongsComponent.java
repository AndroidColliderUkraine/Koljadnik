package com.androidcollider.koljadnik.songs_list;

import com.androidcollider.koljadnik.root.AppComponent;
import com.androidcollider.koljadnik.utils.ActivityScoped;

import dagger.Component;

@ActivityScoped
@Component(dependencies = {AppComponent.class}, modules = {SongsModule.class})
public interface SongsComponent {
    void inject(SongsActivity songsActivity);
}
