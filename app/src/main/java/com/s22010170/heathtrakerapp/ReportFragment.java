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

        return view;
    }

    // Method to update the line chart
    private void updateLineChart(){
        //set the description of the line chart
        Description description = new Description();
        description.setText("Trend of Health Vitals");
        description.setPosition(150f, 15f);
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
        // Create a new line data set
        LineDataSet lineDataSet = new LineDataSet(lineChartData(), "Trend of Health Vitals");

        // Set the color of the line
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

        // Apply the colors to the x-axis
        xAxis.setTextColor(labelColor);
        xAxis.setGridColor(gridColor);

        // Apply the colors to the left y-axis
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setTextColor(labelColor);
        leftYAxis.setGridColor(gridColor);

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
}