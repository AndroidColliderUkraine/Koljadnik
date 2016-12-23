package com.androidcollider.koljadnik.storage;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class AssetsTextDataManager {

    private final static String SONGS_DATA_FILE = "songs.txt";
    private final static String SONG_TYPES_DATA_FILE = "songTypes.txt";

    private Context context;

    public AssetsTextDataManager(Context context) {
        this.context = context;
    }

    public String getSongsFileData(){
        return getTextFromAssetFile(SONGS_DATA_FILE);
    }

    public String getSongTypesFileData(){
        return getTextFromAssetFile(SONG_TYPES_DATA_FILE);
    }

    private String getTextFromAssetFile(String filename){
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
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
        return text.toString();
    }
}
