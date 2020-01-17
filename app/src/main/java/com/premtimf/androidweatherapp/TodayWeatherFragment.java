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

import androidx.fragment.app.Fragment;

import com.premtimf.androidweatherapp.common.Common;
import com.premtimf.androidweatherapp.model.WeatherResult;
import com.premtimf.androidweatherapp.retrofit.IOpenWeatherMap;
import com.premtimf.androidweatherapp.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {


    static TodayWeatherFragment instance;
    ImageView mImgWeather;
    TextView mTextCityName, mTextTemperature, mTextDescription, mTextDateTime, mTextWind, mTextHumidity, mTextPressure, mTextSunrise, mTextSunset, mTextGeoCoords;
    LinearLayout mWeatherPanel;
    ProgressBar mLoading;
    CompositeDisposable mCompositeDisposable;
    IOpenWeatherMap mService;

    public TodayWeatherFragment() {
        mCompositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);

    }

    public static TodayWeatherFragment getInstance() {
        if (instance == null)

            instance = new TodayWeatherFragment();

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);

        mImgWeather = (ImageView) itemView.findViewById(R.id.img_weather);
        mTextCityName = (TextView) itemView.findViewById(R.id.txt_city_name);
        mTextTemperature = (TextView) itemView.findViewById(R.id.txt_temperature);
        mTextWind = (TextView) itemView.findViewById(R.id.txt_wind);
        mTextDateTime = (TextView) itemView.findViewById(R.id.txt_date_time);
        mTextDescription = (TextView) itemView.findViewById(R.id.txt_description);
        mTextHumidity = (TextView) itemView.findViewById(R.id.txt_humidity);
        mTextPressure = (TextView) itemView.findViewById(R.id.txt_pressure);
        mTextGeoCoords = (TextView) itemView.findViewById(R.id.txt_geo_coords);
        mTextSunrise = (TextView) itemView.findViewById(R.id.txt_sunrise);
        mTextSunset = (TextView) itemView.findViewById(R.id.txt_sunset);

        mWeatherPanel = (LinearLayout) itemView.findViewById(R.id.weather_panel);
        mLoading = (ProgressBar) itemView.findViewById(R.id.loading);

        getWeatherInformation();


        return itemView;
    }

    private void getWeatherInformation() {
        mCompositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID, "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
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
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }));

    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
        super.onStop();
    }

}