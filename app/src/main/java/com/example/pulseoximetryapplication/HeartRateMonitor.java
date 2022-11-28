// This creates the Heart Rate Monitor Activity.
// It displays the heart rate data (real time) of the person wearing the band.

package com.example.pulseoximetryapplication;

// Import classes/libraries/packages
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

    // Declare Constants
    public final int UPPER_SAFETY_THRESHOLD = 118;
    public final int LOWER_SAFETY_THRESHOLD = 75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heart_rate_monitor);

        // Register Broadcast Receiver
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(heartRateMessageReceiver, new IntentFilter("VitalHealthInformation"));
    }

    // This method display the real time heart rate reading of the person wearing the band.
    @SuppressLint("DefaultLocale")
    public void DisplayHeartReading(int heartRate) {

        // This gets the view identified by the XML id.
        TextView heartRateMeasurement = findViewById(R.id.heartratereading);
        TextView childSafety = findViewById(R.id.safetytext);
        View heartMonitorView = findViewById(R.id.heartrateview);

        // If the values are within the safety threshold, a green background appears with the "safe" text
        // If values are not within the safety threshold, a red background appears with the "unsafe" text
        if ((heartRate >= LOWER_SAFETY_THRESHOLD) && (heartRate <= UPPER_SAFETY_THRESHOLD)) {
            heartMonitorView.setBackgroundColor(Color.rgb(13, 180, 10));
            childSafety.setText("Safe");
        } else {
            heartMonitorView.setBackgroundColor(Color.rgb(212, 30, 18));
            childSafety.setText("Unsafe");
        }

        // This displays the heart rate measurement on the User Interface.
        String heartRateSensorData = String.format("Heart Rate Reading: %d bpm", heartRate);
        heartRateMeasurement.setText(heartRateSensorData);
    }

    private BroadcastReceiver heartRateMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // The heart rate (real time) value is obtained and the display method is called.
            int heartRateInformation = intent.getIntExtra("HeartRateReading",0);
            DisplayHeartReading(heartRateInformation);
        }
    };
}