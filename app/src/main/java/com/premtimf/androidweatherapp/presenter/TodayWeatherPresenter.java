package com.premtimf.androidweatherapp.presenter;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.premtimf.androidweatherapp.common.Common;
import com.premtimf.androidweatherapp.model.WeatherResult;
import com.premtimf.androidweatherapp.retrofit.IOpenWeatherMap;
import com.premtimf.androidweatherapp.retrofit.RetrofitClient;
import com.premtimf.androidweatherapp.view.TodayWeatherView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TodayWeatherPresenter extends MvpBasePresenter<TodayWeatherView> {

    private CompositeDisposable mCompositeDisposable;
    private IOpenWeatherMap mService;

    public void initComposite() {
        mCompositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    public void getWeatherInfo(final boolean pullToRefresh){
        mCompositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID, "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        ifViewAttached(view -> {
                            view.setData(weatherResult);
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
                }));
    }
}
