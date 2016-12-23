package com.androidcollider.koljadnik.root;

import android.app.Application;
import android.os.Environment;

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

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().applicationModule(new ApplicationModule(this)).build();
        try {
            readTxt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().build());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


    private void readTxt() throws IOException {
        JSONObject jsonData = new JSONObject();

        Integer i = 0;
        i = parseSms(jsonData, i, 5);
        i = parseVinsh(jsonData, i, 3);


        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/MyFiles");
        directory.mkdirs();

        File file = new File(directory, "songs.txt");
        FileOutputStream fOut = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fOut);
        osw.write(jsonData.toString());
        osw.flush();
        osw.close();

        jsonData = new JSONObject();
        try {
            jsonData.put(String.valueOf(0), createSonTypesJson(0, "Колядки"));
            jsonData.put(String.valueOf(1), createSonTypesJson(1, "Щедрівки"));
            jsonData.put(String.valueOf(2), createSonTypesJson(2, "Засівання"));
            jsonData.put(String.valueOf(3), createSonTypesJson(3, "Віншування"));
            jsonData.put(String.valueOf(4), createSonTypesJson(4, "Сучасні"));
            jsonData.put(String.valueOf(5), createSonTypesJson(5, "СМС"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        file = new File(directory, "songTypes.txt");
        fOut = new FileOutputStream(file);
        osw = new OutputStreamWriter(fOut);
        osw.write(jsonData.toString());
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

    private JSONObject createSonTypesJson(int id,  String title) {
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
            jsonObject.put("comments", "");
            jsonObject.put("updatedAt", 0);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
