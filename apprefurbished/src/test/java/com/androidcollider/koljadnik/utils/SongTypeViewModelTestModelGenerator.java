package com.androidcollider.koljadnik.utils;

import com.androidcollider.koljadnik.song_types.SongTypeViewModel;

/**
 * @author Severyn Parkhomenko <pseverin@ukr.net>
 */
public class SongTypeViewModelTestModelGenerator {

    private final static int[] QUANTITY = new int[]{
            23,
            432,
            32
    };

        public static SongTypeViewModel generate(int idsSongType) {
        return new SongTypeViewModel(SongTypeTestModelGenerator.generate(idsSongType), generateQuantity());
    }

    private static int generateQuantity() {
        return QUANTITY[RandomUtils.randInt(0, QUANTITY.length - 1)];
    }
}
