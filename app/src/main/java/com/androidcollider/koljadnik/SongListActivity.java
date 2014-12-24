package com.androidcollider.koljadnik;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.androidcollider.koljadnik.adapters.SongAdapter;
import com.androidcollider.koljadnik.database.DataSource;
import com.androidcollider.koljadnik.objects.Song;

import java.util.ArrayList;


public class SongListActivity extends Activity {

    private ListView lv_songs_list;
    private ArrayList<Song> songList;
    private SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        initFields();
        DataSource dataSource = new DataSource(this);
        Intent intent = new Intent();

        Log.i("Parkh", intent.getIntExtra("SongType", 0)+"");
        songList = dataSource.getSongMainInfo(intent.getIntExtra("SongType",0));
        for (Song song: songList){
            Log.i("Parkh", song.getName());
        }

        songAdapter = new SongAdapter(this, songList);

        lv_songs_list.setAdapter(songAdapter);
    }

    private void initFields(){
        lv_songs_list = (ListView)findViewById(R.id.lv_songs_list);
    }


}
