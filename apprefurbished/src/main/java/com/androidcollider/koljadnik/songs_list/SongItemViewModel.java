package com.androidcollider.koljadnik.songs_list;


import com.androidcollider.koljadnik.utils.ChordTags;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SongItemViewModel {

    private final static Collator ukCollator = Collator.getInstance(new Locale("uk"));

    public final int songId;
    public final String name;
    public final String text;
    public final int rating;
    public final boolean hasNota;

    public SongItemViewModel(int songId, String name, String text, int rating) {
        this.songId = songId;
        this.name = name;
        this.rating = rating;
        this.text = text;
        this.hasNota = text.contains(ChordTags.CHORD_TAG_OPEN);
    }

    public static List<SongItemViewModel> orderBy(List<SongItemViewModel> list, OrderType orderType) {
        switch (orderType) {
            case BY_ALPHABET:
                Collections.sort(list, comparatorByAlphabet);
                break;
            case BY_RATING:
                Collections.sort(list, comparatorByRating);
                break;
        }
        return list;
    }

    private static Comparator<SongItemViewModel> comparatorByAlphabet = (o1, o2) -> ukCollator.compare(o1.name, o2.name);

    private static Comparator<SongItemViewModel> comparatorByRating = (o1, o2) -> Integer.valueOf(o2.rating).compareTo(o1.rating);
}
