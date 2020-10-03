package com.example.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("/data/2.5/weather?q=name&lang=ru&appid=14fcc3726972a2ee7e67a8a0a98c87d8")
    public Call<Info> getCityWithName(@Query("name") String cityName);
}
