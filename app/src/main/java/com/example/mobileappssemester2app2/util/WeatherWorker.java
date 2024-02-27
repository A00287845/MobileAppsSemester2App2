package com.example.mobileappssemester2app2.util;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WeatherWorker extends Worker {
    private final Context context;

    public WeatherWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        String jsonData = "";

        Intent intent = new Intent("ACTION_WEATHER_DATA");
        intent.putExtra("weather_data", jsonData);

        context.sendBroadcast(intent);

        return Result.success();
    }
}
