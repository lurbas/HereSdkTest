package com.lucasurbas.heresdktest.injection.detail;

import com.lucasurbas.heresdktest.injection.ActivityScope;
import com.lucasurbas.heresdktest.injection.app.ApplicationComponent;
import com.lucasurbas.heresdktest.ui.detail.DetailActivity;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {DetailModule.class}
)
public interface DetailComponent {

    void inject(DetailActivity activity);
}
