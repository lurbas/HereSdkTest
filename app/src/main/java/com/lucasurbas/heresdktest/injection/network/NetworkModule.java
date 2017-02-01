package com.lucasurbas.heresdktest.injection.network;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lucasurbas.heresdktest.api.ApiConstants;
import com.lucasurbas.heresdktest.api.PlacesApi;
import com.lucasurbas.heresdktest.api.PlacesApiClient;
import com.lucasurbas.heresdktest.api.PlacesApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10MB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit providePlacesRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiConstants.BASE_PLACES_ENDPOINT)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    PlacesApiService provideSuggestionsService(Retrofit retrofit) {
        return retrofit
                .create(PlacesApiService.class);
    }

    @Provides
    @Singleton
    PlacesApi providesPlacesApi(PlacesApiClient placesApiClient) {
        return placesApiClient;
    }
}
