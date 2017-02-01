package com.lucasurbas.heresdktest.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoSuggestionResponse {

    @Expose
    @SerializedName("results")
    private List<PlaceLink> autoSuggestions;

    public List<PlaceLink> getSuggestions() {
        return autoSuggestions;
    }
}
