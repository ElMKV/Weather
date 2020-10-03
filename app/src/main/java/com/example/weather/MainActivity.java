package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String name = "Perm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       loadInfo();


    }

    private void loadInfo() {
        NetworkService.getInstance()
                .getCityWithName()
                .getCityWithName(name)
                .enqueue(new Callback<Info>() {
                    @Override
                    public void onResponse(Call<Info> call, Response<Info> response) {

                        Info info = response.body();

                        System.out.println(info.getWeather().get(0).getDescription());

                    }

                    @Override
                    public void onFailure(Call<Info> call, Throwable t) {

                    }
                });



    }
}