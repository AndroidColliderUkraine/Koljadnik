package com.androidcollider.koljadnik.utils;

import android.app.Application;

import com.androidcollider.koljadnik.constants.Settings;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 * @project Koljadnik
 */
public class RealmInitializer {

    public static void initRealm(Application application) {
        Realm.init(application);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().
            schemaVersion(Settings.REALM_SCHEMA_VERSION).build());
    }
}
