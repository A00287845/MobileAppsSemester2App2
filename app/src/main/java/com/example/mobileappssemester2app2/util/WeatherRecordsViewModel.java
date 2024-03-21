package com.example.mobileappssemester2app2.util;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileappssemester2app2.pojo.WeatherRecord;

import java.util.ArrayList;

public class WeatherRecordsViewModel extends AndroidViewModel {
    private final MutableLiveData<ArrayList<WeatherRecord>> recordsLiveData = new MutableLiveData<>();

    public WeatherRecordsViewModel (Application application){
        super(application);
        Log.d("EOGHAN", "WeatherRecordsViewModel: CONSTRUCTOR.. applciation? " + application);
        setRecords(new WeatherPreferences(application).loadWeatherRecords());
    }

    public MutableLiveData<ArrayList<WeatherRecord>> getWeatherRecordsLiveData() {
        Log.d("EOGHAN", "WeatherRecordsViewModel getWeatherRecordsLiveData:");
        if (recordsLiveData.getValue() == null) {
            Log.d("EOGHAN", "WeatherRecordsViewModel getWeatherRecordsLiveData: recordsLiveData.getValue is null");

            recordsLiveData.setValue(new ArrayList<>());
        }
        Log.d("EOGHAN", "WeatherRecordsViewModel getWeatherRecordsLiveData: records?" + recordsLiveData.getValue());

        return recordsLiveData;
    }


    public void setRecords(ArrayList<WeatherRecord> records) {
        Log.d("EOGHAN", "WeatherRecordsViewModel setRecords: records? " + records);

        recordsLiveData.setValue(records);
    }

    public void addRecord(WeatherRecord record) {
        Log.d("EOGHAN", "*****************************");
        Log.d("EOGHAN", "WeatherRecordsViewModel addRecord: record? " + record);
        ArrayList<WeatherRecord> currentButtons = recordsLiveData.getValue();
        if (currentButtons == null) {
            currentButtons = new ArrayList<>();
        }
        currentButtons.add(record);
        Log.d("EOGHAN", "WeatherRecordsViewModel addRecordsss: recordsss? " + currentButtons);

        new WeatherPreferences(getApplication()).saveWeatherRecords(currentButtons);
        recordsLiveData.setValue(currentButtons);

    }
}
