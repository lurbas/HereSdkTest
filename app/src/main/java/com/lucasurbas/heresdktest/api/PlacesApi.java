package com.lucasurbas.heresdktest.api;

import com.lucasurbas.heresdktest.model.MapSuggestionResponse;

import rx.Observable;

public interface PlacesApi {

    Observable<MapSuggestionResponse> getSuggestions(String query, double latitude, double longitude);
}
