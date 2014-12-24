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
import com.androidcollider.koljadnik.objects.Song;
import com.androidcollider.koljadnik.objects.SongType;
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

    public void setLocalUpdates (String table, long time){
        sharedPreferences.edit().putLong(table, time).apply();
    }

    public ArrayList<Long> getLocalUpdates (){
        ArrayList<Long> localUpdates = new ArrayList<>();
        for (int i=0; i<tables.length; i++){
            localUpdates.add(sharedPreferences.getLong(tables[i],0));
        }
        return localUpdates;
    }

    public ArrayList<Long> getServerUpdates(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LastUpdate");
        ParseObject parseObject = null;
        try {
            parseObject = query.getFirst();
            ArrayList<Long> serverUpdates = new ArrayList<>();
            serverUpdates.add((Long)parseObject.getNumber("song_update"));
            serverUpdates.add((Long)parseObject.getNumber("songtype_update"));
            serverUpdates.add((Long)parseObject.getNumber("text_update"));
            serverUpdates.add((Long)parseObject.getNumber("chord_update"));
            serverUpdates.add((Long)parseObject.getNumber("note_update"));
            serverUpdates.add((Long)parseObject.getNumber("comment_update"));
            return serverUpdates;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void putParseObjectToLocalTable (String tableName, ParseObject parseObject){

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

        setLocalUpdates(tableName, (Long)parseObject.getNumber("update_time"));
        closeLocal();
    }


    public ArrayList<SongType> getSongTypes(){
        openLocal();
        Cursor cursor = dbLocal.query("SongType", null, null, null, null, null, null);
        ArrayList<SongType> songTypesList = new ArrayList<>();

        if (cursor.moveToFirst()) {
           int nameColIndex = cursor.getColumnIndex("name");
           int idTypeColIndex =  cursor.getColumnIndex("id_type");

           for (int i=0; i<cursor.getCount(); i++){
               String songTypeName =cursor.getString(nameColIndex);
               int songTypeId = cursor.getInt(idTypeColIndex);
               songTypesList.add(new SongType(songTypeId, songTypeName, getSongTypeQuantity(songTypeId)));
               cursor.moveToNext();
           }
        }
        cursor.close();
        closeLocal();
        return songTypesList;
    }

    private int getSongTypeQuantity(int songType){
        Cursor cursor = dbLocal.query("Song", null,"id_type = ?", new String[]{String.valueOf(songType)}, null, null, null);
        return cursor.getCount();
    }

    public Cursor getUpdatebleRowsFromLocal(String tableName, long updateFrom){
        openLocal();
        Cursor cursor = dbLocal.query(tableName, null, "update_time > ?", new String[]{String.valueOf(updateFrom)}, null, null, null);

        return cursor;
    }

    public void putLocalRowsToServerTable(String tableName, Cursor cursor){

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

            /*else*/ if (tableName.equals("Chord")||
                    tableName.equals("Note")||
                    tableName.equals("Comment")){
                if (cursor.moveToFirst()){

                    int updateTimeIndex = cursor.getColumnIndex("update_time");
                    int idSongIndex = cursor.getColumnIndex("id_song");
                    int dataIndex = cursor.getColumnIndex("data");

                    for (int i=0; i<cursor.getCount(); i++){
                        ParseObject parseObject = new ParseObject(tableName);
                        parseObject.put("id_song", cursor.getLong(idSongIndex));
                        parseObject.put("update_time", cursor.getLong(updateTimeIndex));
                        parseObject.put("data", cursor.getString(dataIndex));

                        try {
                            parseObject.save();
                            setServerUpdates("comment_update",cursor.getLong(updateTimeIndex));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        closeLocal();

    }

    public void setServerUpdates(String columnName, long updateTime){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LastUpdate");
        try {
            ParseObject parseObject = query.getFirst();
            parseObject.put(columnName, updateTime);
            parseObject.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void addCommentToLocal(int idSong, String data){
        openLocal();
        //Add data to table Comment
        long currentTime = System.currentTimeMillis();
        ContentValues cv = new ContentValues();
        cv.put("update_time", currentTime);
        cv.put("id_song", idSong);
        cv.put("data", data);
        dbLocal.insert("Comment", null, cv);
        cv.clear();

        setLocalUpdates("Comment", currentTime);
    }

    public ArrayList<Song> getSongMainInfo(int idType){
        openLocal();
        Cursor cursor = dbLocal.query("Song", null, "id_type = ?", new String[]{String.valueOf(idType)}, null, null, null);
        ArrayList<Song> songsList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int ratingColIndex = cursor.getColumnIndex("rating");
            int idColIndex =  cursor.getColumnIndex("id_song");

            long minRating = cursor.getLong(ratingColIndex);
            long maxRating = cursor.getLong(ratingColIndex);

            for (int i=0; i<cursor.getCount(); i++){
                String songName =cursor.getString(nameColIndex);
                int songId = cursor.getInt(idColIndex);
                long songRating = cursor.getInt(ratingColIndex);
                songsList.add(new Song(songId, songRating, songName));

                if (songRating>maxRating){
                    maxRating=songRating;
                }
                if (songRating<minRating){
                    minRating=songRating;
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

}
