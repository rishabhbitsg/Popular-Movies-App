package com.example.rishabh.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rishabh on 22/7/16.
 */
public class Trailer implements Parcelable {
    String name;
    String key;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
    }

    private Trailer(Parcel in) {
        name = in.readString();
        key = in.readString();
    }
    public Trailer(String name, String key) {
        this.name = name;
        this.key  = key;
    }

    public final static Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }
    };
}
