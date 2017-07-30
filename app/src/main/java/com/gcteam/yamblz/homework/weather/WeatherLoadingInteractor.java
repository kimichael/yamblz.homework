package com.gcteam.yamblz.homework.weather;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.gcteam.yamblz.homework.settings.PreferencesManager;
import com.gcteam.yamblz.homework.weather.api.Weather;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by turist on 16.07.2017.
 */

public class WeatherLoadingInteractor {

    private Disposable subscription;
    private Consumer<Throwable> errorHandler;
    private PreferencesManager preferencesManager;

    public WeatherLoadingInteractor(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    void bind(final WeatherLoadingView view) {
        this.errorHandler = new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                view.loadingError();
            }
        };

        Observable<Weather> lastWeather = WeatherStorage.get().lastWeather();
        Weather fromStorage = WeatherStorage.get().load();
        if(fromStorage != null) {
            this.subscription = lastWeather.startWith(fromStorage).subscribe(new Consumer<Weather>() {
                @Override
                public void accept(@NonNull Weather weather) throws Exception {
                    view.loaded(weather);
                }
            });
            return;
        }

        this.subscription = lastWeather.subscribe(new Consumer<Weather>() {
            @Override
            public void accept(@NonNull Weather weather) throws Exception {
                view.loaded(weather);
            }
        });

        startRefresh();
        view.loadingStart();
    }

    void unbind() {
        if(subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
            subscription = null;
        }

        this.errorHandler = new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
            }
        };
    }

    Disposable startRefresh() {
        Single<Weather> currentWeather = WeatherService.get(preferencesManager)
                .currentWeather(Locale.getDefault().getLanguage());

        return currentWeather
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(WeatherStorage.get().updateLastWeather(), errorHandler);
    }
}