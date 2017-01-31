package com.lucasurbas.heresdktest.injection.app;

import android.app.Application;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {ApplicationModule.class}
)
public interface ApplicationComponent {

    Application getApplication();

}
