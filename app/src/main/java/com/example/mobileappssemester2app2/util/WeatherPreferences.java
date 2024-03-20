package com.example.mobileappssemester2app2.util;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.example.mobileappssemester2app2.pojo.WeatherRecord;

public class WeatherPreferences {

    private static final String PREFS_NAME = "weather_prefs";
    private static final String WEATHER_RECORDS_KEY = "weather_records";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public WeatherPreferences(Context context) {
        Log.d("EOGHAN", "WeatherPreferences: CONSTRUCTOR.. context? " + context);
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveWeatherRecords(ArrayList<WeatherRecord> weatherRecords) {
        Log.d("EOGHAN", "WeatherPreferences saveWeatherRecords: saving records: " + weatherRecords);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(weatherRecords);
        editor.putString(WEATHER_RECORDS_KEY, json);
        editor.apply();
    }

    public ArrayList<WeatherRecord> loadWeatherRecords() {
        String json = sharedPreferences.getString(WEATHER_RECORDS_KEY, null);
        if (json == null) {
            Log.d("EOGHAN", "WeatherPreferences loadWeatherRecords: retrieving preferences ? EMPTY : ");

            return new ArrayList<>();
        } else {
            Type weatherRecordListType = new TypeToken<ArrayList<WeatherRecord>>() {}.getType();
            Log.d("EOGHAN", "WeatherPreferences loadWeatherRecords: retrieving preferences ? : " + json);
            return gson.fromJson(json, weatherRecordListType);
        }
    }
}
