package com.premtimf.androidweatherapp.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.premtimf.androidweatherapp.common.Common;
import com.premtimf.androidweatherapp.model.WeatherForecastResult;
import com.premtimf.androidweatherapp.retrofit.IOpenWeatherMap;
import com.premtimf.androidweatherapp.retrofit.RetrofitClient;
import com.premtimf.androidweatherapp.view.ForecastView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ForecastPresenter extends MvpBasePresenter<ForecastView> {
    private CompositeDisposable mCompositeDisposable;
    private IOpenWeatherMap mService;

    public void initComposite(){
        mCompositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    public void getWeatherInformation(final boolean pullToRefresh){
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

                        ifViewAttached(view -> {
                            view.setData(weatherForecastResult);
                            view.showContent();
                        });

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ifViewAttached(view -> {
                            view.showError(throwable.getCause(), pullToRefresh);
                        });
                    }
                })
        );
    }
}
