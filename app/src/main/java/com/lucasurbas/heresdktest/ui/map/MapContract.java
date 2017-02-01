package com.lucasurbas.heresdktest.ui.map;

import com.here.android.mpa.search.Place;
import com.lucasurbas.heresdktest.model.MapSuggestion;
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

        void showPlaces(List<Place> places);

        void showNoPermissionScreen(boolean show);

        void showAutoSuggestions(List<MapSuggestion> suggestions);

        void showToast(String message);

        void askForLocationPermission();

        void initMap();
    }

    interface Presenter extends BasePresenter<View> {

        void create();

        void start();

        void stop();

        void mapReady();

        void retryPermissionsClicked();

        void permissionResult(boolean granted);

        void getAutoSuggestions(String query);

        void getPlaces(String query);

        void placeClick(Place place);

    }
}
