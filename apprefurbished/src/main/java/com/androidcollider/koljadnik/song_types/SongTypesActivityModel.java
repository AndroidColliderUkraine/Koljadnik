package com.androidcollider.koljadnik.song_types;

import android.util.Log;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongType;
import com.androidcollider.koljadnik.storage.SongsDataSource;

import java.util.ArrayList;
import java.util.List;

public class SongTypesActivityModel implements SongTypesActivityMVP.Model {

    private SongsDataSource songsDataSource;

    public SongTypesActivityModel(SongsDataSource songsDataSource) {
        this.songsDataSource = songsDataSource;
    }

    @Override
    public void getSongTypes(final OnReadListener<List<SongTypeViewModel>> listener) {
        songsDataSource.getSongTypes(new OnReadListener<List<SongType>>() {
            @Override
            public void onSuccess(final List<SongType> resultSongType) {
                Log.i("qqq", "1");
                songsDataSource.getSongs(new OnReadListener<List<Song>>() {
                    @Override
                    public void onSuccess(List<Song> resultSong) {
                        Log.i("qqq", "2");
                        List<SongTypeViewModel> songTypeViewModels = new ArrayList<>();

                        for (SongType songType: resultSongType){
                            int quantity = 0;
                            for (Song song: resultSong){
                                if (songType.getId() == song.getIdType()){
                                    quantity++;
                                }
                            }
                            songTypeViewModels.add(new SongTypeViewModel(songType, quantity));
                        }
                        listener.onSuccess(songTypeViewModels);
                    }

                    @Override
                    public void onError(String error) {
                        listener.onError(error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }
}
