package com.androidcollider.koljadnik.storage;

import android.support.v4.util.Pair;

import com.androidcollider.koljadnik.contants.Settings;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class SongsRepository implements SongsDataSource {

    private SongsDataSource songsRealmDataSource, songsFirebaseDataSource;
    private SharedPreferencesManager sharedPreferencesManager;
    private AssetsTextDataManager assetsTextDataManager;

    private List<Song> cachedSongs;
    private List<SongType> cachedSongTypes;

    public SongsRepository(@Local SongsDataSource songsRealmDataSource,
                           @Remote SongsDataSource songsFirebaseDataSource,
                           SharedPreferencesManager sharedPreferencesManager,
                           AssetsTextDataManager assetsTextDataManager) {
        this.songsRealmDataSource = songsRealmDataSource;
        this.songsFirebaseDataSource = songsFirebaseDataSource;
        this.sharedPreferencesManager = sharedPreferencesManager;
        this.assetsTextDataManager = assetsTextDataManager;
    }

    @Override
    public void getSongTypes(final OnReadListener<List<SongType>> onReadListener) {
        if (isNotExpired(sharedPreferencesManager.getLastUpdateForClass(SongType.class))) {
            if (cachedSongTypes != null) {
                onReadListener.onSuccess(cachedSongTypes);
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
            public void onSuccess(List<SongType> firebaseList) {
                sharedPreferencesManager.setLastUpdateForClass(SongType.class, System.currentTimeMillis());

                songsRealmDataSource.saveSongTypes(firebaseList, new OnWriteListener() {
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
        cachedSongTypes = result;
    }


    @Override
    public void getSongs(OnReadListener<List<Song>> onReadListener) {
        if (isNotExpired(sharedPreferencesManager.getLastUpdateForClass(Song.class))) {
            if (cachedSongs != null) {
                onReadListener.onSuccess(cachedSongs);
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
            public void onSuccess(List<Song> firebaseList) {
                sharedPreferencesManager.setLastUpdateForClass(Song.class, System.currentTimeMillis());
                songsRealmDataSource.getSongs(new OnReadListener<List<Song>>() {
                    @Override
                    public void onSuccess(List<Song> realmList) {
                        mergeRatings(realmList, firebaseList);
                        songsRealmDataSource.saveSongs(firebaseList, new OnWriteListener() {
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

            @Override
            public void onError(String error) {

            }
        });
    }

    private void mergeRatings(List<Song> realmList, List<Song> firebaseList){
        List<Song> listToUpdate = new ArrayList<>();
        for (Song realmSong: realmList){
            if (realmSong.getLocalRating() > 0){
                Song firebaseSong = Song.findInListById(firebaseList, realmSong.getId());
                if (firebaseSong !=null) {
                    firebaseSong.setRating(firebaseSong.getRating() + realmSong.getLocalRating());
                    listToUpdate.add(firebaseSong);
                }
            }
        }
        songsFirebaseDataSource.updateSongs(listToUpdate, null);
    }

    private void cashSongs(List<Song> result) {
        cachedSongs = result;
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

    @Override
    public void getSongsByType(int typeId, OnReadListener<List<Song>> onReadListener) {
        getSongs(new OnReadListener<List<Song>>() {
            @Override
            public void onSuccess(List<Song> result) {
                List<Song> songsByType = new ArrayList<>();
                for (Song song : result) {
                    if (song.getIdType() == typeId) {
                        songsByType.add(song);
                    }
                }
                onReadListener.onSuccess(songsByType);
            }

            @Override
            public void onError(String error) {
                onReadListener.onError(error);
            }
        });
    }

    @Override
    public void getSongById(int songId, OnReadListener<Song> onReadListener) {
        getSongs(new OnReadListener<List<Song>>() {
            @Override
            public void onSuccess(List<Song> result) {
                for (Song song : result) {
                    if (song.getId() == songId) {
                        onReadListener.onSuccess(song);
                    }
                }
            }

            @Override
            public void onError(String error) {
                onReadListener.onError(error);
            }
        });
    }

    @Override
    public void getMinMaxRating(OnReadListener<Pair<Long, Long>> onReadListener) {
        getSongs(new OnReadListener<List<Song>>() {
            @Override
            public void onSuccess(List<Song> result) {

                long minRating = 0;
                long maxRating = 0;
                if (result.size() > 0) {
                    minRating = result.get(0).getTotalRating();
                    maxRating = result.get(0).getTotalRating();
                }
                for (Song song : result) {
                    if (song.getTotalRating() > maxRating) {
                        maxRating = song.getTotalRating();
                    }
                    if (song.getTotalRating() < minRating) {
                        minRating = song.getTotalRating();
                    }
                }
                onReadListener.onSuccess(new Pair<>(minRating, maxRating));
            }

            @Override
            public void onError(String error) {
                onReadListener.onError(error);
            }
        });
    }

    @Override
    public void increaseSongLocalRating(Song song) {
        if (cachedSongs != null) {
            for (Song cachedSong : cachedSongs) {
                if (cachedSong.getId() == song.getId()) {
                    cachedSong.increaseLocalRating();
                    songsRealmDataSource.increaseSongLocalRating(song);
                }
            }
        }
    }

    @Override
    public void updateSongs(List<Song> songs, OnWriteListener onWriteListener) {

    }

    @Override
    public void tryToLoadDataFromLocalFile() {
        if (!sharedPreferencesManager.getAlreadyParsedDataFromLocal()){
            String jsonString = assetsTextDataManager.getLocalData();
            try {
                List<Song> songsData = Song.generateSongList(new JSONObject(jsonString).getJSONObject("songs"));
                List<SongType> songsTypesData = SongType.generateSongTypesList(new JSONObject(jsonString).getJSONObject("songTypes"));

                songsRealmDataSource.saveSongs(songsData, null);
                songsRealmDataSource.saveSongTypes(songsTypesData, null);
                sharedPreferencesManager.setAlreadyParsedDataFromLocal(true);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
