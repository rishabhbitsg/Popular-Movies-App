package com.example.rishabh.popularmovies;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private MoviePosterAdapter movieAdapter;
    private ArrayList<MoviePoster> movieList;
    private String category;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceStace) {
        super.onCreate(savedInstanceStace);

        // To populate the overflow options menu
        this.setHasOptionsMenu(true);

        // Restoring state on orientation changes
        if (savedInstanceStace == null || !savedInstanceStace.containsKey("movies")) {
            movieList = new ArrayList<MoviePoster>();
        }
        else {
            movieList = savedInstanceStace.getParcelableArrayList("movies");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Saving state for future restoration
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieAdapter = new MoviePosterAdapter(getActivity(), movieList);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MoviePoster movieData = movieAdapter.getItem(i);
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("movie", movieData);
                startActivity(detailIntent);
            }
        });
        return rootView;
    }


    public class FetchMovieTask extends AsyncTask<String, Void, MoviePoster[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected void onPostExecute(MoviePoster[] movies) {
            movieAdapter.clear();
            if (movies != null) {
                for (MoviePoster data : movies) {
                    movieAdapter.add(data);
                }
            }
            movieAdapter.notifyDataSetChanged();
        }

        protected MoviePoster[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            String appid = "bafc3d65522247af0dc6eb4cfa702ca1";
            String sortBy = null;
            try {

                SharedPreferences preferences = PreferenceManager.
                        getDefaultSharedPreferences(getActivity());
                sortBy = preferences.getString(getString(R.string.pref_sort_key),
                        getString(R.string.pref_sort_mostPopular));

                // Construct the URL for the TMDB query
                String FORECAST_BASE_URL = null;
                if (sortBy.equals(getString(R.string.pref_sort_mostPopular))) {
                    FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/" + "popular?";
                }
                else if (sortBy.equals(getString(R.string.pref_sort_topRated))) {
                    FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/" + "top_rated?";
                }
                else if (sortBy.equals(getString(R.string.pref_sort_favorite))) {
                    return null;
                }
                else {
                    FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/";
                    Log.v(LOG_TAG, "Invalid option for Sort By");
                }


                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, appid)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, movieJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr, 20);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }
        }
    }


    private MoviePoster[] getMovieDataFromJson(String forecastJsonStr, int numMovies)
            throws JSONException {
        final String TMDB_MOVIE_ID = "id";
        final String TMDB_RESULTS = "results";
        final String TMDB_POSTERPATH = "poster_path";
        final String TMDB_TITLE = "original_title";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_VOTEAVG = "vote_average";
        final String TMDB_RELEASEDATE = "release_date";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray movieArray = forecastJson.getJSONArray(TMDB_RESULTS);


        MoviePoster[] resultMovies = new MoviePoster[numMovies];
        for(int i = 0; i < movieArray.length(); i++) {
            String url, title, synopsis, rating, releaseDate, id;



            JSONObject movie = movieArray.getJSONObject(i);
            id = movie.getString(TMDB_MOVIE_ID);
            url = movie.getString(TMDB_POSTERPATH);
            title = movie.getString(TMDB_TITLE);
            synopsis = movie.getString(TMDB_OVERVIEW);
            rating = movie.getString(TMDB_VOTEAVG);
            releaseDate = movie.getString(TMDB_RELEASEDATE);






            resultMovies[i] = new MoviePoster(url, title, synopsis, rating, releaseDate, id);
        }
        return resultMovies;
    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String newCategory = preferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_mostPopular));
        if (category == null || !category.equals(newCategory)) {
            category = newCategory;
            new FetchMovieTask().execute();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
