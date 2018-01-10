package com.androidcollider.koljadnik.song_details;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.common.CommonToolbarActivity;
import com.androidcollider.koljadnik.custom_views.AutoscrollScrollView;
import com.androidcollider.koljadnik.root.App;
import com.androidcollider.koljadnik.utils.ChordUtils;
import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class SongDetailsActivity extends CommonToolbarActivity implements SongDetailsActivityMVP.View {

    public final static String EXTRA_SONG_ID = "song_id";
    private final static String SMS_EXTRA = "sms_body";

    @BindView(R.id.tv_text)
    TextView tvText;

    @BindView(R.id.tv_song_source)
    TextView tvSource;

    @BindView(R.id.cnt_source)
    View cntSource;

    @BindView(R.id.cnt_expanded_settings)
    View cntExpandedSettings;

    @BindView(R.id.tv_instruments_label)
    TextView tvInstrumentsLabel;

    @BindView(R.id.iv_instruments_label)
    ImageView ivInstrumentsLabel;

    @BindView(R.id.sv_scroll)
    AutoscrollScrollView svScroll;

    @BindView(R.id.sb_autoscroll)
    SeekBar sbAutoscroll;

    @BindView(R.id.sb_size)
    SeekBar sbSize;

    @BindView(R.id.cnt_chords)
    View cntChords;

    @Inject
    SongDetailsActivityMVP.Presenter presenter;

    private int songId;
    private boolean isInstrumentExpand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(EXTRA_SONG_ID)) {
            songId = getIntent().getIntExtra(EXTRA_SONG_ID, 0);
        }
        buildAndInjectComponent();

        sbAutoscroll.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.onAutoscrollChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.onSizeChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void buildAndInjectComponent() {
        DaggerSongDetailsComponent.builder()
            .appComponent(((App) getApplication()).getAppComponent())
            .songDetailsModule(new SongDetailsModule(songId))
            .build()
            .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.initData();
        presenter.onSizeChanged(sbSize.getProgress());
    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_text;
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_song_details;
    }

    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return true;
    }

    @OnClick({R.id.btn_chord_minus, R.id.btn_chord_plus, R.id.btn_hide_expand})
    public void onClickPlusMinus(View v) {
        switch (v.getId()) {
            case R.id.btn_chord_minus:
                presenter.clickOnChordPlusBtn();
                break;
            case R.id.btn_chord_plus:
                presenter.clickOnChordMinusBtn();
                break;
            case R.id.btn_hide_expand:
                isInstrumentExpand = !isInstrumentExpand;
                animateHideExpand();
                break;
        }
    }

    private void animateHideExpand() {
        int heightToAnimate = getResources().getDimensionPixelSize(R.dimen.minus_cnt_instruments_height) * (!isInstrumentExpand ? 0 : 1);
        cntExpandedSettings.animate()
            .translationY(heightToAnimate)
            .alpha(1.0f)
            .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    tvInstrumentsLabel.setText(isInstrumentExpand ? R.string.hide_instruments : R.string.show_instruments);
                    ivInstrumentsLabel.setImageResource(isInstrumentExpand ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_up);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
    }

    @Override
    public void updateView(SongDetailsViewModel songDetailsViewModel) {
        updateText(songDetailsViewModel.text);
        setToolbarTitle(songDetailsViewModel.name);

        if (songDetailsViewModel.source != null && !songDetailsViewModel.source.isEmpty()) {
            tvSource.setText(songDetailsViewModel.source);
        } else {
            cntSource.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateText(String songText) {
        tvText.setText(songText.replace(ChordUtils.CHORD_TAG_OPEN,"").replace(ChordUtils.CHORD_TAG_CLOSE,""));
    }

    @Override
    public void updateChordBlockVisibility(boolean isVisible) {
        cntChords.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void changeTextSize(int size, boolean showPlus, boolean showMinus) {
        tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        tvSource.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

        /*btnPlus.setVisibility(showPlus ? View.VISIBLE : View.INVISIBLE);
        btnMinus.setVisibility(showMinus ? View.VISIBLE : View.INVISIBLE);*/
    }

    @Override
    public void showErrorToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.share_song:
                presenter.clickOnShareBtn();
                break;
            case R.id.sms_song:
                presenter.clickOnSmsBtn();
                break;
        }
        return true;
    }

    /*private void shareSong(){
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");

        // Зачем
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Колядка - "+song.getName());
        // О чём
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                song.getText()+getString(R.string.slava_ukraini));
        // С чем
        *//*emailIntent.putExtra(
                android.content.Intent.EXTRA_STREAM,
                Uri.parse("file://"
                        + Environment.getExternalStorageDirectory()
                        + "/Клипы/SOTY_ATHD.mp4"));*//*

        //emailIntent.setType("text/video");
        // Поехали!
        startActivity(Intent.createChooser(emailIntent,
                "Відправка пісні..."));
    }


    private void sendSms(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
        intent.putExtra( "sms_body", song.getText()+getString(R.string.slava_ukraini));
        startActivity(intent);
    }*/

    @Override
    public void startSmsActivity(String smsText) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
        intent.putExtra(SMS_EXTRA, smsText);
        try {
            startActivity(intent);
        } catch (Exception e) {
            showErrorToast(getString(R.string.unable_to_make_current_operation));
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }
    }

    @Override
    public void startShareActivity(String shareActivityTitle, String shareTitle, String shareText) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
            shareTitle);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
            shareText);

        try {
            startActivity(Intent.createChooser(emailIntent,
                shareActivityTitle));
        } catch (Exception e) {
            showErrorToast(getString(R.string.unable_to_make_current_operation));
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }
    }

    @Override
    public void updateScrollSpeed(int speed) {
        svScroll.setScrollSpeed(speed);
    }
}
