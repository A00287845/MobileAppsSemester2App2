package com.example.mobileappssemester2app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.mobileappssemester2app2.pojo.WeatherRecord;
import com.example.mobileappssemester2app2.util.WeatherWorker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private WeatherRecord[] weatherRecords; // Assuming initialization elsewhere

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter("ACTION_WEATHER_DATA");
        BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String jsonData = intent.getStringExtra("weather_data");
                // Convert jsonData to WeatherRecord[] and update UI accordingly
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(weatherReceiver, filter);

        PeriodicWorkRequest weatherRequest = new PeriodicWorkRequest.Builder(WeatherWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(new Constraints.Builder()
                        .setRequiresCharging(false)
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .build())
                .build();
        WorkManager.getInstance(this).enqueue(weatherRequest);
    }
}