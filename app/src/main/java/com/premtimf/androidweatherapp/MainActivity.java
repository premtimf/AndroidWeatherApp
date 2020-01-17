package com.premtimf.androidweatherapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.premtimf.androidweatherapp.adapter.ViewPagerAdapter;
import com.premtimf.androidweatherapp.presenter.MainMvpPresenter;
import com.premtimf.androidweatherapp.view.MainMvpView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends MvpActivity<MainMvpView, MainMvpPresenter> implements MainMvpView {

    @BindView(R.id.toolbar)
    androidx.appcompat.widget.Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.root_view)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        presenter.loadLocationPermissionDialog(this);

    }

    @NonNull
    @Override
    public MainMvpPresenter createPresenter() {
        return new MainMvpPresenter();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TodayWeatherFragment.getInstance(), "Today");
        adapter.addFragment(ForecastFragment.getInstance(), "Forecast");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void showLocationDenied() {
        Snackbar.make(coordinatorLayout, "Permission Denied", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void setupTabs() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }
}
