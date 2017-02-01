package com.lucasurbas.heresdktest.ui.detail;


import android.content.Context;

import com.lucasurbas.heresdktest.R;
import com.lucasurbas.heresdktest.api.PlacesApi;
import com.lucasurbas.heresdktest.model.PlaceDetail;
import com.lucasurbas.heresdktest.model.PlaceLink;
import com.lucasurbas.heresdktest.model.response.PlaceResponse;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DetailPresenter implements DetailContract.Presenter {

    private Context context;
    private DetailContract.View view;
    private DetailContract.Navigator navigator;
    private PlacesApi placesApi;

    private PlaceLink placeLink;
    private PlaceDetail placeDetail;

    private CompositeSubscription compositeSubscription;

    @Inject
    public DetailPresenter(Context context, DetailContract.Navigator navigator, PlacesApi placesApi) {
        this.context = context;
        this.navigator = navigator;
        this.placesApi = placesApi;

        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void attachView(DetailContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        compositeSubscription.unsubscribe();
    }

    @Override
    public void initWith(PlaceLink placeLink) {
        this.placeLink = placeLink;
        if (view != null) {
            view.showTitle(placeLink.getBody());
        }
    }

    @Override
    public void getPlaceDetail() {
        if (placeLink == null) {
            return;
        }
        if (view != null) {
            view.showLoading(true);
        }
        // get place detail from API
        compositeSubscription.add(placesApi.getPlace(placeLink.getDetailUrl())
                .map(new Func1<PlaceResponse, PlaceDetail>() {
                    @Override
                    public PlaceDetail call(PlaceResponse placeResponse) {
                        // transform api response to model easy readable by view
                        String name = placeResponse.getName();
                        String browserUrl = placeResponse.getBrowserUrl();
                        String htmlText = placeResponse.getLocation().getAddress().getHtmlText();
                        return new PlaceDetail(name, htmlText, browserUrl);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PlaceDetail>() {
                    @Override
                    public void call(PlaceDetail placeDetail) {
                        // Success
                        DetailPresenter.this.placeDetail = placeDetail;
                        if (view != null) {
                            view.showLoading(false);
                            view.showPlaceDetail(placeDetail);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // Error
                        if (view != null) {
                            view.showLoading(false);
                            view.showToast(context.getString(R.string.error__getting_results));
                        }
                    }
                }));
    }

    @Override
    public void openInBrowserClick() {
        if (placeDetail == null) {
            return;
        }
        navigator.openInBrowser(placeDetail.getBrowserUrl());
    }

    @Override
    public void backArrowClick() {
        navigator.finish();
    }
}
