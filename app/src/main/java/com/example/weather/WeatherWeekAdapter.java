package com.example.weather;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class WeatherWeekAdapter extends RecyclerView.Adapter<WeatherWeekAdapter.ViewHolder> {
    Context context;
    String codeWeatherWeek;
    List<Daily> list;
    public WeatherWeekAdapter(Context context, List<Daily> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public WeatherWeekAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.week_weather_card, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull WeatherWeekAdapter.ViewHolder holder, int position) {
        Double day = list.get(position).getTemp().getDay();
        Double night = list.get(position).getTemp().getNight();
        day = Math.ceil(day);
        night = Math.ceil(night);
        holder.dayWeather.setText(day + "");
        holder.nightWeather.setText(night + "");

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date dateFormat = new java.util.Date(list.get(position).getDt() * 1000);
        String week = sdf.format(dateFormat);
        week = week.substring(0, 1).toUpperCase() + week.substring(1);
        holder.textViewNameWeek.setText(week);

        codeWeatherWeek = list.get(position).getWeather().get(0).getIcon();

        Picasso.with(context)
                .load("http://openweathermap.org/img/wn/" + codeWeatherWeek + "@2x.png")
                .into(holder.imageViewWeatherWeek);

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dayWeather, nightWeather, textViewNameWeek;
        ImageView imageViewWeatherWeek;
        public ViewHolder(View itemView) {
            super(itemView);
            dayWeather = (TextView) itemView.findViewById(R.id.textViewDayTemp);
            nightWeather = (TextView) itemView.findViewById(R.id.textViewNightTemp);
            textViewNameWeek = (TextView) itemView.findViewById(R.id.textViewNameWeek);
            imageViewWeatherWeek = (ImageView) itemView.findViewById(R.id.imageViewWeatherWeek);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

        }
    }
}
