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
    private String mode = "";

    private HashMap<String, String> params;
    private DataSource dataSource;
    private Context context;
    private ArrayList<Long> localUpdates, serverUpdates;

    private final static String[] tableNames = new String[]{"CarolSong", "CarolType", "CarolText", "CarolChord", "CarolNote", "CarolComment"};
   // private final static String[] serverTables = new String[]{"Songs", "Types", "Texts", "Chords", "Nots", "Comments"};

    private int isUpdatedCount = 0;
    private int needToUpdate = 0;


    public DBupdater(Context context, String mode) {
        this.context = context;
        /*Parse.initialize(context, context.getString(R.string.parse_application_id),
                context.getString(R.string.parse_client_key));*/
        dataSource = new DataSource(context);
        params = new HashMap<>();
        this.mode = mode;
    }


    public void checkAndUpdateTables() {
        localUpdates = new ArrayList<>();
        serverUpdates = new ArrayList<>();
        localUpdates.clear();
        serverUpdates.clear();


        localUpdates = dataSource.getLocalUpdates();
        updateServerRatings();
        //getServerUpdates();

    }

    private void updateLocalTable(final int tableIndex, final long updateFrom) {

        Log.i(TAG, "оновлюємо локальну таблицю " + tableNames[tableIndex]);
        String url = AppController.BASE_URL_KEY + "get_updates_from_table.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE number", "     " + AppController.getInstance().getRequestQueue().getSequenceNumber() + " ");
                Log.d("RESPONSE tables", "     " + response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString("status").equals("True")){
                        JSONArray result = res.getJSONArray("results");

                        for (int i = 0; i < result.length(); i++) {
                            dataSource.putJsonObjectToLocalTable(tableNames[tableIndex], result.getJSONObject(i));
                        }
                        isUpdatedCount++;

                        if (needToUpdate == isUpdatedCount) {
                            needToUpdate = 0;
                            isUpdatedCount = 0;
                            if (mode.equals("finish")) {
                                ((SongTypesActivity) context).finish();
                            }
                            if (mode.equals("start")) {
                                Intent intent = new Intent();
                                intent.setAction("updating_is_over");
                                context.sendBroadcast(intent);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG + " error", volleyError.toString());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("action", "get_updates_from_table");
                params.put("table_name", tableNames[tableIndex]);
                params.put("date", NumberConverter.longToDateConverter(updateFrom));
                return params;
            }
        };
        // Adding request to request queue
        /*strReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        AppController.getInstance().addToRequestQueue(strReq, tableNames[tableIndex]+" update");
    }


    public void getServerUpdates() {
        String url = AppController.BASE_URL_KEY + "get_last_updates.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString("status").equals("True")) {
                        JSONObject result = res.getJSONObject("results");

                        serverUpdates.add(NumberConverter.dateToLongConverter(
                                result.getString("CarolSong_up")
                        ));
                        serverUpdates.add(NumberConverter.dateToLongConverter(
                                result.getString("CarolType_up")
                        ));
                        serverUpdates.add(NumberConverter.dateToLongConverter(
                                result.getString("CarolText_up")
                        ));
                        serverUpdates.add(NumberConverter.dateToLongConverter(
                                result.getString("CarolChord_up")
                        ));
                        serverUpdates.add(NumberConverter.dateToLongConverter(
                                result.getString("CarolNote_up")
                        ));
                        serverUpdates.add(NumberConverter.dateToLongConverter(
                                result.getString("CarolComment_up")
                        ));

                        Log.i(TAG + " local updates", localUpdates.toString());
                        Log.i(TAG + " server updates", serverUpdates.toString());

                        //calculating needToUpdate Tables
                        for (int i = 0; i < tableNames.length; i++) {
                            if (serverUpdates.get(i) > localUpdates.get(i)) {
                                needToUpdate++;
                            }
                        }

                        if (needToUpdate == 0) {
                            setProgramChange();
                        } else {
                            if (mode.equals("start")) {
                                Intent intent = new Intent(DBupdater.this.context, SongTypesActivity.class);
                                ((SplashScreenActivity) DBupdater.this.context).finish();
                                DBupdater.this.context.startActivity(intent);
                                ((SplashScreenActivity) DBupdater.this.context).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            }
                            for (int i = 0; i < tableNames.length; i++) {
                                if (serverUpdates.get(i) > localUpdates.get(i)) {
                                    updateLocalTable(i, localUpdates.get(i));
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG + " error", volleyError.toString());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("action", "get_last_updates");
                return params;
            }
        };
        // Adding request to request queue
        /*strReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        AppController.getInstance().addToRequestQueue(strReq, "last_updates");

    }

    private void setProgramChange() {
        if (mode.equals("start")) {
            Intent intent = new Intent(DBupdater.this.context, SongTypesActivity.class);
            ((SplashScreenActivity) DBupdater.this.context).finish();
            DBupdater.this.context.startActivity(intent);
            ((SplashScreenActivity) DBupdater.this.context).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else if (mode.equals("finish")) {
            ((SongTypesActivity) context).finish();
        }
    }

    private void stringRequest(Response.Listener<String> responseListener, final HashMap<String, String> currentParams) {
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
                return currentParams;
            }
        };
        // Adding request to request queue
        /*strReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public void updateServerRatings() {
        dataSource.updateServerRatings(this);
        if (mode.equals("start")) {
            ((SplashScreenActivity) context).setLoadingStatus("Завантаження рейтингів");
        }
    }


}
