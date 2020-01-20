package com.premtimf.androidweatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.premtimf.androidweatherapp.R;
import com.premtimf.androidweatherapp.common.Common;
import com.premtimf.androidweatherapp.model.MyList;
import com.premtimf.androidweatherapp.model.WeatherForecastResult;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder> {

    private Context context;
    private WeatherForecastResult weatherForecastResult;


    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_forecast, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String weatherDesc;
        List<MyList> list = weatherForecastResult.list;

        //Load icon
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/")
                .append(list.get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(holder.mImageWeather);

        //Load data

        weatherDesc = new StringBuilder("")
                .append(list.
                        get(position).weather.get(0).getDescription()).toString();
        weatherDesc = weatherDesc.substring(0, 1).toUpperCase() + weatherDesc.substring(1);
        holder.mTxtDescription.setText(weatherDesc);
        holder.mTxtTemperature.setText(new StringBuilder(String.valueOf((int) list
                .get(position).main.getTemp()))
                .append("°C").toString());
        holder.mTxtDateTime.setText(Common.convertUnixDate(list.get(position).dt));

    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_date)
        TextView mTxtDateTime;
        @BindView(R.id.txt_description)
        TextView mTxtDescription;
        @BindView(R.id.txt_temperature)
        TextView mTxtTemperature;
        @BindView(R.id.img_weather)
        ImageView mImageWeather;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }
}
