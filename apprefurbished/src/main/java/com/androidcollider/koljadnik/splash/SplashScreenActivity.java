package com.androidcollider.koljadnik.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.common.CommonActivity;
import com.androidcollider.koljadnik.root.App;
import com.androidcollider.koljadnik.song_types.SongTypesActivity;

import javax.inject.Inject;

import butterknife.BindView;


public class SplashScreenActivity extends CommonActivity implements SplashActivityMVP.View {

    @BindView(R.id.iv_splash_title_main)
    ImageView iv_splash_title_main;

    @BindView(R.id.iv_splash_title_hat)
    ImageView iv_splash_title_hat;

    @BindView(R.id.tv_ac)
    TextView tv_ac;

    @Inject
    SplashActivityMVP.Presenter presenter;


    @Override
    protected int getContentViewRes() {
        return R.layout.activity_splash_screen;
    }

    @Override
    public void startAnimationAndShowSongTypesUI() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.init();
    }
}
