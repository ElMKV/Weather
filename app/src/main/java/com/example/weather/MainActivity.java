package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    double lat;
    double lon;
    FusedLocationProviderClient mFusedLocationClient;

    // Initializing other items
    // from layout file
    TextView nameTextView, tempMinTextView, tempMaxTextView, feelsLikeTextView, cloudsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = findViewById(R.id.name);
        tempMaxTextView = findViewById(R.id.tempMax);
        tempMinTextView = findViewById(R.id.tempMin);
        feelsLikeTextView = findViewById(R.id.feelsLike);
        cloudsTextView = findViewById(R.id.cloudsTextView);



    }



    private void loadInfo() {
        NetworkService
                .getInstance()
                .getCityWithName()
                .GetSityInf(lat, lon)
                .enqueue(new Callback<Info>() {
                    @Override
                    public void onResponse(Call<Info> call, Response<Info> response) {
                        int cLoc = 0;
                        Info info = response.body();
                        info.getList().get(cLoc).getName();
                        nameTextView.setText(info.getList().get(cLoc).getName());
                        tempMaxTextView.setText(info.getList().get(cLoc).getMain().getTempMax().toString());
                        tempMinTextView.setText(info.getList().get(cLoc).getMain().getTempMin().toString());
                        feelsLikeTextView.setText(info.getList().get(cLoc).getMain().getFeelsLike().toString());
                        feelsLikeTextView.setText(info.getList().get(cLoc).getMain().getFeelsLike().toString());
                        cloudsTextView.setText(info.getList().get(cLoc).getWeather().get(cLoc).getDescription());


                    }

                    @Override
                    public void onFailure(Call<Info> call, Throwable t) {

                    }
                });
    }

    public void reload(View view) {
        loadInfo();
    }
}