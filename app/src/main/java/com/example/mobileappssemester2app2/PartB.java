package com.example.mobileappssemester2app2;

import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileappssemester2app2.util.WeatherRecordsViewModel;

import java.io.IOException;

public class PartB extends Fragment {
    private WeatherRecordsViewModel viewModel;
    ImageView imageView;
    TextView tempTv, humidTv, windSpeedTv, countTv, firstRecordTv, lastRecordTv;

    public PartB() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("EOGHAN", "onCreateView: Partb");

        View view = inflater.inflate(R.layout.fragment_part_b, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(WeatherRecordsViewModel.class);


        colourButtons();

        imageView = view.findViewById(R.id.windImage);

        tempTv = view.findViewById(R.id.temperatureTextView);
        humidTv = view.findViewById(R.id.humidityTextView);
        windSpeedTv = view.findViewById(R.id.windspeedTv);
        countTv = view.findViewById(R.id.recordsCountTextView);
        firstRecordTv = view.findViewById(R.id.oldRecordDateTV);
        lastRecordTv = view.findViewById(R.id.newestRecordDateTV);

        ImageDecoder.Source sourceImage = ImageDecoder.createSource(getResources(), R.drawable.windy_edited_fix);
        new Thread(() -> setUpImage(sourceImage)).start();


        viewModel.getWeatherRecordsLiveData().observe(getViewLifecycleOwner(), buttons -> {
            if (windSpeedTv != null) {
                if (buttons.isEmpty()) {
                    return;
                }
                double temperature = buttons.get(buttons.size() - 1).getTemperature();
                Log.d("WeatherUpdate", "Setting temperature: " + temperature);
                tempTv.setText(String.format("%.2f°C", temperature));

                double humidity = buttons.get(buttons.size() - 1).getHumidity();
                Log.d("WeatherUpdate", "Setting humidity: " + humidity);
                humidTv.setText(String.format("%.2f%%", humidity));

                double windSpeed = buttons.get(buttons.size() - 1).getWindspeed();
                Log.d("WeatherUpdate", "Setting wind speed: " + windSpeed);
                windSpeedTv.setText(String.format("%.2fkm/h", windSpeed));

                int count = buttons.size();
                Log.d("WeatherUpdate", "Setting count: " + count);
                countTv.setText(String.valueOf(count));

                String firstRecordDate = buttons.get(0).getDate().toString();
                Log.d("WeatherUpdate", "Setting first record date: " + firstRecordDate);
                firstRecordTv.setText(firstRecordDate);

                String lastRecordDate = buttons.get(buttons.size() - 1).getDate().toString();
                Log.d("WeatherUpdate", "Setting last record date: " + lastRecordDate);
                lastRecordTv.setText(lastRecordDate);
            }
        });

    }

    private void setUpImage(ImageDecoder.Source source) {
        try {
            Drawable drawable = ImageDecoder.decodeDrawable(source);
            requireActivity().runOnUiThread(() -> setDrawable(drawable));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDrawable(Drawable drawable) {
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
            ((AnimatedImageDrawable) drawable).start();
        } else {
            imageView.setImageDrawable(drawable);
        }
    }

    private void colourButtons() {
        Button buttonB = requireActivity().findViewById(R.id.buttonA);
        Button buttonA = requireActivity().findViewById(R.id.buttonB);

        buttonA.setEnabled(false);
        buttonB.setEnabled(true);

        buttonA.setBackgroundColor(Color.parseColor("#7093FF"));
        buttonA.setTextColor(Color.parseColor("#330066"));
        buttonB.setBackgroundColor(Color.parseColor("#330066"));
        buttonB.setTextColor(Color.parseColor("#7093FF"));
    }
}