package com.premtimf.androidweatherapp.common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    public static final String APP_ID = "186de5e17a934df35f54b6825e975ec8";
    public static Location current_location = null;

    public static String converUnixToDate(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm EEE DD MM YYYY");
        String formatedDate = simpleDateFormat.format(date);

        return formatedDate;
    }

    public static String converUnixToHour(long sunrise) {
        Date date = new Date(sunrise*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String formatedDate = simpleDateFormat.format(date);

        return formatedDate;
    }
}
