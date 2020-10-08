package com.example.weather;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class WeatherWeekAdapter extends RecyclerView.Adapter<WeatherWeekAdapter.ViewHolder> {
    Context context;
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
        holder.dayWeather.setText(list.get(position).getTemp().getDay() + "");
        holder.nightWeather.setText(list.get(position).getTemp().getNight() + "");

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dayWeather, nightWeather;
        public ViewHolder(View itemView) {
            super(itemView);
            dayWeather = (TextView) itemView.findViewById(R.id.textViewDayTemp);
            nightWeather = (TextView) itemView.findViewById(R.id.textViewNightTemp);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

        }
    }
}
