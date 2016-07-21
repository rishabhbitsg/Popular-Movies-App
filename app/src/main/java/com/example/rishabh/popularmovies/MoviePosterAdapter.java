package com.example.rishabh.popularmovies;

import android.app.Activity;
import android.graphics.Movie;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by rishabh on 8/5/16.
 */
public class MoviePosterAdapter extends ArrayAdapter<MoviePoster> {
    private static final String LOG_TAG = MoviePosterAdapter.class.getSimpleName();
    public MoviePosterAdapter(Activity context, List<MoviePoster> moviePosters) {
        super(context, 0, moviePosters);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoviePoster moviePoster = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.grid_item_movie, parent, false);
        }
        String baseUrl = "http://image.tmdb.org/t/p/";
        String size = "w640";
        String finalUrl = baseUrl + size + moviePoster.url;
        Log.v(LOG_TAG, finalUrl);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_movie_imageview);
        Picasso.with(getContext())
                .load(finalUrl)
                .into(imageView);


        return convertView;
    }
}
