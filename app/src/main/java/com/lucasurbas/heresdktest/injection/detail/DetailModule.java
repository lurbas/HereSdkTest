package com.lucasurbas.heresdktest.injection.detail;


import android.content.Context;

import com.lucasurbas.heresdktest.injection.ActivityScope;
import com.lucasurbas.heresdktest.ui.detail.DetailActivity;
import com.lucasurbas.heresdktest.ui.detail.DetailContract;
import com.lucasurbas.heresdktest.ui.detail.DetailNavigator;
import com.lucasurbas.heresdktest.ui.detail.DetailPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailModule {

    private final DetailActivity detailActivity;

    public DetailModule(DetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

    @Provides
    @ActivityScope
    DetailContract.Presenter providePresenter(DetailPresenter presenter) {
        return presenter;
    }

    @Provides
    @ActivityScope
    DetailContract.Navigator provideNavigator(DetailNavigator navigator) {
        return navigator;
    }

    @Provides
    @ActivityScope
    Context provideContext() {
        return detailActivity;
    }

    @Provides
    @ActivityScope
    DetailActivity provideMapActivity() {
        return detailActivity;
    }
}
