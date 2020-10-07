package com.example.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("/data/2.5/onecall?")
    public Call<Info> GetSityInf(@Query(value = "lat", encoded = false) Double lat,
                                 @Query(value = "lon", encoded = false) Double lon,
                                 @Query(value = "lang", encoded = false) String lang,
                                 @Query(value = "appid", encoded = false) String KEY,
                                 @Query(value = "units", encoded = false) String units);



//    public Call<Info>GetSityInf(@Path("?lat") Double lat, @Path("lon") Double lon);

}
