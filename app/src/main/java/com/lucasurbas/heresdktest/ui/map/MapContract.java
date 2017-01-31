package com.lucasurbas.heresdktest.ui.map;

import android.os.Bundle;

import com.here.android.mpa.search.Place;
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

        void showToast(String message);

        void askForLocationPermission();

        void initMap();
    }

    interface Presenter extends BasePresenter<View> {

        Bundle saveState();

        void restoreState(Bundle bundle);

        void create();

        void retryPermissionsClicked();

        void permissionResult(boolean granted);

        void placeClick(Place place);

    }
}
