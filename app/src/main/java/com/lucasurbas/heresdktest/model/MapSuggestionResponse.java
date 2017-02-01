package com.lucasurbas.heresdktest.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapSuggestionResponse {

    @Expose @SerializedName("results")
    private List<MapSuggestion> suggestions;

    public List<MapSuggestion> getSuggestions() {
        return suggestions;
    }
}
