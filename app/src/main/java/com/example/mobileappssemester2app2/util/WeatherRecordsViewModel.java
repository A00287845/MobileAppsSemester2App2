package com.example.mobileappssemester2app2.util;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileappssemester2app2.pojo.WeatherRecord;

import java.util.ArrayList;

public class WeatherRecordsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<WeatherRecord>> recordsLiveData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<WeatherRecord>> getWeatherRecordsLiveData() {
        Log.d("EOGHAN", "getWeatherRecordsLiveData: ");
        if (recordsLiveData.getValue() == null) {
            recordsLiveData.setValue(new ArrayList<>());
        }
        return recordsLiveData;
    }


    public void setRecords(ArrayList<WeatherRecord> records) {
        Log.d("EOGHAN", "setRecords: records? " + records);

        recordsLiveData.setValue(records);
    }

    public void addRecord(WeatherRecord record) {
        Log.d("EOGHAN", "addRecord: view model record? " + record);
        ArrayList<WeatherRecord> currentButtons = recordsLiveData.getValue();
        if (currentButtons == null) {
            currentButtons = new ArrayList<>();
        }
        currentButtons.add(record);
        recordsLiveData.setValue(currentButtons);
    }
}
