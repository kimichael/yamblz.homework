package com.gcteam.yamblz.homework.data.network.weather;

import com.gcteam.yamblz.homework.data.WeatherResponseMapper;
import com.gcteam.yamblz.homework.data.api.OpenWeatherMapApi;
import com.gcteam.yamblz.homework.data.api.dto.weather.forecast.ForecastResponse;
import com.gcteam.yamblz.homework.domain.object.WeatherData;
import com.gcteam.yamblz.homework.utils.PreferencesManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.HashSet;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.gcteam.yamblz.homework.data.api.OpenWeatherMapApi.API_KEY;

/**
 * Created by turist on 16.07.2017.
 */

public class WeatherService {

    private static final String METRIC_UNITS = "metric";

    private static final HashSet<String> units = new HashSet<>(Arrays.asList(
            "standard", "metric", "imperial"));
    private static final HashSet<String> codes = new HashSet<>(Arrays.asList(
            "ar", "bg", "ca", "cz", "de", "el", "en", "fa", "fi", "fr", "gl", "hr", "hu",
            "it", "ja", "kr", "la", "lt", "mk", "nl", "pl", "pt", "ro", "ru", "se", "sk",
            "sl", "es", "tr", "ua", "vi", "zh_cn", "zh_tw"));

    private OpenWeatherMapApi api;
    private WeatherResponseMapper weatherResponseMapper;
    private PreferencesManager preferencesManager;

    @Inject
    public WeatherService(PreferencesManager preferencesManager, WeatherResponseMapper weatherResponseMapper, OpenWeatherMapApi api) {

        this.weatherResponseMapper = weatherResponseMapper;
        this.preferencesManager = preferencesManager;
        this.api = api;
    }

    public static String checkUnitsType(String unitsType) {
        if (units.contains(unitsType)) {
            return unitsType;
        }

        return "standard";
    }

    public static String checkLangCode(String lang) {
        if (codes.contains(lang)) {
            return lang;
        }

        return "en";
    }

    public Single<WeatherData> getCurrentWeather(LatLng latLng, String units, String lang) {
        return api.weatherByLatLng(API_KEY,
                Double.toString(latLng.latitude),
                Double.toString(latLng.longitude),
                checkUnitsType(units),
                checkLangCode(lang))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(weatherResponseMapper);
    }

    public Single<WeatherData> getCurrentWeather(String lang) {
        return getCurrentWeather(new LatLng(preferencesManager.getLat(),
                        preferencesManager.getLng()),
                METRIC_UNITS,
                lang);
    }

    public Single<ForecastResponse> getForecast(String lat, String lon, String lang) {
        return api.forecastByLatLng(API_KEY,
                lat, lon,
                METRIC_UNITS,
                checkLangCode(lang));
    }
}
