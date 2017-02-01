package com.lucasurbas.heresdktest.injection.app;

import android.app.Application;


import com.lucasurbas.heresdktest.api.PlacesApi;
import com.lucasurbas.heresdktest.injection.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {ApplicationModule.class, NetworkModule.class}
)
public interface ApplicationComponent {

    Application getApplication();

    PlacesApi getPlacesApi();

}
