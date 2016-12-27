package com.androidcollider.koljadnik.root;

import android.content.Context;

import com.androidcollider.koljadnik.common.CommonActivity;
import com.androidcollider.koljadnik.feedback.FeedbackActivity;
import com.androidcollider.koljadnik.feedback.FeedbackModule;
import com.androidcollider.koljadnik.song_types.SongTypesActivity;
import com.androidcollider.koljadnik.song_types.SongTypesModule;
import com.androidcollider.koljadnik.splash.SplashModule;
import com.androidcollider.koljadnik.splash.SplashScreenActivity;
import com.androidcollider.koljadnik.storage.SharedPreferencesModule;
import com.androidcollider.koljadnik.storage.SongsDataSource;
import com.androidcollider.koljadnik.storage.SongsRepositoryModule;
import com.androidcollider.koljadnik.utils.SessionSettingsManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        SharedPreferencesModule.class,
        SongsRepositoryModule.class,

        SongTypesModule.class,
        FeedbackModule.class,
        SplashModule.class})

public interface AppComponent {

    SongsDataSource getSongRepository();

    SessionSettingsManager getLocalSettingsManager();

    Context getContext();

    void inject(SongTypesActivity songTypesActivity);

    void inject(FeedbackActivity feedbackActivity);

    void inject(SplashScreenActivity splashScreenActivity);

    void inject(CommonActivity commonActivity);
}
