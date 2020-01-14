package com.premtimf.androidweatherapp.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.premtimf.androidweatherapp.model.CityDb;

@Database(entities = {CityDb.class}, version = 1)
public abstract class CityDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "city_db";

    public static CityDatabase instance;

    public abstract CityDbDao getCityDbDao();

    static CityDatabase getInstance(final Context context){
        if (instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    CityDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

}
