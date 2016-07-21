package com.example.rishabh.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private MoviePoster movieData;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("movie")) {
            movieData = (MoviePoster) intent.getParcelableExtra("movie");
            String baseUrl = "http://image.tmdb.org/t/p/";
            String size = "w185";
            String finalUrl = baseUrl + size + movieData.url;
            ((TextView) rootView.findViewById(R.id.detail_rating)).setText(movieData.rating + "/10");
            ((TextView) rootView.findViewById(R.id.detail_synopsis)).setText(movieData.synopsis);
            ((TextView) rootView.findViewById(R.id.detail_release)).setText(movieData.releaseDate);
            ((TextView) rootView.findViewById(R.id.detail_title)).setText(movieData.title);
        }
        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
