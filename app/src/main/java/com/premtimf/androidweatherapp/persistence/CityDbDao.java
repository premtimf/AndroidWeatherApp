package com.premtimf.androidweatherapp.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.premtimf.androidweatherapp.model.CityDb;

import java.util.List;

@Dao
public interface CityDbDao {

    @Insert
    long[] insertCity(CityDb... cityDbs);

    @Query("SELECT * FROM table_cities")
    LiveData<List<CityDb>> getCities();

    @Query("SELECT * FROM table_cities WHERE name LIKE :name")
    List<CityDb> getCityWithCustomQuery(String name);
}
