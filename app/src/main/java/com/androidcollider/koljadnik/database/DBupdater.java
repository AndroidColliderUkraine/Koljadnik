package com.androidcollider.koljadnik.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.SongTypesActivity;
import com.androidcollider.koljadnik.SplashScreenActivity;
import com.androidcollider.koljadnik.database.local_db.DBhelperLocalDB;
import com.androidcollider.koljadnik.utils.AppController;
import com.androidcollider.koljadnik.utils.NumberConverter;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pseverin on 23.12.14.
 */
public class DBupdater {

    private final static int MY_SOCKET_TIMEOUT_MS = 30000;
    private final static String TAG = "Андроідний Коллайдер";
    private static boolean appIsStarted = false;

    private HashMap<String,String> params;
    private DataSource dataSource;
    private Context context;
    private ArrayList<Long> localUpdates, serverUpdates;

    private final static String[] tables = new String[]{"Song", "SongType", "Text", "Chord", "Note", "Comment"};

    public DBupdater(Context context) {
        this.context = context;
        /*Parse.initialize(context, context.getString(R.string.parse_application_id),
                context.getString(R.string.parse_client_key));*/
        dataSource = new DataSource(context);
        params = new HashMap<>();
    }


    public void checkAndUpdateTables() {
        localUpdates = new ArrayList<>();
        serverUpdates = new ArrayList<>();
        localUpdates.clear();
        serverUpdates.clear();

        localUpdates = dataSource.getLocalUpdates();
        getServerUpdates();

    }

    private void updateLocalTable(final String tableName, final long updateFrom) {

        String serverTableName = null;
        if (tableName.equals("Song")){
            serverTableName = "Songs";
        }
        if (tableName.equals("SongType")){
            serverTableName = "Types";
        }
        if (tableName.equals("Text")){
            serverTableName = "Texts";
        }
        if (tableName.equals("Chord")){
            serverTableName = "Chords";
        }
        if (tableName.equals("Note")){
            serverTableName = "Nots";
        }
        if (tableName.equals("Comment")){
            serverTableName = "Comments";
        }
        final String sTableName = serverTableName;

        Log.i(TAG, "оновлюємо локальну таблицю " + tableName);

        params.clear();
        params.put("action", "get_updates_from_table");
        params.put("param1", sTableName);
        params.put("param2", NumberConverter.longToDateConverter(updateFrom));

        stringRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", response);
                try {
                    JSONObject res = new JSONObject(response);
                    JSONArray result = res.getJSONArray("result");

                    for (int i=0; i<result.length();i++){
                        dataSource.putJsonObjectToLocalTable(tableName, result.getJSONArray(i));
                    }
                   /* if (!appIsStarted){
                        appIsStarted = true;
                        startProgram();
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                    /*if (!appIsStarted){
                        appIsStarted = true;
                        startProgram();
                    }*/
                }
            }

        });


       /* ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.whereGreaterThan("update_time", updateFrom);
        try {
            List<ParseObject> parseObjects = query.find();
            for (ParseObject parseObject : parseObjects) {
                dataSource.putParseObjectToLocalTable(tableName, parseObject);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

   /* private void updateServerTable(String tableName, long updateFrom) {

        Log.i(TAG, "оновлюємо серверну таблицю " + tableName);
        Cursor cursor = dataSource.getUpdatebleRowsFromLocal(tableName, updateFrom);
        dataSource.putLocalRowsToServerTable(tableName, cursor);
        }*/

    private void getServerUpdates(){
        params.clear();
        params.put("action", "get_last_updates");
        stringRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", response);
                try {
                    JSONObject res = new JSONObject(response);
                    JSONObject result = res.getJSONObject("result");

                    serverUpdates.add(NumberConverter.dateToLongConverter(
                            result.getString("song_up")
                    ));
                    serverUpdates.add(NumberConverter.dateToLongConverter(
                            result.getString("type_up")
                    ));
                    serverUpdates.add(NumberConverter.dateToLongConverter(
                            result.getString("text_up")
                    ));
                    serverUpdates.add(NumberConverter.dateToLongConverter(
                            result.getString("chord_up")
                    ));
                    serverUpdates.add(NumberConverter.dateToLongConverter(
                            result.getString("nota_up")
                    ));
                    serverUpdates.add(NumberConverter.dateToLongConverter(
                            result.getString("comment_up")
                    ));

                    Log.i(TAG +" local updates", localUpdates.toString());
                    Log.i(TAG +" server updates", serverUpdates.toString());

                    for (int i = 0; i < tables.length; i++) {

                        if (serverUpdates.get(i) > localUpdates.get(i)) {
                            updateLocalTable(tables[i], localUpdates.get(i));
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void startProgram(){
        Intent intent = new Intent(DBupdater.this.context, SongTypesActivity.class);
        ((SplashScreenActivity)DBupdater.this.context).finish();
        DBupdater.this.context.startActivity(intent);
        ((SplashScreenActivity)DBupdater.this.context).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void stringRequest(Response.Listener<String> responseListener) {
        String tag_string_req = "string_req";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppController.BASE_URL_KEY, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG + " error", volleyError.toString());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        // Adding request to request queue
        /*strReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
