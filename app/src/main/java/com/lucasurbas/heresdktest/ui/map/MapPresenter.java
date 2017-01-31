package com.lucasurbas.heresdktest.ui.map;


import android.content.Context;

import com.here.android.mpa.search.Place;

import javax.inject.Inject;

public class MapPresenter implements MapContract.Presenter {

    private Context context;
    private MapContract.View view;
    private MapContract.Navigator navigator;

    @Inject
    public MapPresenter(Context context, MapContract.Navigator navigator) {
        this.context = context;
        this.navigator = navigator;
    }

    @Override
    public void attachView(MapContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void create() {
        if (view != null) {
            view.askForLocationPermission();
        }
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
        
    }

    @Override
    public void getPlaces(String query) {

    }

    @Override
    public void placeClick(Place place) {

    }
}
