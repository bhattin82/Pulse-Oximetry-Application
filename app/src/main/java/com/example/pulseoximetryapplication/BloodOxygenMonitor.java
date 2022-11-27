package com.example.pulseoximetryapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BloodOxygenMonitor extends AppCompatActivity {

    public final int UPPER_SAFETY_THRESHOLD = 100;
    public final int LOWER_SAFETY_THRESHOLD = 95;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_oxygen_monitor);

        LocalBroadcastManager.getInstance(this).registerReceiver(bloodOxygenMessageReceiver, new IntentFilter("VitalHealthInformation"));
    }

    @SuppressLint("DefaultLocale")
    public void DisplayBloodOxygenReading(int bloodOxygenLevel) {

        TextView bloodOxygenMeasurement = findViewById(R.id.bloodoxygenreading);
        TextView childSafety = findViewById(R.id.safetext);
        View bloodOxygenMonitorView = findViewById(R.id.bloodoxygenview);

        if ((bloodOxygenLevel >= LOWER_SAFETY_THRESHOLD) && (bloodOxygenLevel <= UPPER_SAFETY_THRESHOLD)) {
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

    private BroadcastReceiver bloodOxygenMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int bloodOxygenInformation = intent.getIntExtra("BloodOxygenReading",0);
            DisplayBloodOxygenReading(bloodOxygenInformation);
        }
    };
}