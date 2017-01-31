package com.lucasurbas.heresdktest.app;

import android.app.Application;
import android.content.Context;

import com.lucasurbas.heresdktest.injection.app.ApplicationComponent;
import com.lucasurbas.heresdktest.injection.app.ApplicationModule;
import com.lucasurbas.heresdktest.injection.app.DaggerApplicationComponent;


public class HereSdkTestApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dependency graph is build using Dagger2
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getAppComponent(Context context) {
        return ((HereSdkTestApplication) context.getApplicationContext()).applicationComponent;
    }
}
