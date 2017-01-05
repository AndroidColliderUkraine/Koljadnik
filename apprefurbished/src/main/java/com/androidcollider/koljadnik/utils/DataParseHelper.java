package com.androidcollider.koljadnik.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by pseverin on 02.01.17.
 */

public class DataParseHelper {

    public static void readTxt(Context context, String filename) throws IOException, JSONException {
        JSONObject jsonData = new JSONObject();

        Integer i = 0;

        i = parseMain(context, "koljadki.txt", jsonData, i, 0);
        i = parseMain(context, "schedrivki.txt", jsonData, i, 1);
        i = parseMain(context, "zasivannia.txt", jsonData, i, 2);
        i = parseVinsh(context, jsonData, i, 3);
        i = parseMain(context, "suchasni.txt", jsonData, i, 4);
        i = parseSms(context, jsonData, i, 5);
        i = parseInshomovni(context, jsonData, i, 6);


        JSONObject jsonDatatypes = new JSONObject();
        jsonDatatypes.put(String.valueOf(0), createSonTypesJson(0, "Колядки"));
        jsonDatatypes.put(String.valueOf(1), createSonTypesJson(1, "Щедрівки"));
        jsonDatatypes.put(String.valueOf(2), createSonTypesJson(2, "Засівання"));
        jsonDatatypes.put(String.valueOf(3), createSonTypesJson(3, "Віншування"));
        jsonDatatypes.put(String.valueOf(4), createSonTypesJson(4, "Сучасні"));
        jsonDatatypes.put(String.valueOf(5), createSonTypesJson(5, "СМС"));
        jsonDatatypes.put(String.valueOf(6), createSonTypesJson(6, "Іншомовні"));


        JSONObject jsonDataRatings = new JSONObject();
        for (int k = 0; k < i; k++){
            jsonDataRatings.put(String.valueOf(k), createSongratingJson(k));
        }

        JSONObject finalJson = new JSONObject();

        finalJson.put("songs", jsonData);
        finalJson.put("songTypes", jsonDatatypes);
        finalJson.put("songRatings", jsonDataRatings);


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

        File file = new File(directory, filename);
        FileOutputStream fOut = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fOut);
        osw.write(finalJson.toString());
        osw.flush();
        osw.close();

    }

    public static int parseVinsh(Context context, JSONObject jsonData, Integer i, int typeId) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("vinsh.txt")));
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

    public static int parseSms(Context context, JSONObject jsonData, Integer i, int typeId) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("sms.txt")));
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

    public static int parseMain(Context context, String filename, JSONObject jsonData, Integer i, int typeId) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
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

    public static int parseInshomovni(Context context, JSONObject jsonData, Integer i, int typeId) throws IOException, JSONException {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("inshomovni.txt")));
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


    private static JSONObject createSonTypesJson(int id, String title) {
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

    private static JSONObject createSongratingJson(int songId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("songId", songId);
            jsonObject.put("rating", 0);
            jsonObject.put("updatedAt", 0);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static JSONObject createSongJson(int id, int idType, String title, String text, String source) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("name", title);
            //jsonObject.put("rating", 0);
            jsonObject.put("idType", idType);
            jsonObject.put("text", text);
            jsonObject.put("remarks", "");
            jsonObject.put("source", source);
            jsonObject.put("updatedAt", 0);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
