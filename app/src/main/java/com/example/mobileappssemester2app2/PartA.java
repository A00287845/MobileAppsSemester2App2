package com.example.mobileappssemester2app2;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mobileappssemester2app2.util.WeatherAdapter;
import com.example.mobileappssemester2app2.util.WeatherPreferences;
import com.example.mobileappssemester2app2.util.WeatherRecordsViewModel;

public class PartA extends Fragment {

    private WeatherRecordsViewModel viewModel;
    private WeatherAdapter adapter;
    private RecyclerView weatherRecyclerView;

    public PartA() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_part_a, container, false);
        colourButtons();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel(view);
        setupUI(view);

        viewModel.getWeatherRecordsLiveData().observe(getViewLifecycleOwner(), weatherRecords -> {
            if (weatherRecords != null) {
                updateUI();
            }
        });
    }

    private void setupViewModel(View view) {
        viewModel = new ViewModelProvider(requireActivity()).get(WeatherRecordsViewModel.class);
        weatherRecyclerView = view.findViewById(R.id.weatherRecyclerView);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        adapter = new WeatherAdapter(viewModel.getWeatherRecordsLiveData().getValue());
        weatherRecyclerView.setAdapter(adapter);
    }

    private void setupUI(View view) {
        Button openChartButton = view.findViewById(R.id.openChartButt);
        if (openChartButton != null) {
            openChartButton.setOnClickListener(v -> populateCharts());
        } else {
            Log.e("EOGHAN", "openChartButton is null");
        }
    }

    private void updateUI() {
        if (adapter != null) {
            adapter.updateData(new WeatherPreferences(requireContext().getApplicationContext()).loadWeatherRecords());
        }
    }

    private void colourButtons() {
        Button buttonA = requireActivity().findViewById(R.id.buttonA);
        Button buttonB = requireActivity().findViewById(R.id.buttonB);

        buttonA.setEnabled(false);
        buttonB.setEnabled(true);

        buttonA.setBackgroundColor(Color.parseColor("#7093FF"));
        buttonA.setTextColor(Color.parseColor("#330066"));
        buttonB.setBackgroundColor(Color.parseColor("#330066"));
        buttonB.setTextColor(Color.parseColor("#7093FF"));
    }

    private void populateCharts() {
        weatherRecyclerView.setVisibility(View.INVISIBLE);
        requireActivity().findViewById(R.id.chartLayout).setVisibility(View.VISIBLE);
        getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, 0).replace(R.id.chartLayout, new ChartsFragment()).commit();
    }
}