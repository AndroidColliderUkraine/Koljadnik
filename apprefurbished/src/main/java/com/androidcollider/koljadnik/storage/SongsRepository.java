package com.androidcollider.koljadnik.storage;

import com.androidcollider.koljadnik.constants.Settings;
import com.androidcollider.koljadnik.constants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.LocationEvent;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongRating;
import com.androidcollider.koljadnik.models.SongType;
import com.androidcollider.koljadnik.storage.files.AssetsTextDataManager;
import com.androidcollider.koljadnik.storage.local.SongsLocalDataSource;
import com.androidcollider.koljadnik.storage.remote.SongsRemoteDataSource;
import com.androidcollider.koljadnik.storage.shared_prefs.SharedPreferencesManager;
import com.androidcollider.koljadnik.utils.ConnectionInternetManager;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnSuccessListener;

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
    private List<SongRating> cachedSongRatings;

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
        long lastUpdate = sharedPreferencesManager.getLastUpdateForClass(SongType.class);
        sharedPreferencesManager.setLastUpdateForClass(SongType.class, System.currentTimeMillis());
        songsFirebaseDataSource.getSongTypes(lastUpdate, new OnReadListener<List<SongType>>() {
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
                onReadListener.onError(error);
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
        long lastUpdate = sharedPreferencesManager.getLastUpdateForClass(Song.class);
        sharedPreferencesManager.setLastUpdateForClass(Song.class, System.currentTimeMillis());
        songsFirebaseDataSource.getSongs(lastUpdate, new OnReadListener<List<Song>>() {
            @Override
            public void onSuccess(List<Song> firebaseList) {
                songsRealmDataSource.saveSongs(firebaseList, new OnWriteListener() {
                    @Override
                    public void onSuccess() {
                        onReadListener.onSuccess(getSongsFromLocal());
                    }

                    @Override
                    public void onError(String error) {
                        onReadListener.onError(error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                onReadListener.onError(error);
            }
        });
    }

    private void cashSongs(List<Song> result) {
        cachedSongs = result;
    }

    private boolean isNotExpired(long lastUpdate) {
        return System.currentTimeMillis() - lastUpdate < Settings.DELTA_TIME_FOR_UPDATE;
    }

    private boolean isNotExpiredRating(long lastUpdate) {
        return System.currentTimeMillis() - lastUpdate < Settings.DELTA_TIME_FOR_UPDATE_RATING;
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
    public UiAction getRatings(OnReadListener<List<SongRating>> onReadListener) {
        if (!connectionInternetManager.isNetworkConnected() ||
            isNotExpiredRating(sharedPreferencesManager.getLastUpdateForClass(SongRating.class))) {
            if (cachedSongRatings != null) {
                onReadListener.onSuccess(cachedSongRatings);
                return UiAction.DONT_BLOCK_UI;
            } else {
                onReadListener.onSuccess(getSongRatingsFromLocal());
                return UiAction.DONT_BLOCK_UI;
            }
        } else {
            getSongRatingsFromRemote(new OnReadListener<List<SongRating>>() {
                @Override
                public void onSuccess(List<SongRating> result) {
                    onReadListener.onSuccess(result);
                }

                @Override
                public void onError(String error) {
                    onReadListener.onSuccess(getSongRatingsFromLocal());
                }
            });
            return UiAction.BLOCK_UI;
        }
    }


    private List<SongRating> getSongRatingsFromLocal() {
        List<SongRating> songRatings = songsRealmDataSource.getSongRatings();
        cashSongRatings(songRatings);
        return songRatings;
    }

    private void getSongRatingsFromRemote(final OnReadListener<List<SongRating>> onReadListener) {
        long lastUpdate = sharedPreferencesManager.getLastUpdateForClass(SongRating.class);
        final long updateTimestamp = System.currentTimeMillis();
        sharedPreferencesManager.setLastUpdateForClass(SongRating.class, updateTimestamp + 1);
        songsFirebaseDataSource.getSongRatings(lastUpdate, new OnReadListener<List<SongRating>>() {
            @Override
            public void onSuccess(List<SongRating> firebaseList) {
                List<SongRating> localSongRatings = songsRealmDataSource.getSongRatings();

                List<SongRating> listToUpdateOnServer = new ArrayList<>();
                List<SongRating> listToUpdateLocal = new ArrayList<>();

                mergeRatings(updateTimestamp, localSongRatings, firebaseList, listToUpdateOnServer, listToUpdateLocal);

                songsFirebaseDataSource.updateSongRatings(listToUpdateOnServer, null);
                songsRealmDataSource.saveSongRatings(listToUpdateLocal, new OnWriteListener() {
                    @Override
                    public void onSuccess() {
                        onReadListener.onSuccess(getSongRatingsFromLocal());
                    }

                    @Override
                    public void onError(String error) {
                        onReadListener.onError(error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                onReadListener.onError(error);
            }
        });

    }

    private void mergeRatings(long updateTimestamp, List<SongRating> realmList, List<SongRating> firebaseList,
                              List<SongRating> listToUpdateOnServer, List<SongRating> listToUpdateLocal) {
        for (SongRating realmSongRating : realmList) {
            if (realmSongRating.getLocalRating() > 0) {
                SongRating firebaseSongRating = SongRating.findInListById(firebaseList, realmSongRating.getIdSong());
                long countingRating;
                if (firebaseSongRating != null) {
                    countingRating = firebaseSongRating.getRating() + realmSongRating.getLocalRating();
                } else {
                    countingRating = realmSongRating.getRating() + realmSongRating.getLocalRating();
                }
                SongRating mergedSongRating = new SongRating(realmSongRating.getIdSong(), countingRating, updateTimestamp);
                listToUpdateOnServer.add(mergedSongRating);
                listToUpdateLocal.add(mergedSongRating);
            }
        }
        for (SongRating firebaseSongRating : firebaseList) {
            if (SongRating.findInListById(listToUpdateLocal, firebaseSongRating.getIdSong()) == null) {
                listToUpdateLocal.add(firebaseSongRating);
            }
        }
    }

    private void cashSongRatings(List<SongRating> result) {
        cachedSongRatings = result;
    }

    @Override
    public void increaseSongLocalRating(Song song) {
        if (cachedSongRatings != null) {
            getSongRatingsFromLocal();
        }
        if (cachedSongRatings != null) {
            for (SongRating cachedSongRating : cachedSongRatings) {
                if (cachedSongRating.getIdSong() == song.getId()) {
                    cachedSongRating.increaseLocalRating();
                    songsRealmDataSource.increaseSongLocalRating(cachedSongRating);
                }
            }
        }
    }

    @Override
    public void tryToLoadDataFromLocalFile() {
        String jsonString = null;
        if (!sharedPreferencesManager.isAlreadyParsedDataFromLocal()) {
            jsonString = assetsTextDataManager.getLocalData();
            try {
                JSONObject json = new JSONObject(jsonString);
                if (json.has("songs")) {
                    List<Song> songsData = Song.generateSongList(json.getJSONArray("songs"));
                    songsRealmDataSource.saveSongs(songsData, null);
                }
                if (json.has("songTypes")) {
                    List<SongType> songsTypesData = SongType.generateSongTypesList(json.getJSONArray("songTypes"));
                    songsRealmDataSource.saveSongTypes(songsTypesData, null);
                }

                sharedPreferencesManager.setAlreadyParsedDataFromLocal(true);
            } catch (JSONException e) {
                e.printStackTrace();
                Crashlytics.log(e.getMessage());
            }
        }

        if (!sharedPreferencesManager.isAlreadyParsedSongsRatingsFromLocal()) {
            if (jsonString == null) {
                jsonString = assetsTextDataManager.getLocalData();
            }
            try {
                List<SongRating> songRatingData = SongRating.generateSongRatingsList(new JSONObject(jsonString).getJSONArray("songRatings"));
                songsRealmDataSource.saveSongRatings(songRatingData, null);
                sharedPreferencesManager.setAlreadyParsedSongsRatingsFromLocal(true);
            } catch (JSONException e) {
                e.printStackTrace();
                Crashlytics.log(e.getMessage());
            }
        }
    }

    @Override
    public void saveLocationEvent(int songId, String lat, String lng) {
        songsRealmDataSource.addLocationEvent(new LocationEvent(songId, lat, lng), new OnWriteListener() {
            @Override
            public void onSuccess() {
                tryToUpdateLocationEvents();
            }

            @Override
            public void onError(String error) {
                Crashlytics.log(error);
            }
        });
    }

    @Override
    public void tryToUpdateLocationEvents() {
        if (connectionInternetManager.isNetworkConnected()){
            songsFirebaseDataSource.addLocationEvents(songsRealmDataSource.getLocationEvents(),
                    aVoid -> songsRealmDataSource.clearLocationEvents());
        }
    }
}
