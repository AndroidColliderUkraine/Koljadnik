package com.androidcollider.koljadnik.storage.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.androidcollider.koljadnik.contants.Settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferencesModule {

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(Settings.SPREF, Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    SharedPreferencesManager provideSharedPreferencesManager(SharedPreferences sharedPreferences) {
        return new SharedPreferencesManager(sharedPreferences);
    }
}
