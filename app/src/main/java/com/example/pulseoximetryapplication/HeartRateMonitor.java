package com.example.pulseoximetryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HeartRateMonitor extends AppCompatActivity {

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heart_rate_monitor);

        TextView heartRateMeasurement = findViewById(R.id.heartratereading);
        TextView childSafety = findViewById(R.id.safetytext);
        View heartMonitorView = findViewById(R.id.heartrateview);
        int upperSafetyThreshold = 110;
        int lowerSafetyThreshold = 60;

        Intent heartData = getIntent();
        int heartRate = heartData.getIntExtra("HeartRateReading", 0);
        int battery = heartData.getIntExtra("BatteryPercentReading", 0);

        // Create new method(x) in this class
           // Add a delay for loop (singleton)
           // Call SensorMode method after delay (after for loop in same method)
           // filter value
           // if filter value not equal to heart rate, update reading

        // Put if logic in a new method

        // Create while loop that keeps calling method(x)

        if ((heartRate >= lowerSafetyThreshold) && (heartRate <= upperSafetyThreshold)) {
            heartMonitorView.setBackgroundColor(Color.rgb(13,180,10));
            childSafety.setText("Safe");
        }
        else {
            heartMonitorView.setBackgroundColor(Color.rgb(212,30,18));
            childSafety.setText("Unsafe");
        }
        String heartRateSensorData = String.format("Heart Rate Reading: %d bpm", heartRate);
        heartRateMeasurement.setText(heartRateSensorData);
    }
}