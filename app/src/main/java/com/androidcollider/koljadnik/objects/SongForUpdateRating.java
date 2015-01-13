package com.androidcollider.koljadnik.objects;

/**
 * Created by Пархоменко on 12.01.2015.
 */
public class SongForUpdateRating {

    private int id;
    private long ratingIncrement;

    public SongForUpdateRating(int id, long ratingIncrement) {
        this.id = id;
        this.ratingIncrement = ratingIncrement;
    }

    public int getId() {
        return id;
    }

    public long getRatingIncrement() {
        return ratingIncrement;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRatingIncrement(long ratingIncrement) {
        this.ratingIncrement = ratingIncrement;
    }
}
