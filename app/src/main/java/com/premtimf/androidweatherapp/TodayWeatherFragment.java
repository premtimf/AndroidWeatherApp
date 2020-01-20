package com.premtimf.androidweatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceFragment;
import com.premtimf.androidweatherapp.common.Common;
import com.premtimf.androidweatherapp.model.WeatherResult;
import com.premtimf.androidweatherapp.presenter.TodayWeatherPresenter;
import com.premtimf.androidweatherapp.retrofit.IOpenWeatherMap;
import com.premtimf.androidweatherapp.retrofit.RetrofitClient;
import com.premtimf.androidweatherapp.view.TodayWeatherView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends MvpLceFragment<SwipeRefreshLayout, WeatherResult, TodayWeatherView, TodayWeatherPresenter>
        implements TodayWeatherView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.img_weather) ImageView mImgWeather;
    @BindView(R.id.txt_city_name) TextView mTextCityName;
    @BindView(R.id.txt_temperature) TextView mTextTemperature;
    @BindView(R.id.txt_description) TextView mTextDescription;
    @BindView(R.id.txt_date_time) TextView mTextDateTime;
    @BindView(R.id.txt_wind) TextView mTextWind;
    @BindView(R.id.txt_humidity) TextView mTextHumidity;
    @BindView(R.id.txt_pressure) TextView mTextPressure;
    @BindView(R.id.txt_sunrise) TextView mTextSunrise;
    @BindView(R.id.txt_sunset) TextView mTextSunset;
    @BindView(R.id.txt_geo_coords) TextView mTextGeoCoords;
    @BindView(R.id.weather_panel) LinearLayout mWeatherPanel;
    @BindView(R.id.loadingView) ProgressBar mLoading;

    @Override
    public TodayWeatherPresenter createPresenter() {
        return new TodayWeatherPresenter();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);
        presenter.initComposite();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        contentView.setOnRefreshListener(this);
        loadData(false);
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
    public void setData(WeatherResult weatherResult) {
        //load image
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/")
                .append(weatherResult.getWeather().get(0).getIcon())
                .append(".png").toString()).into(mImgWeather);

        //load info
        mTextCityName.setText(weatherResult.getName());
        mTextDescription.setText(new StringBuilder("Weather in ")
                .append(weatherResult.getName()).toString());
        mTextTemperature.setText(new StringBuilder(String.valueOf((int) weatherResult.getMain().getTemp()))
                .append("°C").toString());
        mTextDateTime.setText(Common.convertUnixDate(weatherResult.getDt()));
        mTextPressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure()))
                .append(" hpa").toString());
        mTextWind.setText(new StringBuilder("Speed: ")
                .append((weatherResult.getWind().getSpeed()))
                .append(" Deg: ").append(weatherResult.getWind().getDeg()).toString());
        mTextHumidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity()))
                .append(" %").toString());
        mTextSunrise.setText(Common.convertUnixHours(weatherResult.getSys().getSunrise()));
        mTextSunset.setText(Common.convertUnixHours(weatherResult.getSys().getSunset()));
        mTextGeoCoords.setText(new StringBuilder(weatherResult.getCoord().toString()).toString());

        //Display panel
        mWeatherPanel.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.getWeatherInfo(pullToRefresh);
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
    }
}