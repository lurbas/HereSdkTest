package com.lucasurbas.heresdktest.api;


import com.lucasurbas.heresdktest.model.response.AutoSuggestionResponse;
import com.lucasurbas.heresdktest.model.response.PlaceResponse;
import com.lucasurbas.heresdktest.model.response.SearchResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface PlacesApiService {

    @GET("places/v1/autosuggest")
    Observable<AutoSuggestionResponse> getSuggestions(
            @Query(ApiConstants.PARAM_QUERY) String query,
            @Query(ApiConstants.PARAM_AT) String at,
            @Query(ApiConstants.PARAM_SIZE) int size,
            @Query(ApiConstants.PARAM_APP_CODE) String appCode,
            @Query(ApiConstants.PARAM_APP_ID) String appId);

    @GET("places/v1/discover/search")
    Observable<SearchResponse> getSearches(
            @Query(ApiConstants.PARAM_QUERY) String query,
            @Query(ApiConstants.PARAM_AT) String at,
            @Query(ApiConstants.PARAM_SIZE) int size,
            @Query(ApiConstants.PARAM_APP_CODE) String appCode,
            @Query(ApiConstants.PARAM_APP_ID) String appId);

    @GET
    Observable<PlaceResponse> getPlace(@Url String href);
}
