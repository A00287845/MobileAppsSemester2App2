package com.example.mobileappssemester2app2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileappssemester2app2.util.WeatherAdapter;
import com.example.mobileappssemester2app2.util.WeatherPreferences;
import com.example.mobileappssemester2app2.util.WeatherRecordsViewModel;

import java.util.ArrayList;


public class PartA extends Fragment {

    private WeatherRecordsViewModel viewModel;
    private WeatherAdapter adapter;

    public PartA() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(WeatherRecordsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("EOGHAN", "PartA onCreateView: PartA");

        View view = inflater.inflate(R.layout.fragment_part_a, container, false);
        RecyclerView weatherRecyclerView = view.findViewById(R.id.weatherRecyclerView);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));


        adapter = new WeatherAdapter(viewModel.getWeatherRecordsLiveData().getValue());
        Log.d("EOGHAN", "PartA onCreateView: PartA adapter count? " + adapter.getItemCount());

        weatherRecyclerView.setAdapter(adapter);

        viewModel.getWeatherRecordsLiveData().observe(requireActivity(), records -> {
            if (records != null) {
                Log.d("EOGHAN", "PartA onCreate: records? " + records);
                Log.d("EOGHAN", "PartA onCreate: adapter?? " + adapter);
                Log.d("EOGHAN", "PartA onCreate: viewmodel?? " + viewModel);

                if(adapter != null && !records.isEmpty()){
                    adapter.updateData(new WeatherPreferences(requireContext().getApplicationContext()).loadWeatherRecords());
                }
            }
        });

        return view;
    }
}