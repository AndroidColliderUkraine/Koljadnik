package com.androidcollider.koljadnik.storage;


import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SongsRealmDataSource implements SongsLocalDataSource {

    private Realm realm;

    public SongsRealmDataSource(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void getSongTypes(OnReadListener<List<SongType>> onReadListener) {
        try {
            RealmResults<SongType> realmResults = realm.where(SongType.class).
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
    public List<SongType> getSongTypes() {
        realm.beginTransaction();
        List<SongType> songTypes = realm.copyFromRealm(realm.where(SongType.class).
                findAll());

        realm.commitTransaction();
        return songTypes;
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
    public List<Song> getSongs() {
        realm.beginTransaction();
        List<Song> songs = realm.copyFromRealm(realm.where(Song.class).
                findAll());

        realm.commitTransaction();
        return songs;
    }

    @Override
    public void saveSongTypes(List<SongType> songTypes, OnWriteListener onWriteListener) {
        if (onWriteListener != null) {
            realm.executeTransactionAsync(realm1 -> {
                realm1.copyToRealmOrUpdate(songTypes);
            }, onWriteListener::onSuccess);
        } else {
            realm.executeTransactionAsync(realm1 -> {
                realm1.copyToRealmOrUpdate(songTypes);
            });
        }
    }

    @Override
    public void saveSongs(List<Song> songs, OnWriteListener onWriteListener) {
        if (onWriteListener != null) {
            realm.executeTransactionAsync(realm1 -> {
                realm1.copyToRealmOrUpdate(songs);
            }, onWriteListener::onSuccess);
        } else {
            realm.executeTransactionAsync(realm1 -> {
                realm1.copyToRealmOrUpdate(songs);
            });
        }
    }

    @Override
    public void increaseSongLocalRating(Song song) {
        realm.executeTransactionAsync(realm1 -> {
            realm1.copyToRealmOrUpdate(song);
        });
    }
}
