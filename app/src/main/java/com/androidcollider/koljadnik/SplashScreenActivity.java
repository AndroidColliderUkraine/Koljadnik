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
import android.widget.ImageView;
import android.widget.TextView;

import com.androidcollider.koljadnik.database.DBupdater;
import com.androidcollider.koljadnik.database.DataSource;
import com.androidcollider.koljadnik.utils.InternetHelper;


public class SplashScreenActivity extends Activity {

    private ImageView iv_splash_title_main, iv_splash_title_hat;
    boolean closed = false;
    boolean fromOnCreate;
    private Animation slideUpMain, slideDownHat, fadeInAC;
    private TextView tv_ac;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = this;

        slideUpMain = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_hat);
        slideDownHat = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        fadeInAC = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

        iv_splash_title_main = (ImageView) findViewById(R.id.iv_splash_title_main);
        iv_splash_title_hat = (ImageView) findViewById(R.id.iv_splash_title_hat);
        tv_ac = (TextView)findViewById(R.id.tv_ac);


        iv_splash_title_main.setAnimation(fadeInAC);
        iv_splash_title_hat.setAnimation(slideDownHat);
        tv_ac.setAnimation(fadeInAC);


        slideDownHat.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (InternetHelper.isConnectionEnabled(context)){
                    DBupdater dBupdater = new DBupdater(context);
                    dBupdater.checkAndUpdateTables();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, SongTypesActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }




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
