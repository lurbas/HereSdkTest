package com.lucasurbas.heresdktest.injection.map;

import com.lucasurbas.heresdktest.injection.ActivityScope;
import com.lucasurbas.heresdktest.injection.app.ApplicationComponent;
import com.lucasurbas.heresdktest.ui.map.HereMapActivity;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {MapModule.class}
)
public interface MapComponent {

    void inject(HereMapActivity activity);
}
