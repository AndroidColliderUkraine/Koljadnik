package com.androidcollider.koljadnik.song_details;


public class SongDetailsViewModel {

    public final int songId;
    public final String name;
    private String text;
    public final String source;

    public SongDetailsViewModel(int songId, String name, String text, String source) {
        this.songId= songId;
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
