package com.androidcollider.koljadnik.root;

import android.app.Application;
import android.os.Environment;
import android.support.v4.BuildConfig;
import android.util.Log;

import com.androidcollider.koljadnik.contants.Settings;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import io.fabric.sdk.android.Fabric;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Description of ${CLASS_NAME}
 *
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 * @package com.androidcollider.koljadnik
 */
public class App extends Application {

    private Tracker tracker;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        appComponent = DaggerAppComponent.builder().applicationModule(new ApplicationModule(this)).build();
        //Fabric.with(this, new Crashlytics(), new Answers());

       /* try {
            readTxt();
        } catch (IOException|JSONException e) {
            e.printStackTrace();
        }*/
        if (!BuildConfig.DEBUG) {
            getDefaultTracker();
        }
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.INFO);
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().build());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            tracker = analytics.newTracker(Settings.GOOGLE_ANALYTICS_TRACKING_ID);

            // Provide unhandled exceptions reports. Do that first after creating the tracker
            tracker.enableExceptionReporting(true);

            // Enable Remarketing, Demographics & Interests reports
            // https://developers.google.com/analytics/devguides/collection/android/display-features
            tracker.enableAdvertisingIdCollection(true);

            // Enable automatic activity tracking for your app
            tracker.enableAutoActivityTracking(true);
        }
        return tracker;
    }

    private void readTxt() throws IOException, JSONException {
        JSONObject jsonData = new JSONObject();

        Integer i = 0;

        i = parseMain("koljadki.txt", jsonData, i, 0);
        i = parseMain("schedrivki.txt", jsonData, i, 1);
        i = parseMain("zasivannia.txt", jsonData, i, 2);
        i = parseVinsh(jsonData, i, 3);
        i = parseMain("suchasni.txt", jsonData, i, 4);
        i = parseSms(jsonData, i, 5);
        i = parseInshomovni(jsonData, i, 6);


        JSONObject jsonDatatypes = new JSONObject();
            jsonDatatypes.put(String.valueOf(0), createSonTypesJson(0, "Колядки"));
            jsonDatatypes.put(String.valueOf(1), createSonTypesJson(1, "Щедрівки"));
            jsonDatatypes.put(String.valueOf(2), createSonTypesJson(2, "Засівання"));
            jsonDatatypes.put(String.valueOf(3), createSonTypesJson(3, "Віншування"));
            jsonDatatypes.put(String.valueOf(4), createSonTypesJson(4, "Сучасні"));
            jsonDatatypes.put(String.valueOf(5), createSonTypesJson(5, "СМС"));
            jsonDatatypes.put(String.valueOf(6), createSonTypesJson(6, "Іншомовні"));

        JSONObject finalJson = new JSONObject();

            finalJson.put("songs", jsonData);
            finalJson.put("songTypes", jsonDatatypes);


        for (int k = 0; k < jsonData.length(); k++) {
            try {
                Log.i("KOLJ", k + " -  " + jsonData.getJSONObject(String.valueOf(k)).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int k = 0; k < jsonData.length(); k++) {
            try {
                Log.i("KOLJ", k + " -  " + jsonData.getJSONObject(String.valueOf(k)).getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int k = 0; k < jsonData.length(); k++) {
            try {
                Log.i("KOLJ", k + " -  " + jsonData.getJSONObject(String.valueOf(k)).getString("source"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/MyFiles");
        directory.mkdirs();

        File file = new File(directory, "koljandnik_data6.txt");
        FileOutputStream fOut = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fOut);
        osw.write(finalJson.toString());
        osw.flush();
        osw.close();

    }

    public int parseVinsh(JSONObject jsonData, Integer i, int typeId) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("vinsh.txt")));
        // do reading, usually loop until end of file reading
        StringBuilder text = new StringBuilder();
        String title = "";
        while (true) {
            String mLine = reader.readLine();
            if (mLine != null) {
                if (mLine.contains("****")) {
                    if (text.length() > 0) {
                        try {
                            jsonData.put(String.valueOf(i), createSongJson(i, typeId, title, text.toString(), ""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        i++;
                        text = new StringBuilder();
                    }
                } else {
                    if (text.length() == 0) {
                        title = mLine;

                        StringBuilder b = new StringBuilder(title);
                        if (b.substring(title.length() - 1).equals(":") ||
                                b.substring(title.length() - 1).equals(";") ||
                                b.substring(title.length() - 1).equals(",") ||
                                b.substring(title.length() - 1).equals(".")) {
                            b.deleteCharAt(title.length() - 1);
                            title = b.toString();
                        }
                    }
                    text.append(mLine + "\n");
                }
            } else {
                break;
            }
        }
        reader.close();
        return i;
    }

    public int parseSms(JSONObject jsonData, Integer i, int typeId) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("sms.txt")));
        // do reading, usually loop until end of file reading
        StringBuilder text = new StringBuilder();
        String title = "";
        while (true) {
            String mLine = reader.readLine();
            if (mLine != null) {
                if (mLine.contains("-----")) {
                    if (text.length() > 0) {
                        try {
                            jsonData.put(String.valueOf(i), createSongJson(i, typeId, title, text.toString(), ""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        i++;
                        text = new StringBuilder();
                    }
                } else if (!mLine.isEmpty()) {
                    if (text.length() == 0) {
                        title = mLine;

                        StringBuilder b = new StringBuilder(title);
                        if (b.substring(title.length() - 1).equals(":") ||
                                b.substring(title.length() - 1).equals(";") ||
                                b.substring(title.length() - 1).equals(",") ||
                                b.substring(title.length() - 1).equals(".")) {
                            b.deleteCharAt(title.length() - 1);
                            title = b.toString();
                        }
                    }
                    text.append(mLine + "\n");
                }
            } else {
                break;
            }
        }
        reader.close();
        return i;
    }

    public int parseMain(String filename, JSONObject jsonData, Integer i, int typeId) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
        // do reading, usually loop until end of file reading
        StringBuilder text = new StringBuilder();
        String title = "";
        String source = "";
        while (true) {
            String mLine = reader.readLine();
            if (mLine != null) {
                if (mLine.contains("====")) {
                    if (text.length() > 0) {
                        try {
                            jsonData.put(String.valueOf(i), createSongJson(i, typeId, title, text.toString(), source));
                            source = "";
                            title = "";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        i++;
                        text = new StringBuilder();
                    }
                } else if (title.isEmpty()) {
                    title = mLine;
                    title = title.replace("SONG NAME: ", "");
                } else if (mLine.contains("-----")) {
                    continue;
                } else if (mLine.contains("****")) {
                    mLine = reader.readLine();
                    source = mLine;
                } else {
                    text.append(mLine + "\n");
                }
            } else {
                break;
            }
        }
        reader.close();
        return i;
    }

    public int parseInshomovni(JSONObject jsonData, Integer i, int typeId) throws IOException, JSONException {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("inshomovni.txt")));
            while (true) {
                String mLine = reader.readLine();
                if (mLine != null) {
                    text.append(mLine);
                } else {
                    break;
                }
            }
            reader.close();
        } catch (IOException e){

        }
        JSONObject jsonObject = new JSONObject(text.toString());
        JSONArray carols = jsonObject.getJSONArray("carols");

        for (int k = 0; k < carols.length(); k++){
            JSONObject carol = carols.getJSONObject(k);
            String title = carol.getString("text");
            String name = carol.getString("name");
            name = name.replace("\r", "").replace("\n","").trim();
            jsonData.put(String.valueOf(i), createSongJson(i, typeId, name, title, ""));
            i++;
        }
        return i;
    }


    private JSONObject createSonTypesJson(int id, String title) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("name", title);
            jsonObject.put("updatedAt", 0);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createSongJson(int id, int idType, String title, String text, String source) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("name", title);
            jsonObject.put("rating", 0);
            jsonObject.put("idType", idType);
            jsonObject.put("text", text);
            jsonObject.put("remarks", "");
            jsonObject.put("source", source);
            jsonObject.put("comments", null);
            jsonObject.put("updatedAt", 0);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
