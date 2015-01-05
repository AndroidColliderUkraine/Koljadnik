package com.androidcollider.koljadnik;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidcollider.koljadnik.database.DataSource;
import com.androidcollider.koljadnik.objects.Song;


public class TextActivity extends Activity {

    private final static String TAG = "Андроідний Коллайдер";

    private TextView tv_song_title, tv_song_text, tv_song_remarks, tv_song_source, tv_song_comments;
    private DataSource dataSource;
    private Song song;

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

    private void initFields(){/*
        tv_song_title = (TextView)findViewById(R.id.tv_song_title);
        tv_song_title.setText(song.getName());*/
        getActionBar().setTitle(song.getName());

        tv_song_text = (TextView)findViewById(R.id.tv_song_text);
        tv_song_text.setText(song.getText());
/*
        tv_song_remarks = (TextView)findViewById(R.id.tv_song_remarks);
        tv_song_remarks.setText(song.getRemarks());*/

        tv_song_source = (TextView)findViewById(R.id.tv_song_source);
        tv_song_source.setText(song.getSource());

        /*tv_song_comments = (TextView)findViewById(R.id.tv_song_comments);
        if (song.getComments()!=null){
            for (String comment: song.getComments()){
                if (tv_song_comments.getText().toString()!=null){
                    tv_song_comments.setText(tv_song_comments.getText().toString()+"     "+ comment);
                }else {
                    tv_song_comments.setText(comment);
                }
            }
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_text, menu);
        //getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.route_saver_actionbar_background));
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
