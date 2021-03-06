package com.gcteam.yamblz.homework.main;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.gcteam.yamblz.homework.weather.updating.UpdateJob;

/**
 * Created by turist on 16.07.2017.
 */

public class WeatherJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case UpdateJob.TAG:
                return new UpdateJob();
            default:
                return null;
        }
    }
}
