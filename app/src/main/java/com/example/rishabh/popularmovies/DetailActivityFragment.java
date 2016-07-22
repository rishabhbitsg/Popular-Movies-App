package com.example.rishabh.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private MoviePoster movieData;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private ListView mTrailerListView;
    private ListView mReviewListView;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mTrailerAdapter = new TrailerAdapter(getActivity(), new ArrayList<Trailer>());
        mReviewAdapter = new ReviewAdapter(getActivity(), new ArrayList<Review>());
        if (intent != null && intent.hasExtra("movie")) {
            MoviePoster moviePoster = (MoviePoster) intent.getParcelableExtra("movie");
            new FetchTrailerTask().execute(moviePoster.id);
        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra("movie")) {
            movieData = (MoviePoster) intent.getParcelableExtra("movie");
            new FetchTrailerTask().execute(movieData.id);
            new FetchReviewTask().execute(movieData.id);

            String baseUrl = "http://image.tmdb.org/t/p/";
            String size = "w500";
            final String finalUrl = baseUrl + size + movieData.url;
            ImageView thumbnail = (ImageView) rootView.findViewById(R.id.detail_movie_thumbnail);
            Picasso.with(getActivity())
                    .load(finalUrl)
                    .into(thumbnail);
            ((TextView) rootView.findViewById(R.id.detail_rating)).setText(movieData.rating + "/10");
            ((TextView) rootView.findViewById(R.id.detail_synopsis)).setText(movieData.synopsis);
            ((TextView) rootView.findViewById(R.id.detail_release)).setText(movieData.releaseDate.substring(0, 4));
            ((TextView) rootView.findViewById(R.id.detail_title)).setText(movieData.title);



            mTrailerListView = (ListView) rootView.findViewById(R.id.detail_trailer_list);
            mTrailerListView.setAdapter(mTrailerAdapter);


            mTrailerListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Trailer trailer = mTrailerAdapter.getItem(position);
                            final String BASE_URL = "https://m.youtube.com/watch?v=";
                            String url = BASE_URL + trailer.key;
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));

                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }
                    }
            );}


        mReviewListView = (ListView) rootView.findViewById(R.id.detail_review_list);
        mReviewListView.setAdapter(mReviewAdapter);



        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }



    public class FetchTrailerTask extends AsyncTask<String, Void, Trailer[]> {

        private final String LOG_TAG = FetchTrailerTask.class.getSimpleName();

        @Override
        protected void onPostExecute(Trailer[] movies) {
            if (movies != null) {
                mTrailerAdapter.clear();
                for (Trailer data : movies) {
                    mTrailerAdapter.add(data);
                    Log.v(LOG_TAG, data.name);
                }

                mTrailerAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(mTrailerListView);
            }
        }

        protected Trailer[] doInBackground(String... params) {
            final String LOG_TAG = "getTrailer()";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String trailerJsonStr = null;
            String appid = "bafc3d65522247af0dc6eb4cfa702ca1";
            String id = params[0];
            try {
                // Construct the URL for the TMDB query
                String FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/" + id + "/videos";

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
                trailerJsonStr = buffer.toString();
                Log.v(LOG_TAG, trailerJsonStr);
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
                final String TRAILER_RESULTS = "results";
                final String TRAILER_KEY = "key";
                final String TRAILER_NAME = "name";

                JSONObject trailerObject = new JSONObject(trailerJsonStr);
                JSONArray trailerArray = trailerObject.getJSONArray(TRAILER_RESULTS);

                Trailer[] trailers = new Trailer[trailerArray.length()];


                for (int i = 0; i < trailerArray.length(); i++) {
                    String key, name;

                    JSONObject trailer = trailerArray.getJSONObject(i);
                    name = trailer.getString(TRAILER_NAME);
                    key = trailer.getString(TRAILER_KEY);

                    trailers[i] = new Trailer(name, key);
                }

                return trailers;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }
        }
    }

    public class FetchReviewTask extends AsyncTask<String, Void, Review[]> {

        private final String LOG_TAG = FetchReviewTask.class.getSimpleName();

        @Override
        protected void onPostExecute(Review[] movies) {
            if (movies != null) {
                mReviewAdapter.clear();
                for (Review data : movies) {
                    mReviewAdapter.add(data);
                }
                mReviewAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(mReviewListView);
            }
        }

        protected Review[] doInBackground(String... params) {
            final String LOG_TAG = "getReviews()";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String reviewJsonStr = null;
            String appid = "bafc3d65522247af0dc6eb4cfa702ca1";
            String id = params[0];
            try {
                // Construct the URL for the TMDB query
                String FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/" + id + "/reviews";

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
                reviewJsonStr = buffer.toString();
                Log.v(LOG_TAG, reviewJsonStr);
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
                final String REVIEW_RESULTS = "results";
                final String REVIEW_AUTHOR = "author";
                final String REVIEW_CONTENT = "content";

                JSONObject reviewObject = new JSONObject(reviewJsonStr);
                JSONArray reviewArray = reviewObject.getJSONArray(REVIEW_RESULTS);

                Review[] reviews = new Review[Math.min(10, reviewArray.length())];



                for (int i = 0; i < Math.min(10, reviewArray.length()); i++) {
                    String author, content;

                    JSONObject review = reviewArray.getJSONObject(i);
                    author = review.getString(REVIEW_AUTHOR);
                    content = review.getString(REVIEW_CONTENT);

                    reviews[i] = new Review(author, content);
                }

                return reviews;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }
        }
    }
}
