package com.lucasurbas.heresdktest.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lucasurbas.heresdktest.R;
import com.lucasurbas.heresdktest.injection.app.ApplicationComponent;
import com.lucasurbas.heresdktest.injection.detail.DaggerDetailComponent;
import com.lucasurbas.heresdktest.injection.detail.DetailModule;
import com.lucasurbas.heresdktest.model.PlaceDetail;
import com.lucasurbas.heresdktest.model.PlaceLink;
import com.lucasurbas.heresdktest.ui.utils.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends BaseActivity implements DetailContract.View {

    private static final String KEY_PLACE_LINK = "key_place_link";

    @Inject DetailContract.Presenter presenter;

    @BindView(R.id.activity_detail__progress_bar) ProgressBar progressBar;
    @BindView(R.id.activity_detail__details) View details;
    @BindView(R.id.activity_detail__name_content) TextView name;
    @BindView(R.id.activity_detail__address_content) TextView address;

    public static Intent getIntent(Context context, PlaceLink placeLink) {
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
        presenter.attachView(this);
        presenter.initWith(placeLink);
        presenter.getPlaceDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        details.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPlaceDetail(PlaceDetail place) {
        details.setVisibility(View.VISIBLE);
        name.setText(place.getName());
        address.setText(Html.fromHtml(place.getAddress()));
    }
}
