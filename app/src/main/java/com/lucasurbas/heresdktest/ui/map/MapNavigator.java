package com.lucasurbas.heresdktest.ui.map;


import android.content.Context;
import android.content.Intent;

import com.lucasurbas.heresdktest.model.PlaceLink;
import com.lucasurbas.heresdktest.ui.detail.DetailActivity;

import javax.inject.Inject;

public class MapNavigator implements MapContract.Navigator {

    private Context context;

    @Inject
    public MapNavigator(Context context) {
        this.context = context;
    }

    @Override
    public void openPlaceDetail(PlaceLink place) {
        Intent intent = DetailActivity.getIntent(context, place);
        context.startActivity(intent);
    }
}
