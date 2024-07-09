package com.s22010170.heathtrakerapp.utils;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class MedicationXAxisFormatter extends ValueFormatter {

    private final List<String> medicationNames;

    public MedicationXAxisFormatter(List<String> medicationNames) {
        this.medicationNames = medicationNames;
    }

    @Override
    public String getFormattedValue(float value) {
        int index = (int) value;
        if (index >= 0 && index < medicationNames.size()) {
            return medicationNames.get(index);
        } else {
            return "";
        }
    }
}
