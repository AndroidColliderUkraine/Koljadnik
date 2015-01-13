package com.androidcollider.koljadnik.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Sampler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.SongTypesActivity;
import com.androidcollider.koljadnik.database.local_db.DBhelperLocalDB;
import com.androidcollider.koljadnik.objects.Song;
import com.androidcollider.koljadnik.objects.SongForUpdateRating;
import com.androidcollider.koljadnik.objects.SongType;
import com.androidcollider.koljadnik.utils.AppController;
import com.androidcollider.koljadnik.utils.NumberConverter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private DBupdater dBupdater;
    private DBhelperLocalDB dbHelperLocal;
    private SQLiteDatabase dbLocal;
    private Context context;
    private SharedPreferences sharedPreferences;
    private final static String APP_PREFERENCES = "KoljadnikPref";

    private final static String[] tables = new String[]{"Carol", "CarolType", "CarolChord", "CarolNote", "CarolComment"};

    private int isUpdatedCount = 0;
    private int needToUpdate = 0;

    public DataSource(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
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

    public void setLocalUpdates(String table, long time) {
        sharedPreferences.edit().putLong(table, time).apply();
    }

    public ArrayList<Long> getLocalUpdates() {
        ArrayList<Long> localUpdates = new ArrayList<>();
        for (int i = 0; i < tables.length; i++) {
            localUpdates.add(sharedPreferences.getLong(tables[i], 0));
        }
        return localUpdates;
    }


    public void putJsonObjectToLocalTable(String tableName, JSONObject jsonObject) {

        long updateTime = 0;
        if (tableName.equals("Carol")) {
            openLocal();
            try {
                int idSongServer = jsonObject.getInt("id");
                updateTime = NumberConverter.dateToLongConverter(jsonObject.getString("Date"));
                int showTo = jsonObject.getInt("ToShow");;
                if (showTo==1){
                    //Add data to table Song
                    ContentValues cv = new ContentValues();
                    cv.put("update_time", updateTime);
                    cv.put("name", jsonObject.getString("Name"));
                    cv.put("id_type", jsonObject.getInt("Type"));
                    cv.put("text", jsonObject.getString("Data"));
                    cv.put("source", jsonObject.getInt("Source"));
                    cv.put("rating", jsonObject.getLong("Rating"));
                    cv.put("my_local_rating", 0);
                    int updateCount = dbLocal.update(tableName, cv, "id_song = ?", new String[]{String.valueOf(idSongServer)});
                    if (updateCount == 0) {
                        cv.put("id_song", idSongServer);
                        long insertCount = dbLocal.insert("Carol", null, cv);
                    }
                    cv.clear();
                } else {
                    int delCount = dbLocal.delete(tableName, "id_song = ?", new String[]{String.valueOf(idSongServer)});
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (tableName.equals("CarolType")) {
            openLocal();
            //Add data to table Song
            try {
                int idTypeServer = jsonObject.getInt("id");
                updateTime = NumberConverter.dateToLongConverter(jsonObject.getString("Date"));
                int showTo = jsonObject.getInt("ShowTo");;
                if (showTo==1){
                    ContentValues cv = new ContentValues();
                    cv.put("update_time", updateTime);
                    cv.put("name", jsonObject.getString("Type"));
                    int updateCount = dbLocal.update(tableName, cv, "id_type = ?", new String[]{String.valueOf(idTypeServer)});
                    if (updateCount == 0) {
                        cv.put("id_type", idTypeServer);
                        long insertCount = dbLocal.insert(tableName, null, cv);
                    }
                    cv.clear();
                } else {
                    int delCount = dbLocal.delete(tableName, "id_type = ?", new String[]{String.valueOf(idTypeServer)});
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } /*else if (tableName.equals("CarolText")) {
            openLocal();
            //Add data to table Song

            try {
                int idTextServer = jsonObject.getInt("id");

                updateTime = NumberConverter.dateToLongConverter(jsonObject.getString("Date"));
                int showTo = jsonObject.getInt("ShowTo");;
                if (showTo==1){
                    ContentValues cv = new ContentValues();
                    cv.put("update_time", updateTime);
                    cv.put("id_song", jsonObject.getInt("id_Song"));
                    cv.put("data", jsonObject.getString("Data"));
                    cv.put("remarks", "");
                    cv.put("source", jsonObject.getString("Source"));
                    int updateCount = dbLocal.update(tableName, cv, "id_text = ?", new String[]{String.valueOf(idTextServer)});
                    if (updateCount == 0) {
                        cv.put("id_text", idTextServer);
                        dbLocal.insert(tableName, null, cv);
                    }
                    cv.clear();
                } else {
                    int delCount = dbLocal.delete(tableName, "id_text = ?", new String[]{String.valueOf(idTextServer)});
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } *//*else if (tableName.equals("Chord") ||
                tableName.equals("Note") ||
                tableName.equals("Comment")) {
            openLocal();
            //Add data to table Song
            ContentValues cv = new ContentValues();
            cv.put("update_time", parseObject.getUpdatedAt().getTime());
            cv.put("id_song", parseObject.getInt("id_song"));
            cv.put("data", parseObject.getString("data"));
            dbLocal.insert(tableName, null, cv);
            cv.clear();
        }*/
        setLocalUpdates(tableName, updateTime);
        closeLocal();
    }


    public ArrayList<SongType> getSongTypes() {

        openLocal();
        Cursor cursor = dbLocal.query("CarolType", null, null, null, null, null, null);
        ArrayList<SongType> songTypesList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int idTypeColIndex = cursor.getColumnIndex("id_type");

            for (int i = 0; i < cursor.getCount(); i++) {
                String songTypeName = cursor.getString(nameColIndex);
                int songTypeId = cursor.getInt(idTypeColIndex);
                //Log.i(TAG + " формуємо список типів", "тип-"+songTypeName+ "    id-"+songTypeId);
                songTypesList.add(new SongType(songTypeId, songTypeName, getSongTypeQuantity(songTypeId)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        closeLocal();
        return songTypesList;
    }

    private int getSongTypeQuantity(int songType) {
        Cursor cursor = dbLocal.query("Carol", null, "id_type = ?", new String[]{String.valueOf(songType)}, null, null, null);
        //Log.i(TAG + " кількість", cursor.getCount()+"");
        return cursor.getCount();
    }

    public ArrayList<Song> getSongMainInfo(int idType) {
        openLocal();
        Cursor cursor = dbLocal.query("Carol", null, "id_type = ?", new String[]{String.valueOf(idType)}, null, null, null);
        ArrayList<Song> songsList = new ArrayList<>();
        Log.i(TAG, " кількість типу id=" + idType + "     " + cursor.getCount());
        if (cursor.moveToFirst()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int ratingColIndex = cursor.getColumnIndex("rating");
            int localRatingColIndex = cursor.getColumnIndex("my_local_rating");
            int idColIndex = cursor.getColumnIndex("id_song");

            long minRating = cursor.getLong(ratingColIndex);
            long maxRating = cursor.getLong(ratingColIndex);

            for (int i = 0; i < cursor.getCount(); i++) {
                String songName = cursor.getString(nameColIndex);
                int songId = cursor.getInt(idColIndex);
                long songRating = cursor.getLong(ratingColIndex) + cursor.getLong(localRatingColIndex);
                songsList.add(new Song(songId, songName, songRating, idType, null, null, null, null));

                if (songRating > maxRating) {
                    maxRating = songRating;
                }
                if (songRating < minRating) {
                    minRating = songRating;
                }
                cursor.moveToNext();
            }
            Song.current_max_rating = maxRating;
            Song.current_min_rating = minRating;
        }
        cursor.close();
        closeLocal();
        return songsList;
    }

    public void addPointToLocalRating(int idSong) {
        openLocal();
        Cursor cursor = dbLocal.query("Carol", null, "id_song = ?", new String[]{String.valueOf(idSong)}, null, null, null);
        long myLocalRating = 0;
        if (cursor.moveToFirst()) {
            int localRatingColIndex = cursor.getColumnIndex("my_local_rating");
            myLocalRating = cursor.getLong(localRatingColIndex);
        }

        ContentValues cv = new ContentValues();
        cv.put("my_local_rating", myLocalRating + 1);
        dbLocal.update("Carol", cv, "id_song = ?", new String[]{String.valueOf(idSong)});
        closeLocal();
    }


    public ArrayList<SongForUpdateRating> getSongsForUpdateRating() {
        openLocal();
        Cursor cursor = dbLocal.query("Carol", null, "my_local_rating > 0", null, null, null, null);
        ArrayList<SongForUpdateRating> songsForUpdateRating = new ArrayList<>();
       /* needToUpdate = cursor.getCount();
        if (needToUpdate==0){
            isUpdatedCount = 0;
            needToUpdate = 0;
            dBupdater.getServerUpdateDates();
        }else {*/
        if (cursor.moveToFirst()) {
            int localRatingColIndex = cursor.getColumnIndex("my_local_rating");
            int idColIndex = cursor.getColumnIndex("id_song");
            for (int i = 0; i < cursor.getCount(); i++) {
                long myLocalRating = cursor.getLong(localRatingColIndex);
                int songId = cursor.getInt(idColIndex);
                songsForUpdateRating.add(new SongForUpdateRating(songId, myLocalRating));
                cursor.moveToNext();
            }
            ContentValues cv = new ContentValues();
            cv.put("my_local_rating", 0);
            dbLocal.update("Carol", cv, null, null);
        }
        cursor.close();
        closeLocal();

        return songsForUpdateRating;
    }


    public Song getSongAdvancedInfo(Song song) {
        openLocal();
        Cursor cursor = dbLocal.query("Carol", null, "id_song = ?", new String[]{String.valueOf(song.getId())}, null, null, null);
        //ArrayList<Song> songsList = new ArrayList<>();
        //Log.i(TAG, " кількість типу id=" + idType + "     " + cursor.getCount());
        if (cursor.moveToFirst()) {
            int dataColIndex = cursor.getColumnIndex("data");
            //int remarksColIndex = cursor.getColumnIndex("remarks");
            int sourceColIndex = cursor.getColumnIndex("source");
            int idSongColumnIndex = cursor.getColumnIndex("id_song");

            int idSong = cursor.getInt(idSongColumnIndex);

            String text = cursor.getString(dataColIndex);
            //String remarks = cursor.getString(remarksColIndex);
            String source = cursor.getString(sourceColIndex);
            song.setText(text);
            //song.setRemarks(remarks);
            song.setSource(source);
        }
        cursor.close();
        /*cursor = dbLocal.query("CarolComment", null, "id_text = ?", new String[]{String.valueOf(song.getId())}, null, null, null);

        ArrayList<String> commentList = new ArrayList<>();
        //Log.i(TAG, " кількість типу id=" + idType + "     " + cursor.getCount());
        if (cursor.moveToFirst()) {
            int commentDataColIndex = cursor.getColumnIndex("data");

            for (int i = 0; i < cursor.getCount(); i++) {
                commentList.add(cursor.getString(commentDataColIndex));
            }
            song.setComments(commentList);
        }
        cursor.close();
        closeLocal();*/
        return song;
    }

    public boolean isTextContainsChars(int songId, String text) {
        openLocal();
        Cursor cursor = dbLocal.query("Carol", null, "id_song = ?", new String[]{String.valueOf(songId)}, null, null, null);
        //ArrayList<Song> songsList = new ArrayList<>();
        //Log.i(TAG, " кількість типу id=" + idType + "     " + cursor.getCount());
        if (cursor.moveToFirst()) {
            int dataColIndex = cursor.getColumnIndex("data");
            String data = cursor.getString(dataColIndex);
            data = data.toLowerCase();
            if (data.contains(text)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void savePref(String sPrefType, boolean wasStarted) {
        if (sPrefType.equals("wasStarted")) {
            sharedPreferences.edit().
                    putBoolean(sPrefType, wasStarted)
                    .apply();
        }

    }

    public boolean loadStartPref() {
        return sharedPreferences.getBoolean("wasStarted", false);
    }


    public Cursor getUpdatebleRowsFromLocal(String tableName, long updateFrom) {
        openLocal();
        Cursor cursor = dbLocal.query(tableName, null, "update_time > ?", new String[]{String.valueOf(updateFrom)}, null, null, null);

        return cursor;
    }

    public void putLocalRowsToServerTable(String tableName, Cursor cursor) {

            /*if (tableName.equals("Song")){

                if (cursor.moveToFirst()){
                    int updateTimeIndex = cursor.getColumnIndex("update_time");
                    int nameIndex = cursor.getColumnIndex("name");
                    int idTypeIndex = cursor.getColumnIndex("id_type");
                    int ratingIndex = cursor.getColumnIndex("rating");

                    for (int i=0; i<cursor.getCount(); i++){
                        ParseObject parseObject = new ParseObject(tableName);
                        parseObject.put("update_time", cursor.getLong(updateTimeIndex));
                        parseObject.put("name", cursor.getString(nameIndex));
                        parseObject.put("id_type", cursor.getInt(idTypeIndex));
                        parseObject.put("rating", cursor.getInt(ratingIndex));
                        try {
                            parseObject.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }*/

            /*else if (tableName.equals("SongType")){
                if (cursor.moveToFirst()){
                    int idTypeIndex = cursor.getColumnIndex("id_type");
                    int updateTimeIndex = cursor.getColumnIndex("update_time");
                    int nameIndex = cursor.getColumnIndex("name");

                    for (int i=0; i<cursor.getCount(); i++){
                        ParseObject parseObject = new ParseObject(tableName);
                        parseObject.put("update_time", cursor.getLong(updateTimeIndex));
                        parseObject.put("name", cursor.getString(nameIndex));
                        parseObject.put("id_type", cursor.getInt(idTypeIndex));
                        try {
                            parseObject.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }*/

            /*else if (tableName.equals("Text")){
                if (cursor.moveToFirst()){

                    int updateTimeIndex = cursor.getColumnIndex("update_time");
                    int idSongIndex = cursor.getColumnIndex("id_song");
                    int dataIndex = cursor.getColumnIndex("data");
                    int remarksIndex = cursor.getColumnIndex("remarks");
                    int sourceIndex = cursor.getColumnIndex("source");

                    for (int i=0; i<cursor.getCount(); i++){
                        ParseObject parseObject = new ParseObject(tableName);
                        parseObject.put("id_song", cursor.getLong(idSongIndex));
                        parseObject.put("update_time", cursor.getLong(updateTimeIndex));
                        parseObject.put("data", cursor.getString(dataIndex));
                        parseObject.put("remarks", cursor.getInt(remarksIndex));
                        parseObject.put("source", cursor.getInt(sourceIndex));
                        try {
                            parseObject.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }*/

            /*else*/
        if (tableName.equals("Chord") ||
                tableName.equals("Note") ||
                tableName.equals("Comment")) {
            if (cursor.moveToFirst()) {

                int updateTimeIndex = cursor.getColumnIndex("update_time");
                int idSongIndex = cursor.getColumnIndex("id_song");
                int dataIndex = cursor.getColumnIndex("data");

                for (int i = 0; i < cursor.getCount(); i++) {
                    ParseObject parseObject = new ParseObject(tableName);
                    parseObject.put("id_song", cursor.getLong(idSongIndex));
                    parseObject.put("update_time", cursor.getLong(updateTimeIndex));
                    parseObject.put("data", cursor.getString(dataIndex));

                    try {
                        parseObject.save();
                        //setServerUpdates("comment_update", cursor.getLong(updateTimeIndex));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        closeLocal();

    }

}
