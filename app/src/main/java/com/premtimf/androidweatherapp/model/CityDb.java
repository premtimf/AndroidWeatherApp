package com.premtimf.androidweatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "TABLE_CITIES")
public class CityDb implements Parcelable {

    public static final Creator<CityDb> CREATOR = new Creator<CityDb>() {
        @Override
        public CityDb createFromParcel(Parcel in) {
            return new CityDb(in);
        }

        @Override
        public CityDb[] newArray(int size) {
            return new CityDb[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private String id;
    private String name;

    protected CityDb(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public CityDb(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public CityDb() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }


}
