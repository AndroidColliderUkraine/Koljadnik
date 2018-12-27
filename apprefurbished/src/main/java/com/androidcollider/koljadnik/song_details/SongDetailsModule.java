package com.androidcollider.koljadnik.song_details;

import android.content.Context;

import com.androidcollider.koljadnik.storage.SongsDataSource;
import com.androidcollider.koljadnik.utils.ActivityScoped;
import com.androidcollider.koljadnik.utils.LocationManager;
import com.androidcollider.koljadnik.utils.SessionSettingsManager;

import dagger.Module;
import dagger.Provides;

@Module
public class SongDetailsModule {

    private int songId;

    public SongDetailsModule(int songId) {
        this.songId = songId;
    }

    @Provides
    int provideSongId() {
        return songId;
    }

    @Provides
    @ActivityScoped
    public SongDetailsActivityMVP.Presenter provideSongDetailsActivityPresenter(SongDetailsActivityMVP.Model model,
                                                                                SessionSettingsManager sessionSettingsManager,
                                                                                LocationManager locationManager) {
        return new SongDetailsActivityPresenter(model, sessionSettingsManager, locationManager);
    }

    @Provides
    public SongDetailsActivityMVP.Model provideSongDetailsActivityModel(Context context, SongsDataSource repository, int songId) {
        return new SongDetailsActivityModel(context, repository, songId);
    }
}
