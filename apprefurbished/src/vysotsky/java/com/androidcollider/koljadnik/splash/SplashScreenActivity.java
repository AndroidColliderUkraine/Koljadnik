package com.androidcollider.koljadnik.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.common.CommonActivity;
import com.androidcollider.koljadnik.root.App;
import com.androidcollider.koljadnik.song_types.SongTypesActivity;
import com.androidcollider.koljadnik.songs_list.SongsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;


public class SplashScreenActivity extends CommonActivity implements SplashActivityMVP.View {

    @BindView(R.id.cnt_main)
    View cntMain;

    @Inject
    SplashActivityMVP.Presenter presenter;


    @Override
    protected int getContentViewRes() {
        return R.layout.activity_splash_screen;
    }

    @Override
    public void startAnimationAndShowSongTypesUI() {
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(2000);
        cntMain.setAnimation(fadeIn);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(SplashScreenActivity.this, SongsActivity.class);
                    intent.putExtra(SongsActivity.EXTRA_SONG_TYPE_ID, 0);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
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
