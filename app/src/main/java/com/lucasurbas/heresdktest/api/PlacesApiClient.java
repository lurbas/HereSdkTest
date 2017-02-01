package com.lucasurbas.heresdktest.api;


import com.lucasurbas.heresdktest.model.MapSuggestionResponse;

import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;

public class PlacesApiClient implements PlacesApi {

    private static final int SIZE = 10;
    private PlacesApiService apiService;

    @Inject
    public PlacesApiClient(PlacesApiService apiService){
        this.apiService = apiService;
    }

    @Override
    public Observable<MapSuggestionResponse> getSuggestions(String query, double latitude, double longitude) {
        String at = getAt(latitude, longitude);
        return apiService.getSuggestions(query, at, SIZE, ApiConstants.HERE_APP_CODE, ApiConstants.HERE_APP_ID);
    }

    private String getAt(double latitude, double longitude) {
        return String.format(Locale.ROOT, "%.6f,%.6f", latitude, longitude);
    }
}
