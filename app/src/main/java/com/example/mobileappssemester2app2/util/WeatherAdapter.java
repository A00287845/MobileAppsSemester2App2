package com.example.mobileappssemester2app2.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappssemester2app2.R;
import com.example.mobileappssemester2app2.pojo.WeatherRecord;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private List<WeatherRecord> weatherRecords;

    // Constructor
    public WeatherAdapter(List<WeatherRecord> weatherRecords) {
        this.weatherRecords = weatherRecords;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_record, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherRecord record = weatherRecords.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());

        holder.dateTextView.setText(sdf.format(record.getDate()));
        holder.humidityTextView.setText("Humidity: " + record.getHumidity() + "%");
        holder.temperatureTextView.setText("Temperature: " + record.getTemperature() + "°C");
        holder.windspeedTextView.setText("Wind Speed: " + record.getWindspeed() + "km/h");
    }

    @Override
    public int getItemCount() {
        return weatherRecords.size();
    }

    public void updateData(List<WeatherRecord> newWeatherRecords) {
        weatherRecords.clear(); // Clear existing data
        weatherRecords.addAll(newWeatherRecords); // Add new data
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, humidityTextView, temperatureTextView, windspeedTextView;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            humidityTextView = itemView.findViewById(R.id.humidityTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            windspeedTextView = itemView.findViewById(R.id.windspeedTextView);
        }
    }
}
