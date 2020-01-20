package com.premtimf.androidweatherapp.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.premtimf.androidweatherapp.model.CityDb;

@Database(entities = {CityDb.class}, version = 1, exportSchema = false)
public abstract class CitiesDatabase extends RoomDatabase {
    private static CitiesDatabase instance;

    public static CitiesDatabase getDatabase(Context context) {
        if (instance == null) {
            synchronized (CitiesDatabase.class) {
                if (instance == null) {
                    instance = Room.
                            databaseBuilder(context.getApplicationContext(), CitiesDatabase.class, "Cities_DB")
                            .build();
                }
            }
        }
        return instance;
    }

//    private static final int NUMBER_OF_THREADS = 4;
//    static final ExecutorService databaseWriteExecutor =
//            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract CityDao cityDao();
}
