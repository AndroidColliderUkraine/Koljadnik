package com.androidcollider.koljadnik;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidcollider.koljadnik.database.DataSource;


public class SongTypesActivity  extends Activity {

    private final static String TAG = "Андроідний Коллайдер";


    private String[] songTypesArray;
    private ListView lv_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG,"відкрилася 2-га активіті");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_types);

        initFields();

        DataSource dataSource = new DataSource(this);
        //dataSource.addCommentToLocal(1,"Крута пісня");
        songTypesArray = dataSource.getSongTypes();

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, songTypesArray);
        lv_category.setAdapter(adapter);

    }

    private void initFields(){
        lv_category = (ListView)findViewById(R.id.lv_category);
    }
}
