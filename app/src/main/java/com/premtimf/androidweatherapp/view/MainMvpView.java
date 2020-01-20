package com.premtimf.androidweatherapp.view;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface MainMvpView extends MvpView {
    void showLocationDenied();
    void setupTabs();
}
