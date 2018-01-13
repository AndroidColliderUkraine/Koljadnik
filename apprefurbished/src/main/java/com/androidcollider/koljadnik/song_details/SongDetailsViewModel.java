package com.androidcollider.koljadnik.song_details;


public class SongDetailsViewModel {

    public final String name;
    private String text;
    public final String source;

    public SongDetailsViewModel(String name, String text, String source) {
        this.name = name;
        this.text = text;
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
