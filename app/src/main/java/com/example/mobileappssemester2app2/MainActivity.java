package com.example.mobileappssemester2app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

    private WeatherRecordsViewModel viewModel;

    private final BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("EOGHAN", "MainActivity onReceive()");
            String jsonData = intent.getStringExtra("weather_data");
            if (jsonData != null) {
                GsonBuilder builder = new GsonBuilder();
                JsonDeserializer<WeatherRecord> deserializer = getDeserializer();
                builder.registerTypeAdapter(WeatherRecord.class, deserializer);
                Gson gson = builder.create();
                WeatherRecord receivedRecord = gson.fromJson(jsonData, WeatherRecord.class);
                Log.d("EOGHAN", "MainActivity onReceive: weather received MAIN " + receivedRecord);

                ArrayList<WeatherRecord> records = viewModel.getWeatherRecordsLiveData().getValue();
                assert records != null;
                String logMessage;
                if (records.isEmpty() || !receivedRecord.getDate().equals(records.get(records.size() - 1).getDate())) {
                    logMessage = records.isEmpty() ? "ARRAY IS EMPTY" : "THEY DO NOT EQUAL";
                    Log.d("EOGHAN", "onReceive: view model? "+viewModel.getApplication() + " message " + logMessage);
                    viewModel.addRecord(receivedRecord);
                } else {
                    logMessage = "THEY DO EQUAL";
                    Log.d("EOGHAN", "onReceive: " + logMessage);

                }

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
                    Log.e("EOGHAN", "getDeserializer: date parse error", e);;
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
        Log.d("Eoghan", "MainActivity onCreate: MAIN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(WeatherRecordsViewModel.class);

        Log.d("EOGHAN", "MainActivity onCreate: setting records init with " + viewModel.getWeatherRecordsLiveData().getValue());

        PeriodicWorkRequest weatherRequest = new PeriodicWorkRequest.Builder(WeatherWorker.class, 15, TimeUnit.MINUTES).setConstraints(new Constraints.Builder().setRequiresCharging(false).setRequiredNetworkType(NetworkType.UNMETERED).build()).build();
        WorkManager.getInstance(this).enqueue(weatherRequest);

        Button buttonA = findViewById(R.id.buttonA);
        Button buttonB = findViewById(R.id.buttonB);

        buttonA.setOnClickListener(v -> doTransition(new PartA()));

        buttonB.setOnClickListener(v -> doTransition(new PartB()));

    }

    private void doTransition(Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setCustomAnimations(R.anim.fade_in, 0);
        transaction.replace(R.id.contentLayout, frag);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("ACTION_WEATHER_DATA");
        LocalBroadcastManager.getInstance(this).registerReceiver(weatherReceiver, filter);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, new PartA()).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weatherReceiver);
    }
}