package com.lucasurbas.heresdktest.api;

import com.lucasurbas.heresdktest.model.AutoSuggestionResponse;
import com.lucasurbas.heresdktest.model.SearchResponse;

import rx.Observable;

public interface PlacesApi {

    Observable<AutoSuggestionResponse> getSuggestions(String query, double latitude, double longitude);

    Observable<SearchResponse> getSearches(String query, double latitude, double longitude);
}
