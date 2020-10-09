package com.example.weather;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.AlertDialog.Builder;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    String KEY = "14fcc3726972a2ee7e67a8a0a98c87d8";
    String UNITS = "metric";
    String LANG = "ru";

    LocationManager locationManager;
    String codeWeather;

    Double lat;
    Double lon;
    FusedLocationProviderClient mFusedLocationClient;

    TextView nameTextView, sunSetTextView, sunRiseTextView, feelsLikeTextView, cloudsTextView;
    RecyclerView recyclerView;


    ImageView imageViewIcon;

    WeatherWeekAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = findViewById(R.id.name);
        feelsLikeTextView = findViewById(R.id.feelsLikeTextView);
        cloudsTextView = findViewById(R.id.cloudsTextView);
        sunSetTextView = findViewById(R.id.sunSetTextView);
        sunRiseTextView = findViewById(R.id.sunRiseTextView);
        cloudsTextView = findViewById(R.id.cloudsTextView);
        imageViewIcon = findViewById(R.id.imageViewIcon);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        checkEneable();



    }

    private void checkEneable() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();

        } else {
            getLocation();
            loadInfo();
            Log.d("log", "checkEneable");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkEneable();
        Log.d("log", "onResume");

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
                Log.d("LOG", "loc_null");
                lat = locationGPS.getLatitude();
                lon = locationGPS.getLongitude();
                Log.d("loc", String.valueOf(lat));
                Log.d("loc", String.valueOf(lon));

            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
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

                        try {
                            Info info = response.body();
                            getInfoAndInitView(info);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<Info> call, Throwable t) {
                        Log.d("LOG", "Ошибка " +  t.toString());
                        feelsLikeTextView.setText("-");



                    }
                });



    }

    private void getInfoAndInitView(Info info) {




            Log.d("log", info.getTimezone());

            nameTextView.setText(info.getTimezone());

            DecimalFormat df = new DecimalFormat("###");

            feelsLikeTextView.setText(df.format(info.getCurrent().getFeelsLike()) + "\u2103");
            cloudsTextView.setText(info.getCurrent().getWeather().get(0).getDescription());
            codeWeather = info.getCurrent().getWeather().get(0).getIcon();

//        textViewWingSpeed.setText(speedWings + "");

            Integer unixSunRise = info.getCurrent().getSunrise();
            Integer unixSunSet = info.getCurrent().getSunset();
            Date dateSunRise = new Date(unixSunRise * 1000L);
            Date dateSunSet = new Date(unixSunSet * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            String normaldateSunRise = sdf.format(dateSunRise);
            String normaldateSunSet = sdf.format(dateSunSet);

            sunRiseTextView.setText("Восход: " + sdf.format(dateSunRise));
            sunSetTextView.setText("Закат: " + sdf.format(dateSunSet));

            Picasso.with(getApplicationContext())
                    .load("http://openweathermap.org/img/wn/" + codeWeather + "@2x.png")
                    .into(imageViewIcon);
            ArrayList<Daily> dailies = (ArrayList<Daily>) info.getDaily();
            adapter = new WeatherWeekAdapter(MainActivity.this, dailies);
            recyclerView.setAdapter(adapter);





    }


}