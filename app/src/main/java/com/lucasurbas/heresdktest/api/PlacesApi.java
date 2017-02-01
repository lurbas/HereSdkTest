package com.lucasurbas.heresdktest.api;

import com.lucasurbas.heresdktest.model.response.AutoSuggestionResponse;
import com.lucasurbas.heresdktest.model.response.PlaceResponse;
import com.lucasurbas.heresdktest.model.response.SearchResponse;

import rx.Observable;

public interface PlacesApi {

    // All public methods from Places API

    Observable<AutoSuggestionResponse> getSuggestions(String query, double latitude, double longitude);

    Observable<SearchResponse> getSearches(String query, double latitude, double longitude);

    Observable<PlaceResponse> getPlace(String detailUrl);
}
