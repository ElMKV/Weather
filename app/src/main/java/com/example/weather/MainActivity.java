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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

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
    String latitude, longitude, iconCode;

    Button btnGetLocation;
    Double lat;
    Double lon;
    FusedLocationProviderClient mFusedLocationClient;

    TextView nameTextView, tempMinTextView, tempMaxTextView, feelsLikeTextView, cloudsTextView, codeIcon;
    ImageView imageViewIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = findViewById(R.id.name);
        tempMaxTextView = findViewById(R.id.tempMax);
        tempMinTextView = findViewById(R.id.tempMin);
        feelsLikeTextView = findViewById(R.id.feelsLike);
        cloudsTextView = findViewById(R.id.cloudsTextView);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        codeIcon = findViewById(R.id.codeIcon);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();

        } else {
            getLocation();
            loadInfo();
        }

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

                        Info info = response.body();
                        getInfoAndInitView(info);

                    }

                    @Override
                    public void onFailure(Call<Info> call, Throwable t) {
                        Log.d("log", "Ошибка " +  t.toString());



                    }
                });



    }

    private void getInfoAndInitView(Info info) {
        Log.d("log", info.getTimezone());




    }


}