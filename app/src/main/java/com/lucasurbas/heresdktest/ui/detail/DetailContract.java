package com.lucasurbas.heresdktest.ui.detail;


import com.lucasurbas.heresdktest.model.PlaceDetail;
import com.lucasurbas.heresdktest.model.PlaceLink;
import com.lucasurbas.heresdktest.ui.utils.BaseNavigator;
import com.lucasurbas.heresdktest.ui.utils.BasePresenter;
import com.lucasurbas.heresdktest.ui.utils.BaseView;

public interface DetailContract {

    // All public methods in Detail module

    interface Navigator extends BaseNavigator {

        void finish();

    }

    interface View extends BaseView {

        void showPlaceDetail(PlaceDetail place);

        void showTitle(String title);

        void showLoading(boolean show);

        void showToast(String message);
    }

    interface Presenter extends BasePresenter<DetailContract.View> {

        void initWith(PlaceLink placeLink);

        void getPlaceDetail();
    }
}
