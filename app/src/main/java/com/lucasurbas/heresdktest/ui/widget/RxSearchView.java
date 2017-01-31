package com.lucasurbas.heresdktest.ui.widget;


import android.content.Context;
import android.util.AttributeSet;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import rx.Observable;
import rx.subjects.PublishSubject;


public class RxSearchView extends FloatingSearchView{

    public RxSearchView(Context context) {
        super(context);
    }

    public RxSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Observable<String> getOnQueryChangeObservable(){

        final PublishSubject<String> querySubject = PublishSubject.create();

        setOnQueryChangeListener(new OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    clearSuggestions();
                } else {
                    querySubject.onNext(newQuery);
                }
            }
        });

        return querySubject.asObservable();
    }

    public Observable<String> getOnSearchObservable(){

        final PublishSubject<String> searchSubject = PublishSubject.create();

        setOnSearchListener(new OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                searchSubject.onNext(searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String currentQuery) {
                searchSubject.onNext(currentQuery);
            }
        });

        return searchSubject.asObservable();
    }
}
