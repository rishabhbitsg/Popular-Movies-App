package com.example.rishabh.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

/**
 * Created by rishabh on 8/5/16.
 */
public class MoviePoster implements Parcelable {
    String url;
    String title;
    String synopsis;
    String rating;
    String releaseDate;

    public MoviePoster(String url, String title, String synopsis, String rating, String releaseDate) {
        this.url = url;
        this.title = title;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    private MoviePoster(Parcel in) {
        url = in.readString();
        title = in.readString();
        synopsis = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(title);
        parcel.writeString(synopsis);
        parcel.writeString(rating);
        parcel.writeString(releaseDate);
    }

    public final static Parcelable.Creator<MoviePoster> CREATOR = new Parcelable.Creator<MoviePoster>() {
        @Override
        public MoviePoster createFromParcel(Parcel parcel) {
            return new MoviePoster(parcel);
        }

        @Override
        public MoviePoster[] newArray(int i) {
            return new MoviePoster[i];
        }
    };
}
