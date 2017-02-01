package com.lucasurbas.heresdktest.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lucasurbas.heresdktest.R;
import com.lucasurbas.heresdktest.injection.app.ApplicationComponent;
import com.lucasurbas.heresdktest.injection.detail.DaggerDetailComponent;
import com.lucasurbas.heresdktest.injection.detail.DetailModule;
import com.lucasurbas.heresdktest.model.PlaceLink;
import com.lucasurbas.heresdktest.ui.utils.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class DetailActivity extends BaseActivity implements DetailContract.View{

    private static final String KEY_PLACE_LINK = "key_place_link";

    @Inject DetailContract.Presenter presenter;

    public static Intent getIntent(Context context, PlaceLink placeLink){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_PLACE_LINK, placeLink);
        return intent;
    }

    @Override
    protected void setupActivityComponent(ApplicationComponent applicationComponent) {
        DaggerDetailComponent.builder()
                .applicationComponent(applicationComponent)
                .detailModule(new DetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        PlaceLink placeLink = getIntent().getParcelableExtra(KEY_PLACE_LINK);
        presenter.initWith(placeLink);
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showPlaceLink(PlaceLink place) {

    }

    @Override
    public void showPlaceDetail(PlaceLink place) {

    }
}
