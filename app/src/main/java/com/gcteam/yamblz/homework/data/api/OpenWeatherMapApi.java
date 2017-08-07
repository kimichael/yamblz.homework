package com.gcteam.yamblz.homework.data.api;

import com.gcteam.yamblz.homework.data.api.dto.weather.current.WeatherResponse;
import com.gcteam.yamblz.homework.data.api.dto.weather.forecast.ForecastResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by turist on 16.07.2017.
 */

public interface OpenWeatherMapApi {

    String API_BASE_URL = "http://api.openweathermap.org/data/";
    String API_KEY = "8fd5656437393710869297fbf372df49";

    @GET("2.5/weather")
    Single<WeatherResponse> weatherByCityId(@Query("appid") String apiKey,
                                            @Query("id") int cityId,
                                            @Query("units") String unitsFormat,
                                            @Query("lang") String lang);

    @GET("2.5/weather")
    Single<WeatherResponse> weatherByLatLng(@Query("appid") String apiKey,
                                            @Query("lat") String lat,
                                            @Query("lon") String lon,
                                            @Query("units") String units,
                                            @Query("lang") String lang);

    @GET("2.5/forecast/daily")
    Single<ForecastResponse> forecastByLatLng(
            @Query("appid") String apiKey,
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("units") String units,
            @Query("lang") String lang
    );
}