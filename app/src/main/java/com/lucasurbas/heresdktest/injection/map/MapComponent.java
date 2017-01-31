package com.lucasurbas.heresdktest.injection.map;

import com.here.android.mpa.common.MapActivity;
import com.lucasurbas.heresdktest.injection.ActivityScope;
import com.lucasurbas.heresdktest.injection.app.ApplicationComponent;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {MapModule.class}
)
public interface MapComponent {

    void inject(MapActivity activity);
}
