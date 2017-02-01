package com.lucasurbas.heresdktest.ui.map;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.Place;
import com.lucasurbas.heresdktest.model.PlaceLink;
import com.lucasurbas.heresdktest.ui.utils.BaseNavigator;
import com.lucasurbas.heresdktest.ui.utils.BasePresenter;
import com.lucasurbas.heresdktest.ui.utils.BaseView;

import java.util.List;

public interface MapContract {

    // All public methods in Map module

    interface Navigator extends BaseNavigator {

        void openPlaceDetail(String itemId);

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

        void showLoading(boolean show);
    }

    interface Presenter extends BasePresenter<View> {

        void start();

        void stop();

        void mapReady();

        void retryPermissionsClicked();

        void permissionResult(boolean granted);

        void getAutoSuggestions(String query);

        void getPlaces(String query);

        void placeClick(Place place);

        void mapStyleRegularClick();

        void mapStyleSatelliteClick();

        void mapStyleTerrainClick();

        void myLocationClick();
    }
}
