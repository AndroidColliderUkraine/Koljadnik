package com.androidcollider.koljadnik.database.local_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pseverin on 22.12.14.
 */
public class DBhelperLocalDB  extends SQLiteOpenHelper {

    Context context;
    private static final String DATABASE_NAME = "Koljadnik.db";
    private static final int DATABASE_VERSION = 2;

    public DBhelperLocalDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table Route and table Point
        db.execSQL(SQLQueriesLocalDB.create_song_table);
        db.execSQL(SQLQueriesLocalDB.create_song_type_table);
        db.execSQL(SQLQueriesLocalDB.create_text_table);
        db.execSQL(SQLQueriesLocalDB.create_chord_table);
        db.execSQL(SQLQueriesLocalDB.create_note_table);
        db.execSQL(SQLQueriesLocalDB.create_comment_table);
        db.execSQL(SQLQueriesLocalDB.create_my_comment_table);
    }
    // Method for update database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(SQLQueriesLocalDB.drop_ver_1_table_song);
            db.execSQL(SQLQueriesLocalDB.drop_ver_1_table_song_type);
            db.execSQL(SQLQueriesLocalDB.drop_ver_1_table_text);
            db.execSQL(SQLQueriesLocalDB.drop_ver_1_table_chord);
            db.execSQL(SQLQueriesLocalDB.drop_ver_1_table_note);
            db.execSQL(SQLQueriesLocalDB.drop_ver_1_table_comment);

            db.execSQL(SQLQueriesLocalDB.create_song_table);
            db.execSQL(SQLQueriesLocalDB.create_song_type_table);
            db.execSQL(SQLQueriesLocalDB.create_text_table);
            db.execSQL(SQLQueriesLocalDB.create_chord_table);
            db.execSQL(SQLQueriesLocalDB.create_note_table);
            db.execSQL(SQLQueriesLocalDB.create_comment_table);
            db.execSQL(SQLQueriesLocalDB.create_my_comment_table);
        }
    }
}
