package com.androidcollider.koljadnik.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.song_types.SongTypesActivity;


public class SplashScreenActivity extends Activity {

    ImageView iv_splash_title_main;
    ImageView iv_splash_title_hat;
    TextView tv_ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        iv_splash_title_main = (ImageView) findViewById(R.id.iv_splash_title_main);
        iv_splash_title_hat = (ImageView) findViewById(R.id.iv_splash_title_hat);
        tv_ac = (TextView) findViewById(R.id.tv_ac);


        Animation slideDownHat = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

        iv_splash_title_main.setAnimation(fadeIn);
        iv_splash_title_hat.setAnimation(slideDownHat);
        tv_ac.setAnimation(fadeIn);


        slideDownHat.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreenActivity.this, SongTypesActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
