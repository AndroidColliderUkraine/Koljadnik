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

import org.w3c.dom.Text;


public class SplashScreenActivity extends Activity {

    private ImageView iv_splash_title_main, iv_splash_title_hat;
    boolean closed = false;
    boolean fromOnCreate;
    private Animation slideUpMain, slideDownHat, fadeInAC;
    private TextView tv_ac, tv_loading_status;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = this;

        tv_loading_status = (TextView) findViewById(R.id.tv_loading_status);

        slideUpMain = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_hat);
        slideDownHat = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        fadeInAC = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

        iv_splash_title_main = (ImageView) findViewById(R.id.iv_splash_title_main);
        iv_splash_title_hat = (ImageView) findViewById(R.id.iv_splash_title_hat);
        tv_ac = (TextView) findViewById(R.id.tv_ac);


        iv_splash_title_main.setAnimation(fadeInAC);
        iv_splash_title_hat.setAnimation(slideDownHat);
        tv_ac.setAnimation(fadeInAC);


        slideDownHat.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                DataSource dataSource = new DataSource(context);
                if (dataSource.loadPref()){
                    if (InternetHelper.isConnectionEnabled(context)) {
                        DBupdater dBupdater = new DBupdater(context, "start");
                        dBupdater.checkAndUpdateTables();
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, SongTypesActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                } else {
                    if (InternetHelper.isConnectionEnabled(context)) {
                        dataSource.savePref(true);
                        DBupdater dBupdater = new DBupdater(context, "start");
                        dBupdater.checkAndUpdateTables();
                    } else {
                        showSettingsAlert();
                       /* DBupdater dBupdater = new DBupdater(context, "start");
                        dBupdater.checkAndUpdateTables();*/
                    }
                }




            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void setLoadingStatus(String status){
        tv_loading_status.setText(status);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Base_Theme_AppCompat));
        alertDialog.setTitle("Налаштування інтернету");
        alertDialog.setMessage("При першому запуску Колядника Вам необхідно завантажити базу пісень. Будь ласка, ввімкніть мережу натиснувши кнопку Налаштування");

        alertDialog.setNegativeButton("Відміна", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        alertDialog.setPositiveButton("Налаштування", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                SplashScreenActivity.this.startActivity(intent);
                finish();
            }
        });

        alertDialog.show();
    }

}
