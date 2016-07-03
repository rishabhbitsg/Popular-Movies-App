package com.example.rishabh.popularmovies.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by rishabh on 3/7/16.
 */
public class TestDb extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    @Override
    protected void tearDown() throws Exception {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    public void testCreateDb() throws Throwable {
        // Create a HashSet of all the table names we wish to look for.
        // Note that there will be another table in the DB that stores
        // the Android metadata (db version information).

        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.TrailerEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.ReviewEntry.TABLE_NAME);

        SQLiteDatabase db = new MovieDbHelper(mContext).
                                    getReadableDatabase();
        assertTrue(db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly.", c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error: Tables have not been created correctly.", tableNameHashSet.isEmpty());

        // verify that the tables have correct columns
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")", null);
        assertTrue("Error: Unable to query movie table for table info.", c.moveToFirst());


        HashSet<String> columnNameHashSet = new HashSet<>();
        columnNameHashSet.add(MovieContract.MovieEntry._ID);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_NAME_IMAGE_URL);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_NAME_TITLE);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_NAME_SYNOPSIS);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_NAME_RATING);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_NAME_FAVORITED);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_NAME_CATEGORY);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnNameHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: Column names for movie table were not defined properly.", columnNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + MovieContract.TrailerEntry.TABLE_NAME + ")", null);
        assertTrue("Error: Unable to query trailer table for table info.", c.moveToFirst());

        columnNameHashSet = new HashSet<>();
        columnNameHashSet.add(MovieContract.TrailerEntry._ID);
        columnNameHashSet.add(MovieContract.TrailerEntry.COLUMN_NAME_MOVIE_ID);
        columnNameHashSet.add(MovieContract.TrailerEntry.COLUMN_NAME_YOUTUBE_KEY);
        columnNameHashSet.add(MovieContract.TrailerEntry.COLUMN_NAME_DESCRIPTION);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnNameHashSet.remove(columnName);
        } while(c.moveToNext());

        assertTrue("Error: Column names for trailer table were not defined properly.", columnNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + MovieContract.ReviewEntry.TABLE_NAME + ")", null);
        assertTrue("Error: Unable to query review table for table info.", c.moveToFirst());

        columnNameHashSet = new HashSet<>();
        columnNameHashSet.add(MovieContract.ReviewEntry._ID);
        columnNameHashSet.add(MovieContract.ReviewEntry.COLUMN_NAME_MOVIE_ID);
        columnNameHashSet.add(MovieContract.ReviewEntry.COLUMN_NAME_AUTHOR);
        columnNameHashSet.add(MovieContract.ReviewEntry.COLUMN_NAME_CONTENT);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnNameHashSet.remove(columnName);
        } while(c.moveToNext());

        assertTrue("Error: Column names for review table were not defined properly.", columnNameHashSet.isEmpty());

        db.close();
    }
}
