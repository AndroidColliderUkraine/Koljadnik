package com.androidcollider.koljadnik.storage;

import com.androidcollider.koljadnik.contants.FirebaseTable;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.Song;
import com.androidcollider.koljadnik.models.SongType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SongsFirebaseDataSource implements SongsDataSource {

    private DatabaseReference mDatabase;

    public SongsFirebaseDataSource() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getSongTypes(final OnReadListener<List<SongType>> onReadListener) {
        mDatabase.child(FirebaseTable.SONG_TYPES.label).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<SongType> list = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    try {
                        list.add(postSnapshot.getValue(SongType.class));
                    } catch (DatabaseException e) {
                        e.printStackTrace();
                    }
                }
                onReadListener.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onReadListener.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getSongs(final OnReadListener<List<Song>> onReadListener) {
        mDatabase.child(FirebaseTable.SONGS.label).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Song> list = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    try {
                        list.add(postSnapshot.getValue(Song.class));
                    } catch (DatabaseException e) {
                        e.printStackTrace();
                    }
                }
                onReadListener.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onReadListener.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void saveSongTypes(List<SongType> songTypes, OnWriteListener onWriteListener) {

    }

    @Override
    public void saveSongs(List<Song> songs, OnWriteListener onWriteListener) {

    }
}
