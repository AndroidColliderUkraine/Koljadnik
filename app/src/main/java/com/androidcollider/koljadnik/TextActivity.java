package com.androidcollider.koljadnik;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidcollider.koljadnik.database.DataSource;
import com.androidcollider.koljadnik.objects.Song;


public class TextActivity extends Activity {

    private final static String TAG = "Андроідний Коллайдер";

    private TextView tv_song_title, tv_song_text, tv_song_remarks, tv_song_source, tv_song_comments;
    private DataSource dataSource;
    private Song song;
    private ImageView iv_minus, iv_plus;
    private int textSize=14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        Intent intent = getIntent();
        song = intent.getParcelableExtra("Song");
        dataSource = new DataSource(this);
        song = dataSource.getSongAdvancedInfo(song);
        Log.i(TAG,song.toString());
        initFields();
    }

    private void initFields(){
        tv_song_text = (TextView)findViewById(R.id.tv_song_text);
        tv_song_text.setText(song.getText());

        if (song.getSource()==null||song.getSource().isEmpty()){
            findViewById(R.id.tv_song_source).setVisibility(View.GONE);
            findViewById(R.id.tv_dzherelo).setVisibility(View.GONE);
        } else {
            tv_song_source = (TextView)findViewById(R.id.tv_song_source);
            tv_song_source.setText(song.getSource());
        }

        iv_minus = (ImageView)findViewById(R.id.iv_minus);
        iv_plus = (ImageView)findViewById(R.id.iv_plus);

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textSize<20){
                    textSize++;
                    if(song.getSource()!=null&!song.getSource().isEmpty()){
                        tv_song_source.setTextSize(textSize);
                    }
                    tv_song_text.setTextSize(textSize);
                    ((TextView)findViewById(R.id.tv_dzherelo)).setTextSize(textSize+1);
                }
            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textSize>12){
                    textSize--;
                    if(song.getSource()!=null&!song.getSource().isEmpty()){
                        tv_song_source.setTextSize(textSize);
                    }
                    tv_song_text.setTextSize(textSize);
                    ((TextView)findViewById(R.id.tv_dzherelo)).setTextSize(textSize+1);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_text, menu);
        if (Build.VERSION.SDK_INT>10){
            if (getActionBar()!=null){
                getActionBar().setTitle(song.getName());
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }
            getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.share_song){
            shareSong();
        }
        if (item.getItemId()==R.id.sms_song){
            sendSms();
        }
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
    private void shareSong(){
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");

        // Зачем
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Колядка - "+song.getName());
        // О чём
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                song.getText()+getString(R.string.slava_ukraini));
        // С чем
        /*emailIntent.putExtra(
                android.content.Intent.EXTRA_STREAM,
                Uri.parse("file://"
                        + Environment.getExternalStorageDirectory()
                        + "/Клипы/SOTY_ATHD.mp4"));*/

        //emailIntent.setType("text/video");
        // Поехали!
        startActivity(Intent.createChooser(emailIntent,
                "Відправка пісні..."));
    }
    private void sendSms(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
        intent.putExtra( "sms_body", song.getText()+getString(R.string.slava_ukraini));
        startActivity(intent);
    }
}
