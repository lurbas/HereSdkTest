package com.lucasurbas.heresdktest.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResponse {

    @Expose
    @SerializedName("results")
    public SearchResults searchResults;

    public SearchResults getSearchResults() {
        return searchResults;
    }
}
