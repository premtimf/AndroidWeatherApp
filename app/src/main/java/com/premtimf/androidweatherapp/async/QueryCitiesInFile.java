package com.premtimf.androidweatherapp.async;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.premtimf.androidweatherapp.R;
import com.premtimf.androidweatherapp.model.CityDb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class QueryCitiesInFile extends AsyncTask<Void, Void, List<String>> {
    List<String> listCities;

    private Context context;

    public QueryCitiesInFile(Context current) {
        this.context = current;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        listCities = new ArrayList<>();
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = context.getResources().openRawResource(R.raw.city_list);
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

            InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readed;
            while ((readed = bufferedReader.readLine()) != null){
                stringBuilder.append(readed);
                System.out.println("-----" + readed);
            }
            listCities = new Gson().fromJson(stringBuilder.toString(), new TypeToken<List<String>>(){}.getType());

        } catch (IOException e){
            e.printStackTrace();
        }
        return listCities;
    }

}
