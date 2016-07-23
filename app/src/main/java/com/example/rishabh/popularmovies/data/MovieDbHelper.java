package com.example.rishabh.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rishabh on 2/7/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies.db";

    private static final String SQL_CREATE_MOVIE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
            MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
            MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_IMAGE_URL + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_SYNOPSIS + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_RATING + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT NOT NULL " +
            ");";


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
