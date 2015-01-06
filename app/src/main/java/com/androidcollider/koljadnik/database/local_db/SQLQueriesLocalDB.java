package com.androidcollider.koljadnik.database.local_db;

/**
 * Created by pseverin on 22.12.14.
 */
public class SQLQueriesLocalDB {
    //make a string SQL request for Song table
    public static final String create_song_table = "CREATE TABLE Song (" +
            "id_song          INTEGER PRIMARY KEY NOT NULL," +
            "update_time      INTEGER NOT NULL," +
            "name             TEXT NOT NULL," +
            "id_type          INTEGER NOT NULL," +
            "rating           INTEGER NOT NULL," +
            "my_local_rating  INTEGER NOT NULL" +
            ");";
    //make a string SQL request for SongType table
    public static final String create_song_type_table = "CREATE TABLE SongType (" +
            "id_type          INTEGER PRIMARY KEY NOT NULL," +
            "update_time      INTEGER NOT NULL," +
            "name             TEXT NOT NULL" +
            ");";
    //make a string SQL request for Text table
    public static final String create_text_table = "CREATE TABLE Text (" +
            "id_text          INTEGER PRIMARY KEY NOT NULL," +
            "update_time      INTEGER NOT NULL," +
            "id_song          INTEGER NOT NULL," +
            "data             TEXT NOT NULL," +
            "remarks          TEXT NOT NULL," +
            "source           TEXT NOT NULL" +
            ");";
    //make a string SQL request for Chord table
    public static final String create_chord_table = "CREATE TABLE Chord (" +
            "id_chord         INTEGER PRIMARY KEY AUTOINCREMENT," +
            "update_time      INTEGER NOT NULL," +
            "id_song          INTEGER NOT NULL," +
            "data             TEXT NOT NULL" +
            ");";
    //make a string SQL request for Note table
    public static final String create_note_table = "CREATE TABLE Note (" +
            "id_note          INTEGER PRIMARY KEY AUTOINCREMENT," +
            "update_time      INTEGER NOT NULL," +
            "id_song          INTEGER NOT NULL," +
            "data             TEXT NOT NULL" +
            ");";
    //make a string SQL request for Comment table
    public static final String create_comment_table = "CREATE TABLE Comment (" +
            "id_comment       INTEGER PRIMARY KEY AUTOINCREMENT," +
            "update_time      INTEGER NOT NULL," +
            "id_song          INTEGER NOT NULL," +
            "data             TEXT NOT NULL" +
            ");";
    //make a string SQL request for LastUpdate table
    /*public static final String create_last_update_table = "CREATE TABLE LastUpdate (" +
            "id_comment          INTEGER PRIMARY KEY AUTOINCREMENT," +
            "createdAt        INTEGER NOT NULL," +
            "updatedAt        INTEGER NOT NULL," +
            "id_song          INTEGER NOT NULL" +
            "data             TEXT NOT NULL," +
            ");";*/
}
