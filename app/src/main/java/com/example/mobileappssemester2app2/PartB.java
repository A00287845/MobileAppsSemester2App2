package com.example.mobileappssemester2app2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileappssemester2app2.util.WeatherRecordsViewModel;

public class PartB extends Fragment {
    private WeatherRecordsViewModel viewModel;


    public PartB() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("EOGHAN", "onCreateView: Partb");
        View view = inflater.inflate(R.layout.fragment_part_b, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(WeatherRecordsViewModel.class);

        viewModel.getWeatherRecordsLiveData().observe(getViewLifecycleOwner(), buttons -> {

        });

        return view;
    }
}