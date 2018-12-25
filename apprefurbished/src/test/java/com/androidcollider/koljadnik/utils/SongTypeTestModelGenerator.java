package com.androidcollider.koljadnik.utils;

import com.androidcollider.koljadnik.models.SongType;

/**
 * @author Severyn Parkhomenko <pseverin@ukr.net>
 */
public class SongTypeTestModelGenerator {

    private final static String[] NAMES = new String[]{
            "Тест колядки",
            "Тест щедрівки",
            "Тест засівання"
    };

    private final static long[] UPDATED_AT_ARRAY = new long[]{
            4984,
            1497849,
            778545
    };

    public static SongType generate(int id) {
        return new SongType(id, generateName(), generateUpdatedAt());
    }

    private static String generateName() {
        return NAMES[RandomUtils.randInt(0, NAMES.length - 1)];
    }

    private static long generateUpdatedAt() {
        return UPDATED_AT_ARRAY[RandomUtils.randInt(0, UPDATED_AT_ARRAY.length - 1)];
    }
}
