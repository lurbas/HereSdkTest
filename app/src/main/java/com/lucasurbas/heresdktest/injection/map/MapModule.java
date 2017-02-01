package com.lucasurbas.heresdktest.injection.map;

import android.content.Context;

import com.lucasurbas.heresdktest.injection.ActivityScope;
import com.lucasurbas.heresdktest.ui.map.HereMapActivity;
import com.lucasurbas.heresdktest.ui.map.MapContract;
import com.lucasurbas.heresdktest.ui.map.MapNavigator;
import com.lucasurbas.heresdktest.ui.map.MapPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class MapModule {

    private final HereMapActivity mapActivity;

    public MapModule(HereMapActivity mapActivity) {
        this.mapActivity = mapActivity;
    }

    @Provides
    @ActivityScope
    MapContract.Presenter providePresenter(MapPresenter presenter) {
        return presenter;
    }

    @Provides
    @ActivityScope
    MapContract.Navigator provideNavigator(MapNavigator navigator) {
        return navigator;
    }

    @Provides
    @ActivityScope
    Context provideContext() {
        return mapActivity;
    }

    @Provides
    @ActivityScope
    HereMapActivity provideMapActivity() {
        return mapActivity;
    }

}
