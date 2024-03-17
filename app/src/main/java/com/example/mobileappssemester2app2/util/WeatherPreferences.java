package com.example.mobileappssemester2app2.util;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import com.example.mobileappssemester2app2.pojo.WeatherRecord;

public class WeatherPreferences {

    private static final String PREFS_NAME = "weather_prefs";
    private static final String WEATHER_RECORDS_KEY = "weather_records";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public WeatherPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveWeatherRecords(WeatherRecord[] weatherRecords) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(weatherRecords);
        editor.putString(WEATHER_RECORDS_KEY, json);
        editor.apply();
    }

    public WeatherRecord[] loadWeatherRecords() {
        String json = sharedPreferences.getString(WEATHER_RECORDS_KEY, null);
        if (json == null) {
            return new WeatherRecord[0];
        } else {
            Type weatherRecordArrayType = new TypeToken<WeatherRecord[]>() {}.getType();
            return gson.fromJson(json, weatherRecordArrayType); // Deserialize the JSON back into an array
        }
    }
}
