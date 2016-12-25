package com.androidcollider.koljadnik.utils;


public class SessionSettingsManager {

    public static final int DEFAULT_TEXT_SIZE = 16;
    public int textSize = DEFAULT_TEXT_SIZE;

    public void increaseTextSize() {
        textSize++;
    }

    public void reduceTextSize() {
        textSize--;
    }

    public int getTextSize() {
        return textSize;
    }
}
