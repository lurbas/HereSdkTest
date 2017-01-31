package com.lucasurbas.heresdktest.ui.map;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.search.Place;
import com.lucasurbas.heresdktest.R;
import com.lucasurbas.heresdktest.injection.app.ApplicationComponent;
import com.lucasurbas.heresdktest.injection.map.DaggerMapComponent;
import com.lucasurbas.heresdktest.injection.map.MapModule;
import com.lucasurbas.heresdktest.model.MapSuggestion;
import com.lucasurbas.heresdktest.ui.utils.BaseActivity;
import com.lucasurbas.heresdktest.ui.widget.RxSearchView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class HereMapActivity extends BaseActivity implements MapContract.View {

    @Inject MapContract.Presenter presenter;

    @BindView(R.id.activity_map__no_permissions) View noPermissions;
    @BindView(R.id.activity_map__search_view) RxSearchView searchView;

    // map embedded in the map fragment
    private Map map;

    @Override
    protected void setupActivityComponent(ApplicationComponent applicationComponent) {
        DaggerMapComponent.builder()
                .applicationComponent(applicationComponent)
                .mapModule(new MapModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupSearchView();

        presenter.attachView(this);
        presenter.create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void setupSearchView() {
        compositeSubscription.add(
                searchView.getOnQueryChangeObservable()
                        .skip(1)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String query) {
                                presenter.getAutoSuggestions(query);
                            }
                        }));

        compositeSubscription.add(
                searchView.getOnSearchObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String query) {
                                presenter.getPlaces(query);
                            }
                        }));

    }

    @OnClick(R.id.activity_map__retry_permissions)
    public void onRetryPermissionsClick() {
        presenter.retryPermissionsClicked();
    }

    @Override
    public void showPlaces(List<Place> places) {

    }

    @Override
    public void showNoPermissionScreen(boolean show) {
        noPermissions.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAutoSuggestions(List<MapSuggestion> suggestions) {
        searchView.swapSuggestions(suggestions);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void askForLocationPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        presenter.permissionResult(granted);
                    }
                });
    }

    @Override
    public void initMap() {
        // Search for the map fragment to finish setup by calling init().
        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.activity_map__fragment_map);
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment
                    map = mapFragment.getMap();
                    // Set the zoom level to the average between min and max
                    map.setZoomLevel(
                            (map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                } else {
                    Log.v("MAP_ERROR", "Cannot initialize Map Fragment");
                }
            }
        });
    }
}
