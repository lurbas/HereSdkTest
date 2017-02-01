package com.lucasurbas.heresdktest.ui.detail;


import android.content.Intent;
import android.net.Uri;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class DetailNavigator implements DetailContract.Navigator {

    private WeakReference<DetailActivity> detailActivity;

    @Inject
    public DetailNavigator(DetailActivity detailActivity) {
        this.detailActivity = new WeakReference<>(detailActivity);
    }

    @Override
    public void finish() {
        detailActivity.get().finish();
    }

    @Override
    public void openInBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        detailActivity.get().startActivity(browserIntent);
    }
}
