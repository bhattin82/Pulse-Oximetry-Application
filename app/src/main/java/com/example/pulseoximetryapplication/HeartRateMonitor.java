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

public class HeartRateMonitor extends AppCompatActivity {

    public final int UPPER_SAFETY_THRESHOLD = 118;
    public final int LOWER_SAFETY_THRESHOLD = 75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heart_rate_monitor);

        LocalBroadcastManager.getInstance(this).registerReceiver(heartRateMessageReceiver, new IntentFilter("VitalHealthInformation"));
    }

    @SuppressLint("DefaultLocale")
    public void DisplayHeartReading(int heartRate) {

        TextView heartRateMeasurement = findViewById(R.id.heartratereading);
        TextView childSafety = findViewById(R.id.safetytext);
        View heartMonitorView = findViewById(R.id.heartrateview);

        if ((heartRate >= LOWER_SAFETY_THRESHOLD) && (heartRate <= UPPER_SAFETY_THRESHOLD)) {
            heartMonitorView.setBackgroundColor(Color.rgb(13, 180, 10));
            childSafety.setText("Safe");
        } else {
            heartMonitorView.setBackgroundColor(Color.rgb(212, 30, 18));
            childSafety.setText("Unsafe");
        }

        String heartRateSensorData = String.format("Heart Rate Reading: %d bpm", heartRate);
        heartRateMeasurement.setText(heartRateSensorData);
    }

    private BroadcastReceiver heartRateMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int heartRateInformation = intent.getIntExtra("HeartRateReading",0);
            DisplayHeartReading(heartRateInformation);
        }
    };
}