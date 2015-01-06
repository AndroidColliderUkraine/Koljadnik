package com.androidcollider.koljadnik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.androidcollider.koljadnik.adapters.SongAdapter;
import com.androidcollider.koljadnik.adapters.SortTypeAdapter;
import com.androidcollider.koljadnik.database.DataSource;
import com.androidcollider.koljadnik.objects.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SongListActivity extends Activity {

    private final static String TAG = "Андроідний Коллайдер";

    private ListView lv_songs_list, lv_sort_types;
    private EditText et_search_song;
    private ArrayList<Song> songList;
    private SongAdapter songAdapter;
    private DataSource dataSource;
    private ArrayList<String> sortTypeArrayList;
    private ImageView iv_search_sort;
    private String typeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        sortTypeArrayList = new ArrayList<>();
        sortTypeArrayList.add("За алфавітом");
        sortTypeArrayList.add("За рейтингом");

        dataSource = new DataSource(this);
        Intent intent = getIntent();

        typeName = intent.getStringExtra("SongTypeName");
        initFields();
        initListeners();

        songList = dataSource.getSongMainInfo(intent.getIntExtra("SongType",-1));
        for (Song song: songList){
            Log.i("T", song.getName());
        }

        songAdapter = new SongAdapter(this, songList);

        lv_songs_list.setAdapter(songAdapter);
        sortByRating();
        SortTypeAdapter sortTypeAdapter = new SortTypeAdapter(this, sortTypeArrayList);
        lv_sort_types.setAdapter(sortTypeAdapter);
    }

    private void initFields(){
        getActionBar().setTitle(typeName);
        lv_songs_list = (ListView)findViewById(R.id.lv_songs_list);
        et_search_song = (EditText)findViewById(R.id.et_search_song);
        lv_sort_types = (ListView)findViewById(R.id.lv_sort_types);
        iv_search_sort = (ImageView)findViewById(R.id.iv_search_sort);
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

        lv_sort_types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,songList.toString());
                switch (position){
                    case 0:
                        sortByName();
                        showHideSortTypes(false);
                        Log.i(TAG,songList.toString());
                        break;

                    case 1:
                        sortByRating();
                        showHideSortTypes(false);
                        Log.i(TAG,songList.toString());
                        break;
                }
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
        iv_search_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv_sort_types.getVisibility()==View.VISIBLE){
                    showHideSortTypes(false);
                }else {
                    showHideSortTypes(true);
                }
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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_song_list, menu);
        //getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.route_saver_actionbar_background));
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);

        if (item.getItemId()==R.id.add_song){
            Intent intent = new Intent(this, SubmitActivity.class);
            startActivity(intent);
        }
        if(item.getItemId()==android.R.id.home){
            finish();
        }
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

                return new Long(song2.getRating()).compareTo(new Long(song1.getRating()));
            }
        });
        songAdapter.updateData(songList);
    }

    private void showHideSortTypes(boolean isShow){
        if(isShow){
            lv_sort_types.setVisibility(View.VISIBLE);
        } else {
            lv_sort_types.setVisibility(View.GONE);
        }

    }

}
