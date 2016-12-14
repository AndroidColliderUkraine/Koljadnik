package com.androidcollider.koljadnik.storage;


import android.util.Log;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SongsRealmDataSource implements SongsDataSource {

    private Realm realm;

    public SongsRealmDataSource(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void getSongTypes(OnReadListener<List<SongType>> onReadListener) {
        try {
            Log.i("qqq", "0");
            RealmResults<SongType> realmResults = realm.where(SongType.class).
                    findAllAsync();
            if (realmResults.isLoaded()) {
                onReadListener.onSuccess(realm.copyFromRealm(realmResults));
            } else {
                realmResults.addChangeListener(element -> {
                    Log.i("qqq", "02");
                    onReadListener.onSuccess(realm.copyFromRealm(element));
                    realmResults.removeChangeListeners();
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            onReadListener.onError(e.getMessage());
        }
    }

    @Override
    public void getSongs(OnReadListener<List<Song>> onReadListener) {
        try {
            RealmResults<Song> realmResults = realm.where(Song.class).
                    findAllAsync();
            if (realmResults.isLoaded()) {
                onReadListener.onSuccess(realm.copyFromRealm(realmResults));
            } else {
                realmResults.addChangeListener(element -> {
                    onReadListener.onSuccess(realm.copyFromRealm(element));
                    realmResults.removeChangeListeners();

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            onReadListener.onError(e.getMessage());
        }

    }

    @Override
    public void saveSongTypes(List<SongType> songTypes, OnWriteListener onWriteListener) {
        realm.executeTransactionAsync(realm1 -> {
            realm1.copyToRealmOrUpdate(songTypes);
        }, onWriteListener::onSuccess);
    }

    @Override
    public void saveSongs(List<Song> songs, OnWriteListener onWriteListener) {
        realm.executeTransactionAsync(realm1 -> {
            realm1.copyToRealmOrUpdate(songs);
        }, onWriteListener::onSuccess);
    }
}
