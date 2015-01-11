package com.androidcollider.koljadnik;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class SongTypesActivity  extends Activity {

    private final static String TAG = "Андроідний Коллайдер";
    private final static int UPDATE_DATA_TIME = 1000*3600;

    private ArrayList<SongType> songTypesList;
    private ListView lv_category;
    private SongTypeAdapter songTypeAdapter;
    private Timer t;
    private DBupdater dBupdater;

    private DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendDataToAnalytics();

        setContentView(R.layout.activity_song_types);

        dBupdater = new DBupdater(this, "timer");
        startTimerUpdating();
        Log.d("RESPONSE is finished", "     "+ AppController.getInstance().isRequestQoeueFinished());
        initFields();
        initListeners();

        dataSource = new DataSource(this);
        songTypesList = dataSource.getSongTypes();

        songTypeAdapter = new SongTypeAdapter(this,songTypesList);
        lv_category.setAdapter(songTypeAdapter);

        registerReceiver(broadcastReceiver, new IntentFilter("update_types"));
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
                intent.putExtra("SongTypeName", songTypeAdapter.getItem(position).getName());
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
        if (Build.VERSION.SDK_INT>10) {
            if (getActionBar() != null) {
                getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
            }
        }
        return true;
    }


    private void startTimerUpdating() {
        //Declare the timer
        t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      dBupdater.checkAndUpdateTables();
                                  }

                              },
                //Set how long before to start calling the TimerTask (in milliseconds)
                UPDATE_DATA_TIME,
                //Set the amount of time between each execution (in milliseconds)
                UPDATE_DATA_TIME);
    }

    @Override
    protected void onDestroy() {
        t.cancel();
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("update_types")){
                Log.i(TAG + " будуємо типи","");
                ArrayList<SongType> songTypesList = dataSource.getSongTypes();
                songTypeAdapter.updateData(songTypesList);
            }
        }
    };

    private void sendDataToAnalytics(){
        // Get tracker.
        Tracker t = ((AppController) getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("SongTypeActivity");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
