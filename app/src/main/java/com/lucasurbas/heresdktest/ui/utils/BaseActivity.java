package com.lucasurbas.heresdktest.ui.utils;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lucasurbas.heresdktest.app.HereSdkTestApplication;
import com.lucasurbas.heresdktest.injection.app.ApplicationComponent;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent(HereSdkTestApplication.getAppComponent(this));
    }

    protected abstract void setupActivityComponent(ApplicationComponent applicationComponent);
}
