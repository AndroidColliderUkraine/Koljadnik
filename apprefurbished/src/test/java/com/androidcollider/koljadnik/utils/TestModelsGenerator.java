package com.androidcollider.koljadnik.utils;

import com.androidcollider.koljadnik.models.SongType;
import com.androidcollider.koljadnik.song_types.SongTypeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Severyn Parkhomenko <pseverin@ukr.net>
 */
public class TestModelsGenerator {

    public static List<SongType> generateSongTypeList(int size){
        List<SongType> songTypes = new ArrayList<>();
        for (int i = 0; i < size; i++){
            songTypes.add(SongTypeTestModelGenerator.generate(i));
        }
        return songTypes;
    }

    public static List<SongTypeViewModel> generateSongTypeViewModelsList(int size){
        List<SongTypeViewModel> songTypes = new ArrayList<>();
        for (int i = 0; i < size; i++){
            songTypes.add(SongTypeViewModelTestModelGenerator.generate(i));
        }
        return songTypes;
    }
}
