package com.androidcollider.koljadnik.root;

import android.app.Application;
import android.content.Context;

import com.androidcollider.koljadnik.utils.SessionSettingsManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    public SessionSettingsManager provideLocalSettingsManager() {
        return new SessionSettingsManager();
    }
}
