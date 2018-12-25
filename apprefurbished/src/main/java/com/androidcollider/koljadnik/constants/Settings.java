package com.androidcollider.koljadnik.constants;

import com.androidcollider.koljadnik.BuildConfig;

import java.util.concurrent.TimeUnit;

public class Settings {

    public final static int REALM_SCHEMA_VERSION = DynamicSettings.REALM_SCHEMA_VERSION;
    public final static String SPREF = DynamicSettings.SPREF;
    public static final String GOOGLE_ANALYTICS_TRACKING_ID = DynamicSettings.GOOGLE_ANALYTICS_TRACKING_ID;

    public final static long DELTA_TIME_FOR_UPDATE = (BuildConfig.DEBUG) ? TimeUnit.MINUTES.toMillis(3) : TimeUnit.DAYS.toMillis(1);
    public final static long DELTA_TIME_FOR_UPDATE_RATING = (BuildConfig.DEBUG) ? TimeUnit.MINUTES.toMillis(1) : TimeUnit.HOURS.toMillis(6);
    public final static int SEARCH_LIMIT = 3;
    public final static int DEFAULT_RATING = 4;
    public final static int RATING_DEFAULT_LIMIT = 20;
    public final static String[] FEEDBACK_MAILS = new String[]{"android.collider@gmail.com", "pseverin@ukr.net"};
}
