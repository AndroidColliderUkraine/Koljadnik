package com.androidcollider.koljadnik.contants;

import com.androidcollider.koljadnik.BuildConfig;

import java.util.concurrent.TimeUnit;

public class Settings {

    public static final String GOOGLE_ANALYTICS_TRACKING_ID = "UA-58437769-1";
    public final static String SPREF = "koljadnik_pref";
    public final static long DELTA_TIME_FOR_UPDATE = (BuildConfig.DEBUG) ? TimeUnit.MINUTES.toMillis(3) : TimeUnit.DAYS.toMillis(1);
    public final static int SEARCH_LIMIT = 3;
    public final static int DEFAULT_RATING = 4;
    public final static int RATING_DEFAULT_LIMIT = 20;
    public final static String[] FEEDBACK_MAILS = new String[]{"android.collider@gmail.com", "pseverin@ukr.net"};
}
