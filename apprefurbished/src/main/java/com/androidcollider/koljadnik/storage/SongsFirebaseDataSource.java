package com.androidcollider.koljadnik.storage;

import android.util.Log;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongsFirebaseDataSource implements SongsRemoteDataSource {

    private DatabaseReference mDatabase;

    public SongsFirebaseDataSource() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getSongTypes(long lastUpdate, final OnReadListener<List<SongType>> onReadListener) {
        mDatabase.child(FirebaseTable.SONG_TYPES.label).orderByChild("updatedAt").startAt(lastUpdate).
                addListenerForSingleValueEvent(new ValueEventListener() {
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
                Log.i("getSongTypesFIREBASE", list + " " + list.size());
                onReadListener.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onReadListener.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getSongs(long lastUpdate, final OnReadListener<List<Song>> onReadListener) {
        mDatabase.child(FirebaseTable.SONGS.label).orderByChild("updatedAt").startAt(1483557837000L). //TODO temp
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Song> list = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    try {
                        int id = postSnapshot.child("id").getValue(Integer.class);
                        int idType = postSnapshot.child("idType").getValue(Integer.class);
                        String name = postSnapshot.child("name").getValue(String.class);
                        long rating = postSnapshot.child("rating").getValue(Long.class);
                        String text = postSnapshot.child("text").getValue(String.class);
                        Long updatedAt = postSnapshot.child("updatedAt").getValue(Long.class);
                        String remarks = "";
                        if (postSnapshot.hasChild("remarks")) {
                            remarks = postSnapshot.child("remarks").getValue(String.class);
                        }
                        String source = "";
                        if (postSnapshot.hasChild("source")) {
                            source = postSnapshot.child("source").getValue(String.class);
                        }
                        list.add(new Song(id, name, rating, idType, text, remarks, source, updatedAt));
                    } catch (DatabaseException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("getSongsFIREBASE", list + " " + list.size());
                onReadListener.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onReadListener.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void updateSongs(List<Song> songs, OnWriteListener onWriteListener) {
        for (Song song : songs) {
            Map<String, Object> listToUpdate = new HashMap<>();
            listToUpdate.put(String.valueOf(song.getId()), song.toMap());
            mDatabase.child(FirebaseTable.SONGS.label).updateChildren(listToUpdate);
        }
    }
}
