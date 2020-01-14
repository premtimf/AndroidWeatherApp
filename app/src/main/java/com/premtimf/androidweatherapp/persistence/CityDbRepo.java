package com.premtimf.androidweatherapp.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.premtimf.androidweatherapp.async.InsertAsyncTask;
import com.premtimf.androidweatherapp.async.QueryCitiesInFile;
import com.premtimf.androidweatherapp.model.CityDb;

import java.util.List;

public class CityDbRepo {

    List<String> listCities;
    CityDatabase mCityDatabase;

    public CityDbRepo(Context context) {
        mCityDatabase = CityDatabase.getInstance(context);
    }

    public void insertCities(CityDb...cityDb) {
        new InsertAsyncTask(mCityDatabase.getCityDbDao()).execute(cityDb);
    }

    public LiveData<List<CityDb>> retrieveCityTask() {
        return mCityDatabase.getCityDbDao().getCities();
    }

}
