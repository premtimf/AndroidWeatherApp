package com.premtimf.androidweatherapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.premtimf.androidweatherapp.Adapter.WeatherForecastAdapter;
import com.premtimf.androidweatherapp.Common.Common;
import com.premtimf.androidweatherapp.Model.WeatherForecastResult;
import com.premtimf.androidweatherapp.Retrofit.IOpenWeatherMap;
import com.premtimf.androidweatherapp.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    ImageView mImgWeather;
    TextView mTextCityName, mTextTemperature, mTextDescription, mTextDateTime, mTextWind, mTextHumidity, mTextPressure, mTextSunrise, mTextSunset, mTextGeoCoords;
    RecyclerView mRecyclerForecast;

    CompositeDisposable mCompositeDisposable;
    IOpenWeatherMap mService;

    static ForecastFragment instance;

    public static ForecastFragment getInstance() {

        if (instance == null)
            instance = new ForecastFragment();
        return instance;
    }

    public ForecastFragment() {
        mCompositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View itemView = inflater.inflate(R.layout.fragment_forecast, container, false);

        mTextCityName = (TextView) itemView.findViewById(R.id.txt_city_name);
        mTextGeoCoords = (TextView) itemView.findViewById(R.id.txt_geo_coords);

        mRecyclerForecast = (RecyclerView) itemView.findViewById(R.id.recycler_forecast);
        mRecyclerForecast.setHasFixedSize(true);
        mRecyclerForecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        
        getForecastWeatherInformation();

        return itemView;
    }

    private void getForecastWeatherInformation() {
        mCompositeDisposable.add(mService.getForecastByLatLng(
                String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {

                        displayForecastWeather(weatherForecastResult);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR", ""+throwable.getMessage());
                    }
                })
        );
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {

        mTextCityName.setText(new StringBuilder(weatherForecastResult.city.name));
        mTextGeoCoords.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));

        WeatherForecastAdapter weatherForecastAdapter = new WeatherForecastAdapter(getContext(), weatherForecastResult);
        mRecyclerForecast.setAdapter(weatherForecastAdapter);

    }

}
