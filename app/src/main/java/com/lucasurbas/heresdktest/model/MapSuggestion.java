package com.lucasurbas.heresdktest.model;


import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class MapSuggestion implements SearchSuggestion {

    private String body;

    public MapSuggestion(String body) {
        this.body = body;
    }

    protected MapSuggestion(Parcel in) {
        body = in.readString();
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
        return body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(body);
    }
}
