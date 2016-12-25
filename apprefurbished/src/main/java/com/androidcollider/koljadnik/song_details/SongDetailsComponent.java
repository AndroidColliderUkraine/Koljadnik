package com.androidcollider.koljadnik.song_details;

import com.androidcollider.koljadnik.root.AppComponent;
import com.androidcollider.koljadnik.utils.ActivityScoped;

import dagger.Component;

@ActivityScoped
@Component(dependencies = {AppComponent.class}, modules = {SongDetailsModule.class})
public interface SongDetailsComponent {
    void inject(SongDetailsActivity songDetailsActivity);
}
