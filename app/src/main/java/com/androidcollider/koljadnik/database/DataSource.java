package com.androidcollider.koljadnik.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.database.local_db.DBhelperLocalDB;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pseverin on 22.12.14.
 */
public class DataSource {

    private final static String TAG = "Андроідний Коллайдер";

    private DBhelperLocalDB dbHelperLocal;
    private SQLiteDatabase dbLocal;
    private Context context;
    private SharedPreferences sharedPreferences;
    private final static String APP_PREFERENCES = "KoljadnikPref";

    private final static String[] tables = new String[]{"Song","SongType","Text","Chord","Note","Comment"};

    public DataSource(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Parse.initialize(context, context.getString(R.string.parse_application_id),
                context.getString(R.string.parse_client_key));
        dbHelperLocal = new DBhelperLocalDB(context);
    }

    //Open database
    public void openLocal() throws SQLException {
        dbLocal = dbHelperLocal.getWritableDatabase();
    }

    //Close database
    public void closeLocal() {
        dbLocal.close();
    }

    public void checkAndUpdateTables () {

        ArrayList<Date> localUpdates = getLocalUpdates();
        ArrayList<Date> serverUpdates = getServerUpdates();

        Log.i(TAG, localUpdates.toString());
        Log.i(TAG, serverUpdates.toString());


        for (int i = 0; i < tables.length; i++) {
            if (serverUpdates.get(i).getTime() > localUpdates.get(i).getTime()) {
                updateLocalTable(tables[i], localUpdates.get(i));
            }
                    /*if (serverUpdates.get(i).getTime()<localUpdates.get(i).getTime()){
                        updateServerTable(tables[i], serverUpdates.get(i));
                    }*/
        }
    }

        public ArrayList<Date> getServerUpdates () {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LastUpdate");
            ParseObject parseObject = null;
            try {
                parseObject = query.getFirst();
                ArrayList<Date> serverUpdates = new ArrayList<Date>();
                serverUpdates.add(parseObject.getDate("song_table"));
                serverUpdates.add(parseObject.getDate("songtype_table"));
                serverUpdates.add(parseObject.getDate("text_table"));
                serverUpdates.add(parseObject.getDate("chord_table"));
                serverUpdates.add(parseObject.getDate("note_table"));
                serverUpdates.add(parseObject.getDate("comment_table"));
                return serverUpdates;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
    }

    public void setLocalUpdates (String table, long time){
        sharedPreferences.edit().putLong(table, time).apply();
    }

    private ArrayList<Date> getLocalUpdates (){
        ArrayList<Date> localUpdates = new ArrayList<>();
        for (int i=0; i<tables.length; i++){
            localUpdates.add(new Date(sharedPreferences.getLong(tables[i],0)));
        }
        return localUpdates;
    }

    private void updateLocalTable(String tableName, Date updateFrom){
        Log.i(TAG,"оновлюємо таблицю "+tableName);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.whereGreaterThan("updatedAt", updateFrom);
        try {
            List<ParseObject> parseObjects = query.find();
            for (ParseObject parseObject : parseObjects) {
                putParseObjectToLocalTable (tableName, parseObject);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void putParseObjectToLocalTable (String tableName, ParseObject parseObject){

        if (tableName.equals("Song")){
            openLocal();
            //Add data to table Song
            ContentValues cv = new ContentValues();
            cv.put("update_time", parseObject.getUpdatedAt().getTime());
            cv.put("name", parseObject.getString("name"));
            cv.put("id_type", parseObject.getInt("id_type"));
            cv.put("rating", parseObject.getInt("rating"));
            dbLocal.insert("Song", null, cv);
            cv.clear();
        }

        else if (tableName.equals("SongType")){
            openLocal();
            //Add data to table Song
            ContentValues cv = new ContentValues();
            cv.put("update_time", parseObject.getUpdatedAt().getTime());
            cv.put("name", parseObject.getString("name"));
            dbLocal.insert("SongType", null, cv);
            cv.clear();
        }

        else if (tableName.equals("Text")){
            openLocal();
            //Add data to table Song
            ContentValues cv = new ContentValues();
            cv.put("update_time", parseObject.getUpdatedAt().getTime());
            cv.put("id_song", parseObject.getInt("id_song"));
            cv.put("data", parseObject.getString("data"));
            cv.put("remarks", parseObject.getString("remarks"));
            cv.put("source", parseObject.getString("source"));
            dbLocal.insert("Text", null, cv);
            cv.clear();
        }

        else if (tableName.equals("Chord")||
                tableName.equals("Note")||
                tableName.equals("Comment")){
            openLocal();
            //Add data to table Song
            ContentValues cv = new ContentValues();
            cv.put("update_time", parseObject.getUpdatedAt().getTime());
            cv.put("id_song", parseObject.getInt("id_song"));
            cv.put("data", parseObject.getString("data"));
            dbLocal.insert(tableName, null, cv);
            cv.clear();
        }

        setLocalUpdates(tableName, parseObject.getUpdatedAt().getTime());
        closeLocal();
    }

    public String[] getSongTypes(){
        openLocal();
        Cursor cursor = dbLocal.query("SongType", null, null, null, null, null, null);
        String[] songTypesArray = new String[cursor.getCount()];

        if (cursor.moveToFirst()) {
           int nameColIndex = cursor.getColumnIndex("name");

           for (int i=0; i<cursor.getCount(); i++){
               songTypesArray[i]=cursor.getString(nameColIndex);
               cursor.moveToNext();
           }
        }
        cursor.close();
        closeLocal();
        return songTypesArray;


    }

}
