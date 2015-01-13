package com.androidcollider.koljadnik.database;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.SongTypesActivity;
import com.androidcollider.koljadnik.SplashScreenActivity;
import com.androidcollider.koljadnik.objects.SongForUpdateRating;
import com.androidcollider.koljadnik.utils.AppController;
import com.androidcollider.koljadnik.utils.NumberConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<Long> localUpdateDates, serverUpdateDates;

    private final static String[] tableNames = new String[]{"Carol", "CarolType", "CarolChord", "CarolNote", "CarolComment"};
   // private final static String[] serverTables = new String[]{"Songs", "Types", "Texts", "Chords", "Nots", "Comments"};

    private int alreadyUpdTables = 0;
    private int needToUpdTables = 0;

    private int alreadyUpdRatings = 0;
    private int needToUpdRatings = 0;


    public DBupdater(Context context, String mode) {
        this.context = context;
        /*Parse.initialize(context, context.getString(R.string.parse_application_id),
                context.getString(R.string.parse_client_key));*/
        dataSource = new DataSource(context);
        params = new HashMap<>();
        this.mode = mode;
    }


    public void checkAndUpdateTables() {
        localUpdateDates = new ArrayList<>();
        serverUpdateDates = new ArrayList<>();
        localUpdateDates.clear();
        serverUpdateDates.clear();

        localUpdateDates = dataSource.getLocalUpdates();
        updateServerRatings();
        //getServerUpdateDates();

    }

    public void updateServerRatings() {
        if (mode.equals("start")) {
            ((SplashScreenActivity) context).setLoadingStatus("Завантаження рейтингів");
        }
        ArrayList<SongForUpdateRating> songListForUpdateRatings = dataSource.getSongsForUpdateRating();
        needToUpdRatings = songListForUpdateRatings.size();
        if (needToUpdRatings==0){
            getServerUpdateDates();
        } else {
            for (SongForUpdateRating songForUpdateRating: songListForUpdateRatings){
                updateServerRatingsReq(songForUpdateRating.getId(),songForUpdateRating.getRatingIncrement());
            }
        }
    }

    private void updateServerRatingsReq(final int idSong, final long ratingIncrement){
        String url = AppController.BASE_URL_KEY + "update_song_rating.php";

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE ratings", "     " + response);
                        alreadyUpdRatings++;
                        if (alreadyUpdRatings == needToUpdRatings) {
                            alreadyUpdRatings = 0;
                            needToUpdRatings = 0;

                            getServerUpdateDates();
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
                params.put("action", "update_song_rating");
                params.put("id", String.valueOf(idSong));
                params.put("rating", String.valueOf(ratingIncrement));
                return params;
            }
        };
        // Adding request to request queue
        /*strReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        AppController.getInstance().addToRequestQueue(strReq, "update_ratings");
    }




    public void getServerUpdateDates() {
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

                        serverUpdateDates.add(NumberConverter.dateToLongConverter(
                                result.getString("Carol_up")
                        ));
                        serverUpdateDates.add(NumberConverter.dateToLongConverter(
                                result.getString("CarolType_up")
                        ));
                        serverUpdateDates.add(NumberConverter.dateToLongConverter(
                                result.getString("CarolChord_up")
                        ));
                        serverUpdateDates.add(NumberConverter.dateToLongConverter(
                                result.getString("CarolNote_up")
                        ));
                        serverUpdateDates.add(NumberConverter.dateToLongConverter(
                                result.getString("CarolComment_up")
                        ));

                        Log.i(TAG + " local updates", localUpdateDates.toString());
                        Log.i(TAG + " server updates", serverUpdateDates.toString());

                        //calculating needToUpdTables Tables

                        for (int i = 0; i < tableNames.length; i++) {
                            if (serverUpdateDates.get(i) > localUpdateDates.get(i)) {
                                needToUpdTables++;
                            }
                        }
                        if (needToUpdTables == 0) {
                            setProgramChange();
                        } else {
                            if (mode.equals("start")) {
                                ((SplashScreenActivity) context).setLoadingStatus("Оновлення бази пісень");
                            }
                            /*for (int i = 0; i < tableNames.length; i++) {
                                if (serverUpdateDates.get(i) > localUpdateDates.get(i)) {
                                    getNewDataFromServer(i, localUpdateDates.get(i));
                                }
                            }*/
                            getNewDataFromServer(localUpdateDates);

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

    private void getNewDataFromServer(final ArrayList<Long> localUpdateDates) {

        //Log.i(TAG, "оновлюємо локальну таблицю " + tableNames[tableIndex]);
        String url = AppController.BASE_URL_KEY + "get_updates_from_table_for_date.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("RESPONSE number", "     " + AppController.getInstance().getRequestQueue().getSequenceNumber() + " ");
                Log.d("RESPONSE tables", "     " + response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString("status").equals("True")){

                        JSONArray resultCarol = res.getJSONObject("Carol").getJSONArray("results");
                        JSONArray resultType = res.getJSONObject("CarolType").getJSONArray("results");

                        for (int i = 0; i < resultType.length(); i++) {
                            dataSource.putJsonObjectToLocalTable("CarolType", resultCarol.getJSONObject(i));
                        }

                        for (int i = 0; i < resultCarol.length(); i++) {
                            dataSource.putJsonObjectToLocalTable("Carol", resultCarol.getJSONObject(i));
                        }
                        setProgramChange();

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
                params.put("action", "get_updates_from_table_for_date");

                params.put("carol_date", NumberConverter.longToDateConverter(localUpdateDates.get(0)));
                params.put("carol_chord_date", NumberConverter.longToDateConverter(localUpdateDates.get(2)));
                params.put("carol_comment_date", NumberConverter.longToDateConverter(localUpdateDates.get(4)));
                params.put("carol_note_date", NumberConverter.longToDateConverter(localUpdateDates.get(3)));
                params.put("carol_type_date", NumberConverter.longToDateConverter(localUpdateDates.get(1)));
                return params;
            }
        };
        // Adding request to request queue
        /*strReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        AppController.getInstance().addToRequestQueue(strReq, "data update");
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





}
