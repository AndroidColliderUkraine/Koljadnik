package com.androidcollider.koljadnik.contants;

import com.androidcollider.koljadnik.BuildConfig;

import java.util.concurrent.TimeUnit;

public class Settings {

    public final static String SPREF = "koljadnik_pref";
    public final static long DELTA_TIME_FOR_UPDATE = (BuildConfig.DEBUG) ? TimeUnit.SECONDS.toMillis(30) : TimeUnit.HOURS.toMillis(3);
    public final static int SEARCH_LIMIT = 3;
    public final static String[] FEEDBACK_MAILS = new String[]{"android.collider@gmail.com", "pseverin@ukr.net"};
}
