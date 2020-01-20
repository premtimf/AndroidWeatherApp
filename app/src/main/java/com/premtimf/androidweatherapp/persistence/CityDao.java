package com.premtimf.androidweatherapp.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.premtimf.androidweatherapp.model.CityDb;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface CityDao {

    @Insert
    Completable insert(CityDb... cityDb);

    @Query("SELECT * FROM cities WHERE name LIKE :name")
    Single<List<CityDb>> getCitiesBySearch(String name);

    @Query("SELECT * FROM cities")
    Single<List<CityDb>> getAllCities();

}
