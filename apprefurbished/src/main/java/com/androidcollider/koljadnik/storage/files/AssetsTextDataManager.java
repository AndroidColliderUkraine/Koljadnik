package com.androidcollider.koljadnik.storage.files;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class AssetsTextDataManager {

    private final static String DATA_FILE = "data.json";

    private Context context;

    public AssetsTextDataManager(Context context) {
        this.context = context;
    }

    public String getLocalData(){
        return getTextFromAssetFile(DATA_FILE);
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
