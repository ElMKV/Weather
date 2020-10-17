package com.example.weather;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.AlertDialog.Builder;

public class MainActivity extends AppCompatActivity {
    LineGraphSeries<DataPoint> series;
    private static final int REQUEST_LOCATION = 1;
    String KEY = "14fcc3726972a2ee7e67a8a0a98c87d8";
    String UNITS = "metric";
    String LANG = "ru";
    LocationManager locationManager;
    String codeWeather;
    Double lat;
    Double lon;
    TextView nameTextView, sunSetTextView, sunRiseTextView, feelsLikeTextView, cloudsTextView, textViewPower, textViewHumidity,
            textViewCloudsPer, textViewVisibility, textViewUviIndex, textViewWindSpeed, textViewWindDeg;
    RecyclerView recyclerView;
    SeekBar seekBarUvi;
    ImageView imageViewIcon;
    WeatherWeekAdapter adapter;
    GraphView graph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getView();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        checkEneable();
    }

    private void getView() {
        Log.d("LOGG", "getView");
        nameTextView = findViewById(R.id.name);
        feelsLikeTextView = findViewById(R.id.feelsLikeTextView);
        cloudsTextView = findViewById(R.id.cloudsTextView);
        sunSetTextView = findViewById(R.id.sunSetTextView);
        sunRiseTextView = findViewById(R.id.sunRiseTextView);
        cloudsTextView = findViewById(R.id.cloudsTextView);
        imageViewIcon = findViewById(R.id.imageViewIcon);
        recyclerView = findViewById(R.id.recyclerView);
        textViewPower = findViewById(R.id.textViewPower);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewCloudsPer = findViewById(R.id.textViewCloudsPer);
        textViewVisibility = findViewById(R.id.textViewVisibility);
        seekBarUvi = findViewById(R.id.seekBarUvi);
        textViewWindSpeed = findViewById(R.id.textViewWindSpeed);
        textViewWindDeg = findViewById(R.id.textViewWindDeg);
        textViewUviIndex = findViewById(R.id.textViewUviIndex);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    private void checkEneable() {
        Log.d("LOGG", "checkEneable");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("LOGG", "if checkEneable");
            OnGPS();

        } else {
            getLocation();
            Log.d("LOGG", "else checkEneable");
        }
    }

    @Override
    protected void onRestart() {
        Log.d("LOGG", "onResume");
        super.onRestart();
        checkEneable();
    }

    private void OnGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            final Builder builder = new Builder(this);
            builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                lat = locationGPS.getLatitude();
                lon = locationGPS.getLongitude();
                Log.d("loc", String.valueOf(lat));
                Log.d("loc", String.valueOf(lon));

                loadInfo();

            } else {
                Toast.makeText(this, "Не удалось найти местоположение", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadInfo() {
        NetworkService.getInstance()
                .getCityWithName()
                .GetSityInf(lat, lon, LANG, KEY, UNITS)
                .enqueue(new Callback<Info>() {
                    @Override
                    public void onResponse(Call<Info> call, Response<Info> response) {

                        Info info = response.body();
                        getInfoAndInitView(info);

                    }

                    @Override
                    public void onFailure(Call<Info> call, Throwable t) {
                        Log.d("LOG", "Ошибка " + t.toString());
                        feelsLikeTextView.setText("-");
                        Toast.makeText(getApplicationContext(), "Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void getInfoAndInitView(Info info) {

        Log.d("log", info.getTimezone());
        Log.d("LOGG", "getInfoAndInitView");


        nameTextView.setText("Регион - " + info.getTimezone());

        DecimalFormat df = new DecimalFormat("###");

        feelsLikeTextView.setText(df.format(info.getCurrent().getFeelsLike()) + "\u2103");
        cloudsTextView.setText(info.getCurrent().getWeather().get(0).getDescription());
        codeWeather = info.getCurrent().getWeather().get(0).getIcon();

        Integer unixSunRise = info.getCurrent().getSunrise();
        Integer unixSunSet = info.getCurrent().getSunset();
        Date dateSunRise = new Date(unixSunRise * 1000L);
        Date dateSunSet = new Date(unixSunSet * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");


        sunRiseTextView.setText("Восход: " + sdf.format(dateSunRise));
        sunSetTextView.setText("Закат: " + sdf.format(dateSunSet));

        Picasso.with(getApplicationContext())
                .load("http://openweathermap.org/img/wn/" + codeWeather + "@2x.png")
                .into(imageViewIcon);
        ArrayList<Daily> dailies = (ArrayList<Daily>) info.getDaily();
        adapter = new WeatherWeekAdapter(MainActivity.this, dailies);
        recyclerView.setAdapter(adapter);

        textViewPower.setText("Давление - " + Math.ceil( info.getCurrent().getPressure()/1.333) + " мм.рт.ст.");//получаем мм.рт.ст.
        textViewHumidity.setText("Влажность - " + info.getCurrent().getHumidity().toString());
        textViewCloudsPer.setText(info.getCurrent().getClouds().toString() + " % облачности");
        textViewVisibility.setText(info.getCurrent().getVisibility().toString() + " метров видимость" );
        textViewWindSpeed.setText("Скорость ветра - " + info.getCurrent().getWindSpeed().toString() + " м/с");
        Log.d("LOGG" ,info.getCurrent().getWindSpeed().toString());
        double uvi = Math.ceil(info.getCurrent().getUvi());
        int valueUvi = (int) uvi;
        textViewUviIndex.setText("Интенсивность УФ излучения - " + valueUvi);
        seekBarUvi.setProgress(valueUvi);
        seekBarUvi.setEnabled(false);
        textViewWindDeg.setText("Направление ветра - " + GetComapWing(info.getCurrent().getWindDeg()));

        initGraphView(info);

    }

    static String GetComapWing(int index) {
        String wing = null;

        if (index == 360 || index == 0){
            wing = "С";
        }
        if (index>0 & index<90){
            wing = "СВ";
        }
        if (index==90){
            wing = "Юг";
        }
        if (index>90 & index<180){
            wing = "ЮВ";
        }
        if (index == 180){
            wing = "Ю";
        }
        if (index>180 & index<270){
            wing = "ЮЗ";
        }
        if (index > 270 & index <360){
            wing = "СЗ";
        }

        return wing;
    }

    private void initGraphView(Info info) {
        graph = (GraphView) findViewById(R.id.graph);

        if (info.getHourly().size() > 0) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            for (int i = 0; i < info.getHourly().size(); i++) {
                DataPoint point = new DataPoint(i, info.getHourly().get(i).getTemp());
                series.appendData(point, true, info.getHourly().size());
            }
            graph.getViewport().setScrollable(true); // enables horizontal scrolling
            graph.getViewport().setScrollableY(true); // enables vertical scrolling
            graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
            graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
            series.setColor(Color.WHITE);
            graph.getGridLabelRenderer().setGridColor(Color.WHITE);

            graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
            graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
            graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
            graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
            graph.getGridLabelRenderer().reloadStyles();

            DateFormat dateFormat = new SimpleDateFormat("HH");
            Date date = new Date();

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(Double.parseDouble(dateFormat.format(date)));
            graph.getViewport().setMaxX(Double.parseDouble(dateFormat.format(date)) + 4.0);

            graph.addSeries(series);
        }

    }

}