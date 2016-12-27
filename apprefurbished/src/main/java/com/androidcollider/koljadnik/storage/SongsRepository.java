package com.androidcollider.koljadnik.storage;

import android.support.v4.util.Pair;
import android.util.Log;

import com.androidcollider.koljadnik.contants.Settings;
import com.androidcollider.koljadnik.contants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongType;
import com.androidcollider.koljadnik.utils.ConnectionInternetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class SongsRepository implements SongsDataSource {

    private SongsLocalDataSource songsRealmDataSource;
    private SongsRemoteDataSource songsFirebaseDataSource;
    private SharedPreferencesManager sharedPreferencesManager;
    private AssetsTextDataManager assetsTextDataManager;
    private ConnectionInternetManager connectionInternetManager;

    private List<Song> cachedSongs;
    private List<SongType> cachedSongTypes;

    public SongsRepository(SongsLocalDataSource songsRealmDataSource,
                           SongsRemoteDataSource songsFirebaseDataSource,
                           SharedPreferencesManager sharedPreferencesManager,
                           ConnectionInternetManager connectionInternetManager,
                           AssetsTextDataManager assetsTextDataManager) {
        this.songsRealmDataSource = songsRealmDataSource;
        this.songsFirebaseDataSource = songsFirebaseDataSource;
        this.sharedPreferencesManager = sharedPreferencesManager;
        this.assetsTextDataManager = assetsTextDataManager;
        this.connectionInternetManager = connectionInternetManager;
    }

    @Override
    public UiAction getSongTypes(final OnReadListener<List<SongType>> onReadListener) {
        if (!connectionInternetManager.isNetworkConnected() ||
                isNotExpired(sharedPreferencesManager.getLastUpdateForClass(SongType.class))) {
            if (cachedSongTypes != null) {
                onReadListener.onSuccess(cachedSongTypes);
                return UiAction.DONT_BLOCK_UI;
            } else {
                onReadListener.onSuccess(getSongTypesFromLocal());
                return UiAction.DONT_BLOCK_UI;
            }
        } else {
            getSongTypesFromRemote(new OnReadListener<List<SongType>>() {
                @Override
                public void onSuccess(List<SongType> result) {
                    onReadListener.onSuccess(result);
                }

                @Override
                public void onError(String error) {
                    onReadListener.onSuccess(getSongTypesFromLocal());
                }
            });
            return UiAction.BLOCK_UI;
        }
    }

    private List<SongType> getSongTypesFromLocal() {
        List<SongType> songTypes = songsRealmDataSource.getSongTypes();
        cashSongTypes(songTypes);
        return songTypes;
    }

    private void getSongTypesFromRemote(final OnReadListener<List<SongType>> onReadListener) {
        sharedPreferencesManager.setLastUpdateForClass(SongType.class, System.currentTimeMillis());
        songsFirebaseDataSource.getSongTypes(sharedPreferencesManager.getLastUpdateForClass(SongType.class), new OnReadListener<List<SongType>>() {
            @Override
            public void onSuccess(List<SongType> firebaseList) {
                songsRealmDataSource.saveSongTypes(firebaseList, new OnWriteListener() {
                    @Override
                    public void onSuccess() {
                        onReadListener.onSuccess(getSongTypesFromLocal());
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
    public UiAction getSongs(OnReadListener<List<Song>> onReadListener) {
        if (!connectionInternetManager.isNetworkConnected() ||
                isNotExpired(sharedPreferencesManager.getLastUpdateForClass(Song.class))) {
            if (cachedSongs != null) {
                onReadListener.onSuccess(cachedSongs);
                return UiAction.DONT_BLOCK_UI;
            } else {
                onReadListener.onSuccess(getSongsFromLocal());
                return UiAction.DONT_BLOCK_UI;
            }
        } else {
            getSongsFromRemote(new OnReadListener<List<Song>>() {
                @Override
                public void onSuccess(List<Song> result) {
                    onReadListener.onSuccess(result);
                }

                @Override
                public void onError(String error) {
                    onReadListener.onSuccess(getSongsFromLocal());
                }
            });
            return UiAction.BLOCK_UI;
        }
    }

    private List<Song> getSongsFromLocal() {
        List<Song> songs = songsRealmDataSource.getSongs();
        cashSongs(songs);
        return songs;
    }

    private void getSongsFromRemote(final OnReadListener<List<Song>> onReadListener) {
        sharedPreferencesManager.setLastUpdateForClass(Song.class, System.currentTimeMillis());
        songsFirebaseDataSource.getSongs(sharedPreferencesManager.getLastUpdateForClass(Song.class), new OnReadListener<List<Song>>() {
            @Override
            public void onSuccess(List<Song> firebaseList) {
                List<Song> realmList = songsRealmDataSource.getSongs();
                List<Song> listToUpdate = mergeRatings(realmList, firebaseList);
                onReadListener.onSuccess(realmList);
                songsFirebaseDataSource.updateSongs(listToUpdate, null);
                songsRealmDataSource.saveSongs(listToUpdate, null);
            }

            @Override
            public void onError(String error) {
                onReadListener.onError(error);
            }
        });
    }

    private List<Song> mergeRatings(List<Song> realmList, List<Song> firebaseList) {
        List<Song> listToUpdate = new ArrayList<>();
        for (Song realmSong : realmList) {
            if (realmSong.getLocalRating() > 0) {
                Song songToUpdate = realmSong;
                Song firebaseSong = Song.findInListById(firebaseList, realmSong.getId());
                if (firebaseSong != null) {
                    songToUpdate = firebaseSong;
                }
                songToUpdate.setRating(songToUpdate.getRating() + realmSong.getLocalRating());
                songToUpdate.setUpdatedAt(System.currentTimeMillis());
                realmList.set(realmList.indexOf(realmSong), songToUpdate);
                listToUpdate.add(songToUpdate);
            }
        }
        return listToUpdate;
    }

    private void cashSongs(List<Song> result) {
        cachedSongs = result;
    }

    private boolean isNotExpired(long lastUpdate) {
        return System.currentTimeMillis() - lastUpdate < Settings.DELTA_TIME_FOR_UPDATE;
    }

    @Override
    public UiAction getSongsByType(int typeId, OnReadListener<List<Song>> onReadListener) {
        return getSongs(new OnReadListener<List<Song>>() {
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
    public UiAction getSongById(int songId, OnReadListener<Song> onReadListener) {
        return getSongs(new OnReadListener<List<Song>>() {
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
    public UiAction getMinMaxRating(OnReadListener<Pair<Long, Long>> onReadListener) {
        return getSongs(new OnReadListener<List<Song>>() {
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
    public void tryToLoadDataFromLocalFile() {
        if (!sharedPreferencesManager.isAlreadyParsedDataFromLocal()) {
            String jsonString = assetsTextDataManager.getLocalData();
            try {
                List<Song> songsData = Song.generateSongList(new JSONObject(jsonString).getJSONArray("songs"));
                List<SongType> songsTypesData = SongType.generateSongTypesList(new JSONObject(jsonString).getJSONArray("songTypes"));

                songsRealmDataSource.saveSongs(songsData, null);
                songsRealmDataSource.saveSongTypes(songsTypesData, null);
                sharedPreferencesManager.setAlreadyParsedDataFromLocal(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
