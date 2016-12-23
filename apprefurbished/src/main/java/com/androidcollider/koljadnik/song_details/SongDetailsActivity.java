package com.androidcollider.koljadnik.song_details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.common.CommonToolbarActivity;
import com.androidcollider.koljadnik.root.App;

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

    @BindView(R.id.btn_minus)
    View btnMinus;

    @BindView(R.id.btn_plus)
    View btnPlus;

    @Inject
    SongDetailsActivityMVP.Presenter presenter;

    private int songId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(EXTRA_SONG_ID)) {
            songId = getIntent().getIntExtra(EXTRA_SONG_ID, 0);
        }
        buildAndInjectComponent();
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

    @OnClick({R.id.btn_plus, R.id.btn_minus})
    public void onClickPlusMinus(View v) {
        switch (v.getId()) {
            case R.id.btn_plus:
                presenter.clickOnPlusBtn();
                break;
            case R.id.btn_minus:
                presenter.clickOnMinusBtn();
                break;
        }
    }

    @Override
    public void updateView(SongDetailsViewModel songDetailsViewModel) {
        tvText.setText(songDetailsViewModel.text);

        if (songDetailsViewModel.source != null && !songDetailsViewModel.source.isEmpty()) {
            tvSource.setText(songDetailsViewModel.source);
        } else {
            cntSource.setVisibility(View.GONE);
        }
    }

    @Override
    public void changeTextSize(int size, boolean showPlus, boolean showMinus) {
        tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        tvSource.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

        btnPlus.setVisibility(showPlus ? View.VISIBLE : View.INVISIBLE);
        btnMinus.setVisibility(showMinus ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showErrorToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
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
        startActivity(intent);
    }

    @Override
    public void startShareActivity(String shareActivityTitle, String shareTitle, String shareText) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                shareTitle);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                shareText);
        startActivity(Intent.createChooser(emailIntent,
                shareActivityTitle));
    }
}
