package com.androidcollider.koljadnik.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.SongTypesActivity;
import com.androidcollider.koljadnik.SplashScreenActivity;
import com.androidcollider.koljadnik.database.local_db.DBhelperLocalDB;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pseverin on 23.12.14.
 */
public class DBupdater {

    private final static String TAG = "Андроідний Коллайдер";

    private DataSource dataSource;
    private Context context;

    private final static String[] tables = new String[]{"Song", "SongType", "Text", "Chord", "Note", "Comment"};

    public DBupdater(Context context) {
        this.context = context;
        Parse.initialize(context, context.getString(R.string.parse_application_id),
                context.getString(R.string.parse_client_key));
        dataSource = new DataSource(context);
    }


    public void checkAndUpdateTables() {

        ArrayList<Long> localUpdates = dataSource.getLocalUpdates();
        ArrayList<Long> serverUpdates = dataSource.getServerUpdates();

        Log.i(TAG, localUpdates.toString());
        Log.i(TAG, serverUpdates.toString());

        long nul = 0;
        for (int i = 0; i < tables.length; i++) {
            if (serverUpdates.get(i)==null){
                serverUpdates.set(i,nul);
            }
            if (serverUpdates.get(i) > localUpdates.get(i)) {
                updateLocalTable(tables[i], localUpdates.get(i));
            }
            if (serverUpdates.get(i) < localUpdates.get(i)){
                updateServerTable(tables[i], serverUpdates.get(i));
            }
        }
    }

    private void updateLocalTable(String tableName, long updateFrom) {

        Log.i(TAG, "оновлюємо локальну таблицю " + tableName);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.whereGreaterThan("update_time", updateFrom);
        try {
            List<ParseObject> parseObjects = query.find();
            for (ParseObject parseObject : parseObjects) {
                dataSource.putParseObjectToLocalTable(tableName, parseObject);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateServerTable(String tableName, long updateFrom) {

        Log.i(TAG, "оновлюємо серверну таблицю " + tableName);
        Cursor cursor = dataSource.getUpdatebleRowsFromLocal(tableName, updateFrom);
        dataSource.putLocalRowsToServerTable(tableName, cursor);
        }


}
