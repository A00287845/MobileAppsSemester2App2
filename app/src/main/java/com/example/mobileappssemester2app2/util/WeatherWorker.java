package com.example.mobileappssemester2app2.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

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
        Log.d("EOGHAN", " WeatherWorker doWork: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService weatherService = retrofit.create(WeatherService.class);

        double latitude = 53.4375;
        double longitude = -7.9375;

        Call<ResponseBody> call = weatherService.getForecast(latitude, longitude, "relativehumidity_2m,temperature_2m,wind_speed_10m");

        try {
            Response<ResponseBody> response = call.execute();
            try {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String jsonResponse = responseBody.string();
                        Intent intent = new Intent("ACTION_WEATHER_DATA");
                        intent.putExtra("weather_data", jsonResponse);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        return Result.success();
                    } else {
                        return Result.failure();
                    }
                } else {
                    return Result.failure();
                }
            } finally {
                if (response.body() != null) {
                    response.body().close();
                }
            }
        } catch (IOException e) {
            Log.e("EOGHAN", "Error fetching weather data", e);
            Log.e("EOGHAN", "Error fetching weather data" + e.getMessage());
            return Result.failure();
        }

    }

    interface WeatherService {
        String BASE_URL = "https://api.open-meteo.com";

        @GET("/v1/forecast")
        Call<ResponseBody> getForecast(
                @Query("latitude") double latitude,
                @Query("longitude") double longitude,
                @Query("current") String currentParameters
        );
    }
}
