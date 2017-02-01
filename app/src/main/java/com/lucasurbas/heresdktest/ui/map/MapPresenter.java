package com.lucasurbas.heresdktest.ui.map;


import android.content.Context;

import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.search.Place;
import com.lucasurbas.heresdktest.api.PlacesApi;
import com.lucasurbas.heresdktest.model.MapSuggestion;
import com.lucasurbas.heresdktest.model.MapSuggestionResponse;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MapPresenter implements MapContract.Presenter {

    private Context context;
    private MapContract.View view;
    private MapContract.Navigator navigator;
    private GeoPosition lastPosition;
    private PlacesApi placesApi;

    // Define positioning listener
    private PositioningManager.OnPositionChangedListener positionListener = new
            PositioningManager.OnPositionChangedListener() {

                public void onPositionUpdated(PositioningManager.LocationMethod method,
                                              GeoPosition position, boolean isMapMatched) {
                    MapPresenter.this.lastPosition = position;
                }

                public void onPositionFixChanged(PositioningManager.LocationMethod method,
                                                 PositioningManager.LocationStatus status) {
                }
            };

    @Inject
    public MapPresenter(Context context, MapContract.Navigator navigator, PlacesApi placesApi) {
        this.context = context;
        this.navigator = navigator;
        this.placesApi = placesApi;
    }

    @Override
    public void attachView(MapContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (PositioningManager.getInstance() != null) {
            PositioningManager.getInstance().removeListener(positionListener);
        }
    }

    @Override
    public void create() {
        if (view != null) {
            view.askForLocationPermission();
        }
    }

    @Override
    public void start() {
        if (PositioningManager.getInstance() != null) {
            PositioningManager.getInstance().start(PositioningManager.LocationMethod.GPS_NETWORK);
        }
    }

    @Override
    public void stop() {
        if (PositioningManager.getInstance() != null) {
            PositioningManager.getInstance().stop();
        }
    }

    @Override
    public void mapReady() {
        PositioningManager.getInstance().addListener(new WeakReference<>(positionListener));
        PositioningManager.getInstance().start(PositioningManager.LocationMethod.GPS_NETWORK);
    }

    @Override
    public void retryPermissionsClicked() {
        if (view != null) {
            view.askForLocationPermission();
        }
    }

    @Override
    public void permissionResult(boolean granted) {
        if (granted && view != null) {
            view.showNoPermissionScreen(false);
            view.initMap();
        } else if (view != null) {
            view.showNoPermissionScreen(true);
        }
    }

    @Override
    public void getAutoSuggestions(String query) {
        if (lastPosition != null) {
            placesApi.getSuggestions(query, lastPosition.getCoordinate().getLatitude(), lastPosition.getCoordinate().getLongitude())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(new Func1<MapSuggestionResponse, Boolean>() {
                        @Override
                        public Boolean call(MapSuggestionResponse mapSuggestionResponse) {
                            // filter empty results
                            return mapSuggestionResponse.getSuggestions() != null && !mapSuggestionResponse.getSuggestions().isEmpty();
                        }
                    })
                    .map(new Func1<MapSuggestionResponse, List<MapSuggestion>>() {
                        @Override
                        public List<MapSuggestion> call(MapSuggestionResponse mapSuggestionResponse) {
                            return mapSuggestionResponse.getSuggestions();
                        }
                    })
                    .subscribe(new Action1<List<MapSuggestion>>() {
                        @Override
                        public void call(List<MapSuggestion> mapSuggestions) {
                            if (view != null) {
                                view.showAutoSuggestions(mapSuggestions);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            // empty
                        }
                    });
        }
    }

    @Override
    public void getPlaces(String query) {

    }

    @Override
    public void placeClick(Place place) {

    }
}
