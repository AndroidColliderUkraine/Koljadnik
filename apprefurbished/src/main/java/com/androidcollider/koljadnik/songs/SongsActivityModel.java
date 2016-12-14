package com.androidcollider.koljadnik.songs;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongType;
import com.androidcollider.koljadnik.song_types.SongTypeViewModel;
import com.androidcollider.koljadnik.song_types.SongTypesActivityMVP;
import com.androidcollider.koljadnik.storage.SongsDataSource;

import java.util.ArrayList;
import java.util.List;

public class SongsActivityModel implements SongsActivityMVP.Model {

    private SongsDataSource songsDataSource;
    private long minRating, maxRating;

    public SongsActivityModel(SongsDataSource songsDataSource) {
        this.songsDataSource = songsDataSource;
    }

    @Override
    public void getSongsByTypeId(int typeId, final OnReadListener<List<SongItemViewModel>> listener) {
        songsDataSource.getSongs(new OnReadListener<List<Song>>() {
            @Override
            public void onSuccess(List<Song> resultSong) {
                List<Song> songsByTypeId = new ArrayList<>();

                minRating = resultSong.get(0).getRating();
                maxRating = resultSong.get(0).getRating();

                for (Song song: resultSong){
                    if (song.getRating() > maxRating){
                        maxRating = song.getRating();
                    }
                    if (song.getRating() < minRating){
                        minRating = song.getRating();
                    }
                    if (song.getIdType() == typeId){
                        songsByTypeId.add(song);
                    }
                }
                List<SongItemViewModel> songItemViewModels = new ArrayList<>();
                for (Song song: songsByTypeId){
                    songItemViewModels.add(new SongItemViewModel(song.getId(), song.getName(), song.getRatingByMinMax(minRating, maxRating)));
                }
                listener.onSuccess(songItemViewModels);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }
}
