package com.androidcollider.koljadnik.storage;

import android.util.Log;

import com.androidcollider.koljadnik.contants.Settings;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongType;
import com.androidcollider.koljadnik.root.SharedPreferencesManager;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class SongsRepository implements SongsDataSource {

    private SongsDataSource songsRealmDataSource, songsFirebaseDataSource;
    private SharedPreferencesManager sharedPreferencesManager;

    private List<Song> songs;
    private List<SongType> songTypes;

    public SongsRepository(@Local SongsDataSource songsRealmDataSource,
                           @Remote SongsDataSource songsFirebaseDataSource,
                           SharedPreferencesManager sharedPreferencesManager) {
        this.songsRealmDataSource = songsRealmDataSource;
        this.songsFirebaseDataSource = songsFirebaseDataSource;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public void getSongTypes(final OnReadListener<List<SongType>> onReadListener) {
        if (isNotExpired(sharedPreferencesManager.getLastUpdateForClass(SongType.class))) {
            if (songTypes != null) {
                onReadListener.onSuccess(songTypes);
            } else {
                getSongTypesFromLocal(onReadListener);
            }
        } else {
            getSongTypesFromRemote(onReadListener);
        }
    }

    private void getSongTypesFromLocal(final OnReadListener<List<SongType>> onReadListener) {
        songsRealmDataSource.getSongTypes(new OnReadListener<List<SongType>>() {
            @Override
            public void onSuccess(List<SongType> result) {
                cashSongTypes(result);
                onReadListener.onSuccess(result);
            }

            @Override
            public void onError(String error) {
                onReadListener.onError(error);
            }
        });
    }

    private void getSongTypesFromRemote(final OnReadListener<List<SongType>> onReadListener) {
        songsFirebaseDataSource.getSongTypes(new OnReadListener<List<SongType>>() {
            @Override
            public void onSuccess(List<SongType> result) {
                sharedPreferencesManager.setLastUpdateForClass(SongType.class, System.currentTimeMillis());
                songsRealmDataSource.saveSongTypes(result, new OnWriteListener() {
                    @Override
                    public void onSuccess() {
                        getSongTypesFromLocal(onReadListener);
                    }

                    @Override
                    public void onError(String error) {
                        onReadListener.onError(error);
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void cashSongTypes(List<SongType> result) {
        songTypes = result;
    }


    @Override
    public void getSongs(OnReadListener<List<Song>> onReadListener) {
        if (isNotExpired(sharedPreferencesManager.getLastUpdateForClass(Song.class))) {
            if (songs != null) {
                onReadListener.onSuccess(songs);
            } else {
                getSongsFromLocal(onReadListener);
            }
        } else {
            getSongsFromRemote(onReadListener);
        }
    }

    private void getSongsFromLocal(final OnReadListener<List<Song>> onReadListener) {
        songsRealmDataSource.getSongs(new OnReadListener<List<Song>>() {
            @Override
            public void onSuccess(List<Song> result) {
                cashSongs(result);
                onReadListener.onSuccess(result);
            }

            @Override
            public void onError(String error) {
                onReadListener.onError(error);
            }
        });
    }

    private void getSongsFromRemote(final OnReadListener<List<Song>> onReadListener) {
        songsFirebaseDataSource.getSongs(new OnReadListener<List<Song>>() {
            @Override
            public void onSuccess(List<Song> result) {
                sharedPreferencesManager.setLastUpdateForClass(Song.class, System.currentTimeMillis());
                songsRealmDataSource.saveSongs(result, new OnWriteListener() {
                    @Override
                    public void onSuccess() {
                        getSongsFromLocal(onReadListener);
                    }

                    @Override
                    public void onError(String error) {
                        onReadListener.onError(error);
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void cashSongs(List<Song> result) {
        songs = result;
    }

    private boolean isNotExpired(long lastUpdate) {
        return System.currentTimeMillis() - lastUpdate < Settings.DELTA_TIME_FOR_UPDATE;
    }

    @Override
    public void saveSongTypes(List<SongType> songTypes, OnWriteListener onWriteListener) {

    }

    @Override
    public void saveSongs(List<Song> songs, OnWriteListener onWriteListener) {

    }
}
