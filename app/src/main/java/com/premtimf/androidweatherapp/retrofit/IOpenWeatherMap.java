package com.premtimf.androidweatherapp.retrofit;

import com.premtimf.androidweatherapp.model.WeatherForecastResult;
import com.premtimf.androidweatherapp.model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {

    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lon,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);

    @GET("weather")
    Observable<WeatherResult> getWeatherByCityName(@Query("q") String cityName,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);
    @GET("forecast")
    Observable<WeatherForecastResult> getForecastByLatLng(@Query("lat") String lat,
                                                          @Query("lon") String lon,
                                                          @Query("appid") String appid,
                                                          @Query("units") String unit);
}
