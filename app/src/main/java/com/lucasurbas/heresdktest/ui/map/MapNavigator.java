package com.lucasurbas.heresdktest.ui.map;


import android.content.Intent;

import com.lucasurbas.heresdktest.model.PlaceLink;
import com.lucasurbas.heresdktest.ui.detail.DetailActivity;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class MapNavigator implements MapContract.Navigator {

    private WeakReference<HereMapActivity> mapActivity;

    @Inject
    public MapNavigator(HereMapActivity mapActivity) {
        this.mapActivity = new WeakReference<>(mapActivity);
    }

    @Override
    public void openPlaceDetail(PlaceLink place) {
        Intent intent = DetailActivity.getIntent(mapActivity.get(), place);
        mapActivity.get().startActivity(intent);
    }
}
