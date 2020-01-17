//package com.premtimf.androidweatherapp.persistence;
//
//import android.content.Context;
//
//import androidx.lifecycle.LiveData;
//
//import com.premtimf.androidweatherapp.model.CityDb;
//
//import java.util.List;
//
//import io.reactivex.Completable;
//import io.reactivex.Single;
//
//public class CityRepository {
//    private CityDao mCityDao;
//    private Single<List<CityDb>> mAllCities;
//
//    public CityRepository (Context context){
//        CitiesDatabase db = CitiesDatabase.getDatabase(context);
//        mCityDao = db.cityDao();
//        mAllCities = mCityDao.getAllCities();
//    }
//
//    Single<List<CityDb>> getAllCities(){
//        return mAllCities;
//    }
//
//    void insert(CityDb... cityDbs){
//        CitiesDatabase.databaseWriteExecutor.execute(() -> mCityDao.insert(cityDbs));
//    }
//
//}
