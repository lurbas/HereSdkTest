package com.lucasurbas.heresdktest.ui.map;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.lucasurbas.heresdktest.R;
import com.lucasurbas.heresdktest.injection.app.ApplicationComponent;
import com.lucasurbas.heresdktest.injection.map.DaggerMapComponent;
import com.lucasurbas.heresdktest.injection.map.MapModule;
import com.lucasurbas.heresdktest.model.PlaceLink;
import com.lucasurbas.heresdktest.ui.utils.BaseActivity;
import com.lucasurbas.heresdktest.ui.widget.RxSearchView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class HereMapActivity extends BaseActivity implements MapContract.View {

    @Inject MapContract.Presenter presenter;

    @BindView(R.id.activity_map__no_permissions) View noPermissions;
    @BindView(R.id.activity_map__search_view) RxSearchView searchView;

    private List<MapObject> mapObjects = new ArrayList<>();

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
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        setupSearchView();

        if (savedInstanceState != null) {
            presenter.restoreState(savedInstanceState);
        }
        presenter.attachView(this);
        presenter.checkPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        presenter.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();
    }

    private void setupSearchView() {
        compositeSubscription.add(
                searchView.getOnQueryChangeObservable()
                        .skip(1)
                        .filter(new Func1<String, Boolean>() {
                            @Override
                            public Boolean call(String query) {
                                return query.length() >= 2;
                            }
                        })
                        .debounce(200, TimeUnit.MILLISECONDS)
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

        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_map_regular:
                        presenter.mapStyleRegularClick();
                        break;

                    case R.id.action_map_satellite:
                        presenter.mapStyleSatelliteClick();
                        break;

                    case R.id.action_map_terrain:
                        presenter.mapStyleTerrainClick();
                        break;

                    case R.id.action_map_my_location:
                        presenter.myLocationClick();
                        break;
                }
            }
        });

    }

    @OnClick(R.id.activity_map__retry_permissions)
    public void onRetryPermissionsClick() {
        presenter.retryPermissionsClicked();
    }

    @Override
    public void showPlaces(List<PlaceLink> places) {
        map.removeMapObjects(mapObjects);
        for (final PlaceLink placeLink : places) {
            Glide.with(getApplicationContext())
                    .load(placeLink.getIconUrl())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            Image image = new Image();
                            image.setBitmap(resource); // Possibly runOnUiThread()
                            MapMarker mapMarker = new MapMarker(new GeoCoordinate(placeLink.getLatitude(), placeLink.getLongitude()), image);
                            mapMarker.setAnchorPoint(new PointF(image.getWidth() / 2, image.getHeight()));
                            mapMarker.setTitle(placeLink.getBody());
                            mapMarker.setDescription(placeLink.getId());
                            mapObjects.add(mapMarker);
                            map.addMapObject(mapMarker);
                        }
                    });
        }
    }

    @Override
    public void showNoPermissionScreen(boolean show) {
        noPermissions.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAutoSuggestions(List<PlaceLink> suggestions) {
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
        // It's impossible to remove markers after rotation as there is no method to obtain all markers on map
        // a workaround is to not retain fragment
        mapFragment.setRetainInstance(false);
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment
                    map = mapFragment.getMap();
                    // show position marker
                    map.getPositionIndicator().setVisible(true);
                    MapGesture.OnGestureListener listener =
                            new MapGesture.OnGestureListener.OnGestureListenerAdapter() {
                                @Override
                                public boolean onMapObjectsSelected(List<ViewObject> objects) {
                                    for (ViewObject object : objects) {
                                        if (object.getBaseType() == ViewObject.Type.USER_OBJECT && ((MapObject) object).getType() == MapObject.Type.MARKER) {
                                            MapMarker mapMarker = (MapMarker) object;
                                            presenter.markerClicked(mapMarker.getDescription(), mapMarker.getCoordinate());
                                            return true;
                                        }
                                    }
                                    return false;
                                }

                                @Override
                                public boolean onTapEvent(PointF pointF) {
                                    for (MapObject mapObject : mapObjects) {
                                        if (mapObject.getType() == MapObject.Type.MARKER) {
                                            ((MapMarker) mapObject).hideInfoBubble();
                                        }
                                    }
                                    return super.onTapEvent(pointF);
                                }
                            };
                    mapFragment.getMapGesture().addOnGestureListener(listener);

                    presenter.mapReady();
                } else {
                    Log.v("MAP_ERROR", "Cannot initialize Map Fragment");
                }
            }
        });
    }

    @Override
    public void showMapStyle(String scheme) {
        if (map != null) {
            map.setMapScheme(scheme);
        }
    }

    @Override
    public void showMapZoom(double zoom) {
        if (map != null) {
            map.setZoomLevel(zoom);
        }
    }

    @Override
    public void animateToPosition(GeoCoordinate geoCoordinate, double zoom) {
        if (map != null) {
            map.setCenter(geoCoordinate, Map.Animation.LINEAR, zoom, -1, -1);
        }
    }

    @Override
    public void animateToPosition(GeoCoordinate geoCoordinate) {
        if (map != null) {
            map.setCenter(geoCoordinate, Map.Animation.LINEAR);
        }
    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            searchView.showProgress();
        } else {
            searchView.hideProgress();
        }
    }

    @Override
    public void showPlaceDialog(final PlaceLink place) {
        new MaterialDialog.Builder(this)
                .title(place.getBody())
                .content(R.string.activity_map__open_detail)
                .positiveText(R.string.action_open)
                .negativeText(R.string.action_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.goToPlace(place);
                    }
                })
                .show();
    }
}
