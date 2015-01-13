package com.androidcollider.koljadnik.database.local_db;

/**
 * Created by pseverin on 22.12.14.
 */
public class SQLQueriesLocalDB {
    //make a string SQL request for Song table
    public static final String create_song_table = "CREATE TABLE Carol (" +
            "id_song          INTEGER PRIMARY KEY NOT NULL," +
            "update_time      INTEGER NOT NULL," +
            "name             TEXT NOT NULL," +
            "text             TEXT NOT NULL," +
            "source           TEXT NOT NULL," +
            "id_type          INTEGER NOT NULL," +
            "rating           INTEGER NOT NULL," +
            "my_local_rating  INTEGER NOT NULL" +
            ");";
    //make a string SQL request for SongType table
    public static final String create_song_type_table = "CREATE TABLE CarolType (" +
            "id_type          INTEGER PRIMARY KEY NOT NULL," +
            "update_time      INTEGER NOT NULL," +
            "name             TEXT NOT NULL" +
            ");";
    //make a string SQL request for Text table
    /*public static final String create_text_table = "CREATE TABLE CarolText (" +
            "id_text          INTEGER PRIMARY KEY NOT NULL," +
            "update_time      INTEGER NOT NULL," +
            "id_song          INTEGER NOT NULL," +
            "data             TEXT NOT NULL," +
            "remarks          TEXT NOT NULL," +
            "source           TEXT NOT NULL" +
            ");";*/
    //make a string SQL request for Chord table
    public static final String create_chord_table = "CREATE TABLE CarolChord (" +
            "id_chord         INTEGER PRIMARY KEY NOT NULL," +
            "update_time      INTEGER NOT NULL," +
            "id_song          INTEGER NOT NULL," +
            "data             TEXT NOT NULL" +
            ");";
    //make a string SQL request for Note table
    public static final String create_note_table = "CREATE TABLE CarolNote (" +
            "id_note          INTEGER PRIMARY KEY NOT NULL," +
            "update_time      INTEGER NOT NULL," +
            "id_song          INTEGER NOT NULL," +
            "data             TEXT NOT NULL" +
            ");";
    //make a string SQL request for Comment table
    public static final String create_comment_table = "CREATE TABLE CarolComment (" +
            "id_comment       INTEGER PRIMARY KEY NOT NULL," +
            "update_time      INTEGER NOT NULL," +
            "id_text          INTEGER NOT NULL," +
            "data             TEXT NOT NULL" +
            ");";
    //make a string SQL request for Comment table
    public static final String create_my_comment_table = "CREATE TABLE CarolMyComment (" +
            "id_comment       INTEGER PRIMARY KEY AUTOINCREMENT," +
            "id_text          INTEGER NOT NULL," +
            "data             TEXT NOT NULL" +
            ");";

    //make a string SQL request for Dropping all tables from ver 1
    public static final String drop_ver_1_table_song = "DROP TABLE Song";
    public static final String drop_ver_1_table_song_type = "DROP TABLE SongType;";
    public static final String drop_ver_1_table_text = "DROP TABLE Text;";
    public static final String drop_ver_1_table_chord = "DROP TABLE Chord;";
    public static final String drop_ver_1_table_note = "DROP TABLE Note;";
    public static final String drop_ver_1_table_comment = "DROP TABLE Comment;";


}
