package com.example.rishabh.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rishabh on 22/7/16.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {
    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.detail_review_list_item, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.detail_review_author)).setText(review.author);
        ((TextView) convertView.findViewById(R.id.detail_review_content)).setText(review.content);

        return convertView;
    }
}
