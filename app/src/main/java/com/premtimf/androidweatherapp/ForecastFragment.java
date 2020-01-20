package com.premtimf.androidweatherapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceFragment;
import com.premtimf.androidweatherapp.adapter.WeatherForecastAdapter;
import com.premtimf.androidweatherapp.model.WeatherForecastResult;
import com.premtimf.androidweatherapp.presenter.ForecastPresenter;

import com.premtimf.androidweatherapp.view.ForecastView;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends MvpLceFragment<SwipeRefreshLayout, WeatherForecastResult, ForecastView, ForecastPresenter>
        implements ForecastView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.txt_city_name)
    TextView mTextCityName;
    @BindView(R.id.txt_geo_coords)
    TextView mTextGeoCoords;
    @BindView(R.id.recycler_forecast)
    RecyclerView mRecyclerForecast;
    WeatherForecastAdapter mWeatherForecastAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_forecast, container, false);

        return itemView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mRecyclerForecast.setHasFixedSize(true);
        mRecyclerForecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        loadData(false);
        contentView.setOnRefreshListener(this);

    }

    @Override
    public ForecastPresenter createPresenter() {
        return new ForecastPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public void setData(WeatherForecastResult weatherForecastResult) {

        mTextCityName.setText(new StringBuilder(weatherForecastResult.city.name));
        mTextGeoCoords.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));

        mWeatherForecastAdapter = new WeatherForecastAdapter(getContext(), weatherForecastResult);
        mRecyclerForecast.setAdapter(mWeatherForecastAdapter);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.initComposite();
        presenter.getWeatherInformation(pullToRefresh);
    }

    @Override
    public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        contentView.setRefreshing(pullToRefresh);
    }
}
