package com.example.mobileappssemester2app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.mobileappssemester2app2.pojo.WeatherRecord;
import com.example.mobileappssemester2app2.util.WeatherPreferences;
import com.example.mobileappssemester2app2.util.WeatherRecordsViewModel;
import com.example.mobileappssemester2app2.util.WeatherWorker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ArrayList<WeatherRecord> weatherRecords;
    private WeatherRecordsViewModel viewModel;

    private final BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonData = intent.getStringExtra("weather_data");
            if (jsonData != null) {
                GsonBuilder builder = new GsonBuilder();

                JsonDeserializer<WeatherRecord> deserializer = getDeserializer();
                builder.registerTypeAdapter(WeatherRecord.class, deserializer);

                Gson gson = builder.create();
                WeatherRecord receivedRecord = gson.fromJson(jsonData, WeatherRecord.class);
                Log.d("EOGHAN", "onReceive: weather received MAIN " + receivedRecord);
                weatherRecords.add(receivedRecord);
                new WeatherPreferences(MainActivity.this).saveWeatherRecords(weatherRecords);
                viewModel.setRecords(weatherRecords);
            }
        }

        @SuppressLint("SimpleDateFormat")
        private JsonDeserializer<WeatherRecord> getDeserializer() {
            return (json, typeOfT, context) -> {
                JsonObject current = json.getAsJsonObject().getAsJsonObject("current");

                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(current.get("time").getAsString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                double humidity = current.get("relativehumidity_2m").getAsDouble();
                double temperature = current.get("temperature_2m").getAsDouble();
                double windspeed = current.get("wind_speed_10m").getAsDouble();

                return new WeatherRecord(date, humidity, temperature, windspeed);
            };
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Eoghan", "onCreate: MAIN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(WeatherRecordsViewModel.class);

        weatherRecords = new WeatherPreferences(this).loadWeatherRecords();

        Log.d("EOGHAN", "onCreate: setting records init with " + weatherRecords);
        viewModel.setRecords(weatherRecords);

        PeriodicWorkRequest weatherRequest = new PeriodicWorkRequest.Builder(WeatherWorker.class, 15, TimeUnit.MINUTES).setConstraints(new Constraints.Builder().setRequiresCharging(false).setRequiredNetworkType(NetworkType.UNMETERED).build()).build();
        WorkManager.getInstance(this).enqueue(weatherRequest);

        Button buttonA = findViewById(R.id.buttonA);
        Button buttonB = findViewById(R.id.buttonB);

        buttonA.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, new PartA()).commit());

        buttonB.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, new PartB()).commit());

        getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, new PartA()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("ACTION_WEATHER_DATA");
        LocalBroadcastManager.getInstance(this).registerReceiver(weatherReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weatherReceiver);
    }
}