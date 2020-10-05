package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    double lat = 55.4;
    double lon = 37.5;

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadInfo();


    }

    private void loadInfo() {
        NetworkService
                .getInstance()
                .getCityWithName()
                .GetSityInf(lat, lon)
                .enqueue(new Callback<Info>() {
                    @Override
                    public void onResponse(Call<Info> call, Response<Info> response) {
                        Log.d("loc", "вошел");
                        Info info = response.body();
                        info.getList().get(1).getName();
                        Log.d("loc", info.getList().get(1).getName());
                    }

                    @Override
                    public void onFailure(Call<Info> call, Throwable t) {

                    }
                });


    }


}