package com.premtimf.androidweatherapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.premtimf.androidweatherapp.common.Common;
import com.premtimf.androidweatherapp.model.WeatherResult;
import com.premtimf.androidweatherapp.retrofit.IOpenWeatherMap;
import com.premtimf.androidweatherapp.retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment {

    private List<String> listCities;
    private MaterialSearchBar searchBar;

    ImageView mImgWeather;
    TextView mTextCityName, mTextTemperature, mTextDescription, mTextDateTime, mTextWind, mTextHumidity, mTextPressure, mTextSunrise, mTextSunset, mTextGeoCoords;
    LinearLayout mWeatherPanel;
    ProgressBar mLoading;

    CompositeDisposable mCompositeDisposable;
    IOpenWeatherMap mService;

    static CityFragment instance;

    public static CityFragment getInstance(){
        if (instance == null)

            instance = new CityFragment();

        return instance;
    }

    public CityFragment() {
        mCompositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_city, container, false);

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

        searchBar = (MaterialSearchBar) itemView.findViewById(R.id.searchBar);
        searchBar.setEnabled(false);

        new LoadCities().execute();

        return itemView;
    }

    public class LoadCities extends SimpleAsyncTask<List<String>> {
        @Override
        protected List<String> doInBackgroundSimple() {
            listCities = new ArrayList<>();
            try {
                StringBuilder stringBuilder = new StringBuilder();
                InputStream inputStream = getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

                InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String readed;
                while ((readed = bufferedReader.readLine()) != null){
                    stringBuilder.append(readed);
                    System.out.println("-----" + readed);
                }
                listCities = new Gson().fromJson(stringBuilder.toString(), new TypeToken<List<String>>(){}.getType());

            } catch (IOException e){
                e.printStackTrace();
            }
            return listCities;
        }

        @Override
        protected void onSuccess(final List<String> listCity) {
            super.onSuccess(listCity);

            searchBar.setEnabled(true);
            searchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<String> suggest = new ArrayList<>();
                    for (String search : listCity ){
                        if (search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                            suggest.add(search);
                    }
                    searchBar.setLastSuggestions(suggest);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {

                }

                @Override
                public void onSearchConfirmed(CharSequence text) {

                    getWeatherInfo(text.toString());
                    searchBar.setLastSuggestions(listCity);

                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });

            searchBar.setLastSuggestions(listCity);

            mLoading.setVisibility(View.GONE);
            mWeatherPanel.setVisibility(View.VISIBLE);
        }
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

    private void getWeatherInfo(String cityName) {
        mCompositeDisposable.add(mService.getWeatherByCityName(cityName,
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
                        mTextTemperature.setText(new StringBuilder(String.valueOf((int)weatherResult.getMain().getTemp()))
                                .append("°C").toString());
                        mTextDateTime.setText(Common.converUnixToDate(weatherResult.getDt()));
                        mTextPressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure()))
                                .append(" hpa").toString());
                        mTextWind.setText(new StringBuilder("Speed: ")
                                .append((weatherResult.getWind().getSpeed()))
                                .append(" Deg: ").append(weatherResult.getWind().getDeg()).toString());
                        mTextHumidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity()))
                                .append(" %").toString());
                        mTextSunrise.setText(Common.converUnixToHour(weatherResult.getSys().getSunrise()));
                        mTextSunset.setText(Common.converUnixToHour(weatherResult.getSys().getSunset()));
                        mTextGeoCoords.setText(new StringBuilder(weatherResult.getCoord().toString()).toString());

                        //Display panel
                        mWeatherPanel.setVisibility(View.VISIBLE);
                        mLoading.setVisibility(View.GONE);


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }));
    }
}
