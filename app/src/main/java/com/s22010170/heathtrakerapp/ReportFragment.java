package com.s22010170.heathtrakerapp;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportFragment extends Fragment {

    LineChart lineChart;
    BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        // Get the line chart and bar chart from the layout
        lineChart = view.findViewById(R.id.line_chart);
        barChart = view.findViewById(R.id.bar_chart);

        // Update the line chart
        updateLineChart();
        // Update the bar chart
        updateBarChart();

        return view;
    }

    // Method to update the line chart
    private void updateLineChart(){
        // Set the colors for the labels and grid lines
        int labelColor, gridColor;
        // Check the current mode and set the colors accordingly
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        // Check if the device is in dark mode
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode
            labelColor = getResources().getColor(R.color.white, null); // Use a white color for labels in dark mode
            gridColor = getResources().getColor(R.color.gray, null); // Use a gray color for grid lines in dark mode
        } else {
            // Light mode
            labelColor = getResources().getColor(R.color.black, null); // Use a black color for labels in light mode
            gridColor = getResources().getColor(R.color.light_gray, null); // Use a light gray color for grid lines in light mode
        }

        //set the description of the line chart
        Description description = new Description();
        description.setText("Trend of Health Vitals");
        description.setPosition(150f, 15f);
        description.setTextColor(labelColor);
        lineChart.setDescription(description);

        // Disable the right y-axis
        lineChart.getAxisRight().setEnabled(false);
        // Set the x-axis values
        List<String> xAxisValues = Arrays.asList("mon", "tue", "wed", "thu", "fri", "sat", "sun");
        // Get the x-axis of the line chart
        XAxis xAxis = lineChart.getXAxis();
        // Set the position of the x-axis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // Set the x-axis values
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        // Set the label count of the x-axis
        xAxis.setLabelCount(xAxisValues.size());
        // Set the granularity of the x-axis
        xAxis.setGranularity(1f);
        // Apply the colors to the x-axis
        xAxis.setTextColor(labelColor);
        xAxis.setGridColor(gridColor);

        // Apply the colors to the left y-axis
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setTextColor(labelColor);
        leftYAxis.setGridColor(gridColor);

        // Create a new line data set
        LineDataSet lineDataSet = new LineDataSet(lineChartData(), "Trend of Health Vitals");
        // Apply the colors to the line data set
        lineDataSet.setValueTextColor(labelColor);
        // Create an array list of line data sets
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        // Create a new line data
        LineData lineData = new LineData(dataSets);
        // Set the line data to the line chart
        lineChart.setData(lineData);
        // Invalidate the line chart
        lineChart.invalidate();
    }

    //data to populate the line chart
    private ArrayList<Entry> lineChartData(){
        ArrayList<Entry> data = new ArrayList<>();
        data.add(new Entry(1, 10));
        data.add(new Entry(2, 20));
        data.add(new Entry(3, 10));
        data.add(new Entry(4, 40));
        data.add(new Entry(5, 50));
        data.add(new Entry(6, 20));
        data.add(new Entry(7, 70));
        return data;
    }

    // Method to update the bar chart
    private void updateBarChart() {
        // Set the colors for the labels and grid lines
        int labelColor, gridColor;
        // Check the current mode and set the colors accordingly
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        // Check if the device is in dark mode
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode
            labelColor = getResources().getColor(R.color.white, null); // Use a white color for labels in dark mode
            gridColor = getResources().getColor(R.color.gray, null); // Use a gray color for grid lines in dark mode
        } else {
            // Light mode
            labelColor = getResources().getColor(R.color.black, null); // Use a black color for labels in light mode
            gridColor = getResources().getColor(R.color.light_gray, null); // Use a light gray color for grid lines in light mode
        }

        // Set the description of the bar chart
        Description description = new Description();
        description.setText("Medication History");
        description.setPosition(150f, 15f);
        description.setTextColor(labelColor);
        barChart.setDescription(description);

        // Disable the right y-axis
        barChart.getAxisRight().setEnabled(false);
        // Set the x-axis values
        List<String> xAxisValues = Arrays.asList("paracetamol", "ibuprofen", "aspirin", "vitamin c", "vitamin d", "vitamin e", "vitamin b");
        // Get the x-axis of the bar chart
        XAxis xAxis = barChart.getXAxis();
        // Set the position of the x-axis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // Set the x-axis values
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        // Set the label count of the x-axis
        xAxis.setLabelCount(xAxisValues.size());
        // Set the granularity of the x-axis
        xAxis.setGranularity(1f);
        // Apply the colors to the x-axis
        xAxis.setTextColor(labelColor);
        xAxis.setGridColor(gridColor);

        // Apply the colors to the left y-axis
        YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setTextColor(labelColor);
        leftYAxis.setGridColor(gridColor);

        // Create a new bar data set
        BarDataSet barDataSet = new BarDataSet(barChartData(), "Medication History");
        // Apply the colors to the bar data set
        barDataSet.setValueTextColor(labelColor);
        // Create a new bar data
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        // Set the bar data to the bar chart
        barChart.setData(barData);
        // Invalidate the bar chart
        barChart.invalidate();
    }

    // Method to populate the bar chart
    private ArrayList<BarEntry> barChartData(){
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(1, 10));
        data.add(new BarEntry(2, 20));
        data.add(new BarEntry(3, 10));
        data.add(new BarEntry(4, 40));
        data.add(new BarEntry(5, 50));
        data.add(new BarEntry(6, 20));
        data.add(new BarEntry(7, 70));
        return data;
    }
}