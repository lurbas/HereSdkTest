package com.lucasurbas.heresdktest.ui.utils;

public interface BasePresenter<V extends BaseView> {

    void attachView(V view);

    void detachView();
}
