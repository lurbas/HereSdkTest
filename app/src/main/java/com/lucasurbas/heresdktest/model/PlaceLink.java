package com.lucasurbas.heresdktest.model;


import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlaceLink implements SearchSuggestion {

    @Expose
    private String id;
    @Expose
    private String title;
    @Expose
    private ArrayList<Double> position;
    @Expose
    @SerializedName("icon")
    private String iconUrl;
    @Expose
    @SerializedName("href")
    private String detailUrl;

    public PlaceLink(String id) {
        this.id = id;
    }

    protected PlaceLink(Parcel in) {
        id = in.readString();
        title = in.readString();
        position = (ArrayList<Double>) in.readSerializable();
        iconUrl = in.readString();
        detailUrl = in.readString();
    }

    public static final Creator<PlaceLink> CREATOR = new Creator<PlaceLink>() {
        @Override
        public PlaceLink createFromParcel(Parcel in) {
            return new PlaceLink(in);
        }

        @Override
        public PlaceLink[] newArray(int size) {
            return new PlaceLink[size];
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
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeSerializable(position);
        parcel.writeString(iconUrl);
        parcel.writeString(detailUrl);
    }

    public double getLatitude() {
        return position != null ? position.get(0) : null;
    }

    public double getLongitude() {
        return position != null ? position.get(1) : null;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getId() {
        return id;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceLink placeLink = (PlaceLink) o;

        return id != null ? id.equals(placeLink.id) : placeLink.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
