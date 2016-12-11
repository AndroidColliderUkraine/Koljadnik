package com.androidcollider.koljadnik.repository;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.song_types.SongType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SongTypesFirebaseDataSource implements SongTypesDataSource {

    private DatabaseReference mDatabase;

    public SongTypesFirebaseDataSource() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getSongTypes(final OnReadListener<List<SongType>> onReadListener) {
        mDatabase.child("songTypes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<SongType> list = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    list.add(new SongType(postSnapshot.child("id").getValue(Integer.class),
                            postSnapshot.child("name").getValue(String.class)));
                }
                onReadListener.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onReadListener.onError(databaseError.getMessage());
            }
        });
    }
}
