package com.lucasurbas.heresdktest.ui.detail;


import com.lucasurbas.heresdktest.model.PlaceLink;

import javax.inject.Inject;

public class DetailPresenter implements DetailContract.Presenter {

    private DetailContract.View view;

    @Inject
    public DetailPresenter() {

    }

    @Override
    public void attachView(DetailContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void initWith(PlaceLink placeLink) {

    }

    @Override
    public void getPlaceDetail() {

    }
}
