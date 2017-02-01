package com.lucasurbas.heresdktest.ui.map;

import android.os.Bundle;

import com.here.android.mpa.common.GeoCoordinate;
import com.lucasurbas.heresdktest.model.PlaceLink;
import com.lucasurbas.heresdktest.ui.utils.BaseNavigator;
import com.lucasurbas.heresdktest.ui.utils.BasePresenter;
import com.lucasurbas.heresdktest.ui.utils.BaseView;

import java.util.List;

public interface MapContract {

    // All public methods in Map module

    interface Navigator extends BaseNavigator {

        void openPlaceDetail(PlaceLink place);

    }

    interface View extends BaseView {

        void showPlaces(List<PlaceLink> places);

        void showNoPermissionScreen(boolean show);

        void showAutoSuggestions(List<PlaceLink> suggestions);

        void showToast(String message);

        void askForLocationPermission();

        void initMap();

        void showMapStyle(String normalDay);

        void showMapZoom(double zoom);

        void animateToPosition(GeoCoordinate geoCoordinate, double zoom);

        void animateToPosition(GeoCoordinate geoCoordinate);

        void showLoading(boolean show);

        void showPlaceDialog(PlaceLink place);
    }

    interface Presenter extends BasePresenter<View> {

        void saveState(Bundle outState);

        void restoreState(Bundle savedInstanceState);

        void checkPermissions();

        void start();

        void stop();

        void mapReady();

        void retryPermissionsClicked();

        void permissionResult(boolean granted);

        void getAutoSuggestions(String query);

        void getPlaces(String query);

        void goToPlace(PlaceLink place);

        void mapStyleRegularClick();

        void mapStyleSatelliteClick();

        void mapStyleTerrainClick();

        void myLocationClick();

        void markerClicked(String placeId, GeoCoordinate coordinate);
    }
}
