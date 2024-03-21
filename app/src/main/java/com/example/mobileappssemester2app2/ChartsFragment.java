package com.example.mobileappssemester2app2;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileappssemester2app2.pojo.WeatherRecord;
import com.example.mobileappssemester2app2.util.WeatherPreferences;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ChartsFragment extends Fragment {

    public ChartsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_charts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUi();
    }

    private void setupUi() {
        LineChart chartTemperature = requireActivity().findViewById(R.id.chart_temperature);
        LineChart chartHumidity = requireActivity().findViewById(R.id.chart_humidity);
        LineChart chartWindspeed = requireActivity().findViewById(R.id.chart_windSpeed);
        setChartData(chartTemperature, "Temperature", -10f, 40f, "°C");
        setChartData(chartHumidity, "Humidity", 0f, 100f, "%");
        setChartData(chartWindspeed, "Windspeed", 0f, 60f, "km/h");
        requireActivity().findViewById(R.id.exitChartButt).setOnClickListener(v-> getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, 0).replace(R.id.contentLayout, new PartA()).commit());
    }

    private void setChartData(LineChart chart, String CHART_NAME, float AXIS_MINIMUM, float AXIS_MAXIMUM, String Y_AXIS_LABEL) {
        List<Entry> entries = new ArrayList<>();

        for (WeatherRecord report : new WeatherPreferences(requireContext().getApplicationContext()).loadWeatherRecords()) {
            if(CHART_NAME.equals("Temperature")) {
                entries.add(new Entry(report.getDate().getTime(), (float) report.getTemperature()));
            }

            if(CHART_NAME.equals("Humidity")) {
                entries.add(new Entry(report.getDate().getTime(), (float) report.getHumidity()));
            }

            if(CHART_NAME.equals("Windspeed")) {
                entries.add(new Entry(report.getDate().getTime(), (float) report.getWindspeed()));
            }
        }

        LineDataSet dataSet = getLineDataSet(entries);
        dataSet.setColor(Color.WHITE);

        LineData lineData = new LineData(dataSet);
        lineData.setValueTextColor(Color.WHITE);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new DateAxisValueFormatter());
        xAxis.setGranularity(1f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setValueFormatter(new YAxisValueFormatter(Y_AXIS_LABEL));
        leftAxis.setAxisMinimum(AXIS_MINIMUM);
        leftAxis.setAxisMaximum(AXIS_MAXIMUM);
        chart.getAxisRight().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.BLACK);
        Description description = new Description();
        description.setText(CHART_NAME);
        description.setTextColor(Color.WHITE);
        chart.setDescription(description);
        chart.invalidate();
    }

    @NonNull
    private static LineDataSet getLineDataSet(List<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, "");


        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(Color.GREEN);
        dataSet.setCircleRadius(3f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return dataSet;
    }

    static class DateAxisValueFormatter extends ValueFormatter {
        private final SimpleDateFormat mFormat;

        public DateAxisValueFormatter() {
            mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
        }

        @Override
        public String getFormattedValue(float value) {
            long millis = (long) value;
            return mFormat.format(new Date(millis));
        }
    }

    static class YAxisValueFormatter extends ValueFormatter {
        String label;

        public YAxisValueFormatter(String label) {
            this.label = label;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return value + label;
        }
    }
}