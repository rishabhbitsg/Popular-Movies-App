package com.example.rishabh.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rishabh on 2/7/16.
 */
public final class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.rishabh.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /* Inner class that defines "movie" table columns. */
    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";

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

        // Type: INTEGER DEFAULT 0
        // Description: Has the user marked the movie as favorite.
        public static final String COLUMN_NAME_FAVORITED = "favorited";

        // Type: TEXT NOT NULL
        // Description: Category of the movie
        public static final String COLUMN_NAME_CATEGORY = "category";
    }

    /* Inner class that defines "trailer" table columns */
    public static abstract class TrailerEntry implements BaseColumns {
        public static final String TABLE_NAME = "trailer";

        // Type: INTEGER NOT NULL
        // Description: Foreign key from "movie" table
        public static final String COLUMN_NAME_MOVIE_ID = "movie_id";

        // Type: TEXT NOT NULL
        // Description: Key for youtube video.
        public static final String COLUMN_NAME_YOUTUBE_KEY = "youtube_key";

        // Type: TEXT NOT NULL
        // Decription: Trailer description.
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    /* Inner class that defines "review" table columns */
    public static abstract class  ReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "review";

        // Type: INTEGER NOT NULL
        // Description: Foreign key from "movie" table
        public static final String COLUMN_NAME_MOVIE_ID = "movie_id";

        // Type: TEXT NOT NULL
        // Description: Author of the review.
        public static final String COLUMN_NAME_AUTHOR = "author";

        // Type: TEXT NOT NULL
        // Description: Content of the review.
        public static final String COLUMN_NAME_CONTENT = "content";
    }
}
