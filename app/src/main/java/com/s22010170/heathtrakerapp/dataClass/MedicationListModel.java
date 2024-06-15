package com.s22010170.heathtrakerapp.dataClass;

public class MedicationListModel {
    int medicationId;
    String medicationName;
    String medicationDosage;
    String medicationTime;
    String medicationFrequency;

    public MedicationListModel(int medicationId, String medicationName, String medicationDosage, String medicationTime, String medicationFrequency) {
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.medicationDosage = medicationDosage;
        this.medicationTime = medicationTime;
        this.medicationFrequency = medicationFrequency;
    }

    public int getMedicationId() {
        return medicationId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public String getMedicationDosage() {
        return medicationDosage;
    }

    public String getMedicationTime() {
        return medicationTime;
    }

    public String getMedicationFrequency() {
        return medicationFrequency;
    }
}
