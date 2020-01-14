package com.premtimf.androidweatherapp.async;

import android.os.AsyncTask;

import com.premtimf.androidweatherapp.model.CityDb;
import com.premtimf.androidweatherapp.persistence.CityDbDao;

public class InsertAsyncTask extends AsyncTask<CityDb, Void, Void> {

    private CityDbDao mCityDbDao;

    public InsertAsyncTask(CityDbDao cityDbDao) {
        mCityDbDao = cityDbDao;
    }

    @Override
    protected Void doInBackground(CityDb... cityDbs) {
        mCityDbDao.insertCity(cityDbs);
        return null;
    }
}
