package com.example.rishabh.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rishabh on 2/7/16.
 */
public final class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.rishabh.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // create append path for movie table
    public static final String MOVIE_PATH = "movie";

    /* Inner class that defines "movie" table columns. */
    public static abstract class MovieEntry implements BaseColumns {
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;

        public static final String TABLE_NAME = "movie";

        // Type: INTEGER UNIQUE NOT NULL
        // Description: Movie id on TMDb.
        public static final String COLUMN_NAME_MOVIE_ID = "movie_id";

        // Type: TEXT NOT NULL
        // Description: Contains the url for movie poster.
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";

        // Type: TEXT NOT NULL
        // Description: Contains the movie title.
        public static final String COLUMN_NAME_TITLE = "title";

        // Type: TEXT NOT NULL
        // Description: Contains the movie synopsis.
        public static final String COLUMN_NAME_SYNOPSIS = "synopsis";

        // Type: REAL NOT NULL
        // Description: Contains the movie rating.
        public static final String COLUMN_NAME_RATING = "rating";

        // Type: TEXT NOT NULL
        // Description: Contains the movie release date.
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";

        // Create a content Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(MOVIE_PATH).build();

        public static Uri buildContentUriwithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
