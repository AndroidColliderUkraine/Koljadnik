package com.androidcollider.koljadnik.songs_list;

import android.support.v4.util.Pair;

import com.androidcollider.koljadnik.constants.Settings;
import com.androidcollider.koljadnik.constants.UiAction;
import com.androidcollider.koljadnik.listeners.CallStartProgressDialog;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongRating;
import com.androidcollider.koljadnik.storage.SongsDataSource;
import com.androidcollider.koljadnik.utils.SearchSongsAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class SongsActivityModel implements SongsActivityMVP.Model {

    private SongsDataSource songsDataSource;

    private int typeId;

    public SongsActivityModel(SongsDataSource songsDataSource, int typeId) {
        this.songsDataSource = songsDataSource;
        this.typeId = typeId;
    }

    @Override
    public void getSongsByTypeId(final OnReadListener<List<SongItemViewModel>> listener,
                                     final CallStartProgressDialog dialogCall) {
        UiAction uiAction = songsDataSource.getSongsByType(typeId, new OnReadListener<List<Song>>() {
            @Override
            public void onSuccess(List<Song> resultSong) {
                UiAction uiAction = songsDataSource.getRatings(new OnReadListener<List<SongRating>>() {
                    @Override
                    public void onSuccess(List<SongRating> result) {
                        Pair<Long, Long> minMaxRatings = SongRating.findMinMax(result);

                        List<SongItemViewModel> songItemViewModels = new ArrayList<>();
                        for (Song song : resultSong) {
                            int rating = Settings.DEFAULT_RATING;
                            SongRating ratingObj = SongRating.findInListById(result, song.getId());
                            if (ratingObj != null) {
                                rating = ratingObj.getRatingByMinMax(minMaxRatings.first, minMaxRatings.second);
                            }

                            songItemViewModels.add(new SongItemViewModel(song.getId(), song.getName(), song.getText(), rating));
                        }
                        listener.onSuccess(songItemViewModels);
                    }

                    @Override
                    public void onError(String error) {
                        listener.onError(error);
                    }
                });
                if (uiAction == UiAction.BLOCK_UI){
                    dialogCall.onCall();
                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
        if (uiAction == UiAction.BLOCK_UI){
            dialogCall.onCall();
        }
    }

    @Override
    public void getSongsBySearchAndOrdered(String searchStr, OrderType orderType, OnReadListener<List<SongItemViewModel>> listener,
                                               final CallStartProgressDialog dialogCall) {
        getSongsByTypeId(new OnReadListener<List<SongItemViewModel>>() {
            @Override
            public void onSuccess(List<SongItemViewModel> result) {
                if (searchStr.length() >= Settings.SEARCH_LIMIT) {
                    new SearchSongsAsyncTask(result) {
                        @Override
                        public void onSuccess(List<SongItemViewModel> songItemViewModels) {
                            getSongsOrdered(songItemViewModels, orderType, listener);
                        }
                    }.execute(searchStr);
                } else {
                    getSongsOrdered(result, orderType, listener);
                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        }, dialogCall);
    }

    @Override
    public void increaseRating(int songId) {
        songsDataSource.getSongById(songId, new OnReadListener<Song>() {
            @Override
            public void onSuccess(Song result) {
                songsDataSource.increaseSongLocalRating(result);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void getSongsOrdered(List<SongItemViewModel> songItemViewModels, OrderType orderType, OnReadListener<List<SongItemViewModel>> listener) {
        SongItemViewModel.orderBy(songItemViewModels, orderType);
        listener.onSuccess(songItemViewModels);
    }
}
