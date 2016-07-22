package com.example.rishabh.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rishabh on 22/7/16.
 */
public class Review implements Parcelable {
    String author;
    String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }

    private Review(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public final static Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };
}
