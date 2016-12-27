package com.androidcollider.koljadnik.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Description of ConnectionInternetManager
 *
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 * @package com.defietser.managers
 */

public final class ConnectionInternetManager {

    private Context mContext;

    public ConnectionInternetManager(Context context) {
        this.mContext = context;
    }


    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnected();
    }
}
