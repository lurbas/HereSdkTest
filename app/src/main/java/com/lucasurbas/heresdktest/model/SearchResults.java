package com.lucasurbas.heresdktest.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResults {

    @Expose
    @SerializedName("items")
    public List<PlaceLink> places;

    public List<PlaceLink> getPlaces() {
        return places;
    }
}
