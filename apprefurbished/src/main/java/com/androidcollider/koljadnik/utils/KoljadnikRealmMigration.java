package com.androidcollider.koljadnik.utils;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Description of KoljadnikRealmMigration
 */
public class KoljadnikRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if (oldVersion < 2) {
            schema.create("SongRating")
                    .addField("idSong", int.class)
                    .addField("rating", long.class)
                    .addField("localRating", long.class)
                    .addField("updatedAt", long.class)
                    .addPrimaryKey("idSong");

            schema.get("Song").removeField("rating").removeField("localRating");
        }
        if (oldVersion < 3) {
            schema.create("LocationEvent")
                    .addField("id", String.class)
                    .addField("songId", int.class)
                    .addField("lat", String.class)
                    .addField("lng", String.class)
                    .addField("timestamp", String.class)
                    .addPrimaryKey("id");
        }
    }
}
