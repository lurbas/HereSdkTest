package com.lucasurbas.heresdktest.ui.map;


import android.content.Context;

import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.search.Place;
import com.lucasurbas.heresdktest.R;
import com.lucasurbas.heresdktest.api.PlacesApi;
import com.lucasurbas.heresdktest.model.AutoSuggestionResponse;
import com.lucasurbas.heresdktest.model.PlaceLink;
import com.lucasurbas.heresdktest.model.SearchResponse;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MapPresenter implements MapContract.Presenter {

    private static final double DEFAULT_MAP_ZOOM = 13;
    private static final double MY_LOCATION_MAP_ZOOM = 16;

    private Context context;
    private MapContract.View view;
    private MapContract.Navigator navigator;
    private GeoPosition lastPosition;
    private PlacesApi placesApi;

    private CompositeSubscription compositeSubscription;
    private Subscription autoSuggestionSubscription;

    // Define positioning listener
    private PositioningManager.OnPositionChangedListener positionListener = new
            PositioningManager.OnPositionChangedListener() {

                public void onPositionUpdated(PositioningManager.LocationMethod method,
                                              GeoPosition position, boolean isMapMatched) {
                    MapPresenter.this.lastPosition = position;
                }

                public void onPositionFixChanged(PositioningManager.LocationMethod method,
                                                 PositioningManager.LocationStatus status) {
                }
            };

    @Inject
    public MapPresenter(Context context, MapContract.Navigator navigator, PlacesApi placesApi) {
        this.context = context;
        this.navigator = navigator;
        this.placesApi = placesApi;

        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void attachView(MapContract.View view) {
        this.view = view;
        if (view != null) {
            view.askForLocationPermission();
        }
    }

    @Override
    public void detachView() {
        this.view = null;
        if (PositioningManager.getInstance() != null) {
            PositioningManager.getInstance().removeListener(positionListener);
        }
        compositeSubscription.unsubscribe();
        if (autoSuggestionSubscription != null && !autoSuggestionSubscription.isUnsubscribed()) {
            autoSuggestionSubscription.unsubscribe();
        }
    }

    @Override
    public void start() {
        if (PositioningManager.getInstance() != null) {
            PositioningManager.getInstance().start(PositioningManager.LocationMethod.GPS_NETWORK);
        }
    }

    @Override
    public void stop() {
        if (PositioningManager.getInstance() != null) {
            PositioningManager.getInstance().stop();
        }
    }

    @Override
    public void mapReady() {
        PositioningManager.getInstance().addListener(new WeakReference<>(positionListener));
        PositioningManager.getInstance().start(PositioningManager.LocationMethod.GPS_NETWORK);
        if (view != null) {
            view.showMapZoom(DEFAULT_MAP_ZOOM);
        }
    }

    @Override
    public void retryPermissionsClicked() {
        if (view != null) {
            view.askForLocationPermission();
        }
    }

    @Override
    public void permissionResult(boolean granted) {
        if (granted && view != null) {
            view.showNoPermissionScreen(false);
            view.initMap();
        } else if (view != null) {
            view.showNoPermissionScreen(true);
        }
    }

    @Override
    public void getAutoSuggestions(String query) {
        if (lastPosition != null) {
            if (autoSuggestionSubscription != null && !autoSuggestionSubscription.isUnsubscribed()) {
                autoSuggestionSubscription.unsubscribe();
            }
            autoSuggestionSubscription = placesApi.getSuggestions(query, lastPosition.getCoordinate().getLatitude(), lastPosition.getCoordinate().getLongitude())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(new Func1<AutoSuggestionResponse, Boolean>() {
                        @Override
                        public Boolean call(AutoSuggestionResponse autoSuggestionResponse) {
                            // filter empty results
                            return autoSuggestionResponse.getSuggestions() != null && !autoSuggestionResponse.getSuggestions().isEmpty();
                        }
                    })
                    .map(new Func1<AutoSuggestionResponse, List<PlaceLink>>() {
                        @Override
                        public List<PlaceLink> call(AutoSuggestionResponse autoSuggestionResponse) {
                            return autoSuggestionResponse.getSuggestions();
                        }
                    })
                    .subscribe(new Action1<List<PlaceLink>>() {
                                   @Override
                                   public void call(List<PlaceLink> mapSuggestions) {
                                       if (view != null) {
                                           view.showAutoSuggestions(mapSuggestions);
                                       }
                                   }
                               }, new Action1<Throwable>() {
                                   @Override
                                   public void call(Throwable throwable) {
                                       // empty
                                   }
                               }
                    );
        }
    }

    @Override
    public void getPlaces(String query) {
        if (lastPosition != null) {
            if (view != null) {
                view.showLoading(true);
            }
            if (autoSuggestionSubscription != null && !autoSuggestionSubscription.isUnsubscribed()) {
                autoSuggestionSubscription.unsubscribe();
            }
            compositeSubscription.add(placesApi.getSearches(query, lastPosition.getCoordinate().getLatitude(), lastPosition.getCoordinate().getLongitude())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<SearchResponse>() {
                        @Override
                        public void call(SearchResponse searchResponse) {
                            if (searchResponse.getSearchResults() != null && searchResponse.getSearchResults().getPlaces() != null && !searchResponse.getSearchResults().getPlaces().isEmpty()) {
                                if (view != null) {
                                    view.showLoading(false);
                                    view.showPlaces(searchResponse.getSearchResults().getPlaces());
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (view != null) {
                                view.showLoading(false);
                                view.showToast(context.getString(R.string.error__getting_results));
                                view.showToast(throwable.getMessage());
                            }
                        }
                    }));
        } else if (view != null) {
            view.showToast(context.getString(R.string.error__no_location));
        }
    }

    @Override
    public void placeClick(Place place) {

    }

    @Override
    public void mapStyleRegularClick() {
        if (view != null) {
            view.showMapStyle(Map.Scheme.NORMAL_DAY);
        }
    }

    @Override
    public void mapStyleSatelliteClick() {
        if (view != null) {
            view.showMapStyle(Map.Scheme.SATELLITE_DAY);
        }
    }

    @Override
    public void mapStyleTerrainClick() {
        if (view != null) {
            view.showMapStyle(Map.Scheme.TERRAIN_DAY);
        }
    }

    @Override
    public void myLocationClick() {
        if (lastPosition != null && view != null) {
            view.animateToPosition(lastPosition.getCoordinate(), MY_LOCATION_MAP_ZOOM);
        }
    }
}
