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
import com.example.mobileappssemester2app2.util.WeatherRecordsViewModel;


public class PartA extends Fragment {

 private WeatherRecordsViewModel viewModel;

    public PartA() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("EOGHAN", "onCreateView: PartA");

        View view = inflater.inflate(R.layout.fragment_part_a, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(WeatherRecordsViewModel.class);

        RecyclerView weatherRecyclerView = view.findViewById(R.id.weatherRecyclerView);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        WeatherAdapter adapter = new WeatherAdapter(viewModel.getWeatherRecordsLiveData().getValue());
        Log.d("EOGHAN", "onCreateView: PartA adapter count? " + adapter.getItemCount());

        weatherRecyclerView.setAdapter(adapter);

        viewModel.getWeatherRecordsLiveData().observe(getViewLifecycleOwner(), records -> {
            if (records != null){
                Log.d("EOGHAN", "onCreateView: PartA records? " + records );
                Log.d("EOGHAN", "onCreateView: PartA adapter?? " + adapter );

                adapter.updateData(records);
            }
        });

        return view;
    }
}