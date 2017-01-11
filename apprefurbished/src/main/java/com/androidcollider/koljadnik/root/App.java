package com.androidcollider.koljadnik.root;

import android.app.Application;
import android.support.v4.BuildConfig;

import com.androidcollider.koljadnik.contants.Settings;
import com.androidcollider.koljadnik.utils.KoljadnikRealmMigration;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Description of ${CLASS_NAME}
 *
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 * @package com.androidcollider.koljadnik
 */
public class App extends Application/*MultiDexApplication*/ {

    private Tracker tracker;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        appComponent = DaggerAppComponent.builder().applicationModule(new ApplicationModule(this)).build();

        if (!BuildConfig.DEBUG) {
            getDefaultTracker();
        }

        Fabric.with(this, new Crashlytics(), new Answers());
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.INFO);

        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().
                schemaVersion(Settings.REALM_SCHEMA_VERSION).
                migration(new KoljadnikRealmMigration()).build());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            tracker = analytics.newTracker(Settings.GOOGLE_ANALYTICS_TRACKING_ID);

            // Provide unhandled exceptions reports. Do that first after creating the tracker
            tracker.enableExceptionReporting(true);

            // Enable Remarketing, Demographics & Interests reports
            // https://developers.google.com/analytics/devguides/collection/android/display-features
            tracker.enableAdvertisingIdCollection(true);

            // Enable automatic activity tracking for your app
            tracker.enableAutoActivityTracking(true);
        }
        return tracker;
    }
}
