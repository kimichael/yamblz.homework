package com.gcteam.yamblz.homework;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.facebook.stetho.Stetho;
import com.gcteam.yamblz.homework.domain.update.weather.WeatherJobCreator;
import com.gcteam.yamblz.homework.presentation.di.ComponentManager;

import timber.log.Timber;

/**
 * Created by turist on 16.07.2017.
 */

public class WeatherApplication extends Application {
    private static ComponentManager componentManager;

    public static ComponentManager getComponentManager() {
        return componentManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new WeatherJobCreator());
        componentManager = new ComponentManager(this);
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            Timber.plant(new Timber.DebugTree());
        }
    }

}
