package com.premtimf.androidweatherapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cities")
public class CityDb {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public CityDb(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public CityDb() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
