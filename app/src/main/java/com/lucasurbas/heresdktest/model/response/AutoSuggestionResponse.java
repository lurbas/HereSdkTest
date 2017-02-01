package com.lucasurbas.heresdktest.model.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lucasurbas.heresdktest.model.PlaceLink;

import java.util.List;

public class AutoSuggestionResponse {

    @Expose
    @SerializedName("results")
    private List<PlaceLink> autoSuggestions;

    public List<PlaceLink> getSuggestions() {
        return autoSuggestions;
    }
}
