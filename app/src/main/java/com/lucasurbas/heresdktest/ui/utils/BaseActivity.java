package com.lucasurbas.heresdktest.ui.utils;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lucasurbas.heresdktest.app.HereSdkTestApplication;
import com.lucasurbas.heresdktest.injection.app.ApplicationComponent;

import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends AppCompatActivity {

    protected CompositeSubscription compositeSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent(HereSdkTestApplication.getAppComponent(this));

        compositeSubscription = new CompositeSubscription();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    protected abstract void setupActivityComponent(ApplicationComponent applicationComponent);
}
