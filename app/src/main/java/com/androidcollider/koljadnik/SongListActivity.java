package com.androidcollider.koljadnik;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.androidcollider.koljadnik.adapters.SongAdapter;
import com.androidcollider.koljadnik.database.DataSource;
import com.androidcollider.koljadnik.objects.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SongListActivity extends Activity {

    private final static String TAG = "Андроідний Коллайдер";

    private ListView lv_songs_list;
    private EditText et_search_song;
    private ArrayList<Song> songList;
    private SongAdapter songAdapter;
    private DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        initFields();
        initListeners();
        dataSource = new DataSource(this);
        Intent intent = getIntent();

        Log.i("Parkh", intent.getIntExtra("SongType",-1)+"");
        songList = dataSource.getSongMainInfo(intent.getIntExtra("SongType",-1));
        for (Song song: songList){
            Log.i("T", song.getName());
        }

        songAdapter = new SongAdapter(this, songList);

        lv_songs_list.setAdapter(songAdapter);
        //sortByRating();
    }

    private void initFields(){
        lv_songs_list = (ListView)findViewById(R.id.lv_songs_list);
        et_search_song = (EditText)findViewById(R.id.et_search_song);
    }
    private void initListeners() {
        lv_songs_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idSong = songAdapter.getItem(position).getId();

                addOnePointToListRating(idSong);
                addOnePointToLocalDBRating(idSong);

                songAdapter.updateData(songList);

                Intent intent = new Intent(SongListActivity.this,TextActivity.class);
                intent.putExtra("Song", songAdapter.getItem(position));
                startActivity(intent);
            }
        });

        et_search_song.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                songAdapter.search(et_search_song.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void addOnePointToListRating(int idSong){
        for (Song song : songList) {
            if (song.getId() == idSong) {
                song.setRating(song.getRating() + 1);
            }
        }
        long minRating = songList.get(0).getRating();
        long maxRating = songList.get(0).getRating();
        for (Song song : songList) {
            long songRating = song.getRating();
            if (songRating>maxRating){
                maxRating=songRating;
            }
            if (songRating<minRating){
                minRating=songRating;
            }
        }
        Song.current_max_rating = maxRating;
        Song.current_min_rating = minRating;
    }

    private void addOnePointToLocalDBRating(int idSong){
        dataSource.addPointToLocalRating(idSong);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_song_list, menu);
        //getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.route_saver_actionbar_background));
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
        return true;
    }

    private void sortByName (){
        //Sorting
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {

                return song1.getName().compareTo(song2.getName());
            }
        });
        songAdapter.updateData(songList);
    }

    private void sortByRating (){
        //Sorting
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {

                return new Long(song1.getRating()).compareTo(new Long(song2.getRating()));
            }
        });
        songAdapter.updateData(songList);
    }

}
