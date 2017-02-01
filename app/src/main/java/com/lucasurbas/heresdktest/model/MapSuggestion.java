package com.lucasurbas.heresdktest.model;


import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.gson.annotations.Expose;

public class MapSuggestion implements SearchSuggestion {

    @Expose
    private String title;


    protected MapSuggestion(Parcel in) {
        title = in.readString();
    }

    public static final Creator<MapSuggestion> CREATOR = new Creator<MapSuggestion>() {
        @Override
        public MapSuggestion createFromParcel(Parcel in) {
            return new MapSuggestion(in);
        }

        @Override
        public MapSuggestion[] newArray(int size) {
            return new MapSuggestion[size];
        }
    };

    @Override
    public String getBody() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
    }
}
