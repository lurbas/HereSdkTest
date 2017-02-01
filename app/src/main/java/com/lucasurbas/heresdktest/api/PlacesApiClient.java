package com.lucasurbas.heresdktest.api;


import com.lucasurbas.heresdktest.model.response.AutoSuggestionResponse;
import com.lucasurbas.heresdktest.model.response.PlaceResponse;
import com.lucasurbas.heresdktest.model.response.SearchResponse;

import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;

public class PlacesApiClient implements PlacesApi {

    private static final int SUGGESTIONS_SIZE = 10;
    private static final int SEARCH_SIZE = 5;
    private PlacesApiService apiService;

    @Inject
    public PlacesApiClient(PlacesApiService apiService){
        this.apiService = apiService;
    }

    @Override
    public Observable<AutoSuggestionResponse> getSuggestions(String query, double latitude, double longitude) {
        String at = getAt(latitude, longitude);
        return apiService.getSuggestions(query, at, SUGGESTIONS_SIZE, ApiConstants.HERE_APP_CODE, ApiConstants.HERE_APP_ID);
    }

    @Override
    public Observable<SearchResponse> getSearches(String query, double latitude, double longitude) {
        String at = getAt(latitude, longitude);
        return apiService.getSearches(query, at, SEARCH_SIZE, ApiConstants.HERE_APP_CODE, ApiConstants.HERE_APP_ID);
    }

    private String getAt(double latitude, double longitude) {
        return String.format(Locale.ROOT, "%.6f,%.6f", latitude, longitude);
    }

    @Override
    public Observable<PlaceResponse> getPlace(String detailUrl) {
        return apiService.getPlace(detailUrl);
    }
}
