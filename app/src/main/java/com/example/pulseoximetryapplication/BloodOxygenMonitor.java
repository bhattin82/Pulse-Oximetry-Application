package com.example.pulseoximetryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BloodOxygenMonitor extends AppCompatActivity {

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_oxygen_monitor);

        TextView bloodOxygenMeasurement = findViewById(R.id.bloodoxygenreading);
        TextView childSafety = findViewById(R.id.safetext);
        View bloodOxygenMonitorView = findViewById(R.id.bloodoxygenview);
        int upperSafetyThreshold = 100;
        int lowerSafetyThreshold = 95;

        Intent bloodOxygenData = getIntent();
        int bloodOxygenLevel = bloodOxygenData.getIntExtra("BloodOxygenLevelReading", 0);
        int battery = bloodOxygenData.getIntExtra("BatteryPercentReading", 0);
        if ((bloodOxygenLevel >= lowerSafetyThreshold) && (bloodOxygenLevel <= upperSafetyThreshold)) {
            bloodOxygenMonitorView.setBackgroundColor(Color.rgb(13,180,10));
            childSafety.setText("Safe");
        }
        else {
            bloodOxygenMonitorView.setBackgroundColor(Color.rgb(212,30,18));
            childSafety.setText("Unsafe");
        }
        String bloodOxygenSensorData = String.format("Blood Oxygen Reading: %d SpO2", bloodOxygenLevel);
        bloodOxygenMeasurement.setText(bloodOxygenSensorData);
    }
}