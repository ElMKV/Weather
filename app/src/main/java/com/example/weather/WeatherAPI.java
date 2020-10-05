package com.example.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("/data/2.5/find?&?&units=metric&lang=ru&appid=14fcc3726972a2ee7e67a8a0a98c87d8")
    public Call<Info>GetSityInf(@Query(value = "lat", encoded = false) Double lat,@Query(value = "lon", encoded = false) Double lon);

}
