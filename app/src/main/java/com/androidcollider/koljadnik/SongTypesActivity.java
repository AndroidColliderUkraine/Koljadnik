package com.androidcollider.koljadnik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidcollider.koljadnik.adapters.SongTypeAdapter;
import com.androidcollider.koljadnik.database.DBupdater;
import com.androidcollider.koljadnik.database.DataSource;
import com.androidcollider.koljadnik.objects.SongType;
import com.androidcollider.koljadnik.utils.AppController;
import com.androidcollider.koljadnik.utils.InternetHelper;

import java.util.ArrayList;


public class SongTypesActivity  extends Activity {

    private final static String TAG = "Андроідний Коллайдер";

    private ArrayList<SongType> songTypesList;
    private ListView lv_category;
    private SongTypeAdapter songTypeAdapter;

    private DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG,"відкрилася 2-га активіті");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_types);
        Log.d("RESPONSE is finished", "     "+ AppController.getInstance().isRequestQoeueFinished());
        initFields();
        initListeners();

        dataSource = new DataSource(this);
        //dataSource.addCommentToLocal(1,"Крута пісня");
        songTypesList = dataSource.getSongTypes();

        songTypeAdapter = new SongTypeAdapter(this,songTypesList);
        lv_category.setAdapter(songTypeAdapter);

    }

    private void initFields(){
        lv_category = (ListView)findViewById(R.id.lv_category);
    }
    private void initListeners(){
        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SongListActivity.class);
                int idType =  songTypeAdapter.getItem(position).getId();
                Log.i(TAG + " peredaemo id", idType + "");
                intent.putExtra("SongType", idType);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (InternetHelper.isConnectionEnabled(this)){
            DBupdater dBupdater = new DBupdater(this,"finish");
            dBupdater.checkAndUpdateTables();
        } else {
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_song_types, menu);
        //getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.route_saver_actionbar_background));
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
        return true;
    }
}
