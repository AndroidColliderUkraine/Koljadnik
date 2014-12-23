package com.androidcollider.koljadnik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.androidcollider.koljadnik.database.DBupdater;
import com.androidcollider.koljadnik.database.DataSource;
import com.androidcollider.koljadnik.utils.InternetHelper;


public class SplashScreenActivity extends Activity {

    private TextView tv_splash_title;
    boolean closed = false;
    boolean fromOnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        tv_splash_title = (TextView) findViewById(R.id.tv_splash_title);
        tv_splash_title.setAnimation(slideUp);

        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (InternetHelper.isConnectionEnabled(getApplicationContext())){
                    DBupdater dBupdater = new DBupdater(getApplicationContext());
                    dBupdater.checkAndUpdateTables();
                }


                Intent intent = new Intent(SplashScreenActivity.this, SongTypesActivity.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(SplashScreenActivity.this, CategoryActivity.class);
                finish();
                startActivity(myIntent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                if (!closed) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    boolean gpsPresent = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    boolean networkProviderPresent = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if ((gpsPresent) && (networkProviderPresent)) {
                        Intent myIntent = new Intent(SplashScreen.this, LogInActivity.class);
                        finish();
                        startActivity(myIntent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    } else {
                        showSettingsAlert();
                    }
                }
            }
        }, 3500);*/
        //fromOnCreate = true;
    }

    /*@Override
    public void onBackPressed() {
        closed = true;
        finish();
    }*/

    /*@Override
    protected void onResume() {
        super.onResume();
        if (!fromOnCreate) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gpsPresent = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkProviderPresent = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if ((gpsPresent) && (networkProviderPresent)) {
                Intent myIntent = new Intent(SplashScreen.this, LogInActivity.class);
                finish();
                startActivity(myIntent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                showSettingsAlert();
            }
        } else {
            fromOnCreate = false;
        }
    }*/

    /*public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppBaseTheme));
        alertDialog.setTitle("Configure GPS");
        alertDialog.setMessage("To use this application you need to turn on your GPS.Please enable it by clicking configuration button !!");

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        alertDialog.setPositiveButton("Configuration", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                SplashScreen.this.startActivity(intent);
            }
        });

        alertDialog.show();
    }*/


}
