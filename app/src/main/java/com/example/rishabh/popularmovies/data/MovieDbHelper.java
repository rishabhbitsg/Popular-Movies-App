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
            MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_IMAGE_URL + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_SYNOPSIS + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_RATING + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_FAVORITED + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_NAME_CATEGORY + " TEXT NOT NULL " +
            ");";

    private static final String SQL_CREATE_TRAILER = "CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
            MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY, " +
            MovieContract.TrailerEntry.COLUMN_NAME_MOVIE_ID + " INTEGER NOT NULL, " +
            MovieContract.TrailerEntry.COLUMN_NAME_YOUTUBE_KEY + " TEXT NOT NULL, " +
            MovieContract.TrailerEntry.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL, " +
            " FOREIGN KEY (" + MovieContract.TrailerEntry.COLUMN_NAME_MOVIE_ID + ") REFERENCES " +
            MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ") " +
            ");";

    private static final String SQL_CREATE_REVIEW = "CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " (" +
            MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY, " +
            MovieContract.ReviewEntry.COLUMN_NAME_MOVIE_ID + " INTEGER NOT NULL, " +
            MovieContract.ReviewEntry.COLUMN_NAME_AUTHOR + " TEXT NOT NULL, " +
            MovieContract.ReviewEntry.COLUMN_NAME_CONTENT + " TEXT NOT NULL, " +
            " FOREIGN KEY (" + MovieContract.ReviewEntry.COLUMN_NAME_MOVIE_ID + ") REFERENCES " +
            MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ") " +
            ");";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE);
        db.execSQL(SQL_CREATE_TRAILER);
        db.execSQL(SQL_CREATE_REVIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
