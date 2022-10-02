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