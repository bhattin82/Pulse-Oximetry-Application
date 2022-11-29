// This creates the Blood Oxygen Level Monitor Activity.
// It displays the blood oxygen level data (real time) of the person wearing the band.

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

public class BloodOxygenMonitor extends AppCompatActivity {

    // Declare Constants
    public final int UPPER_SAFETY_THRESHOLD = 100;
    public final int LOWER_SAFETY_THRESHOLD = 95;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_oxygen_monitor);

        // Register broadcast receiver when activity starts
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(bloodOxygenMessageReceiver, new IntentFilter("VitalHealthInformation"));
    }

    // This method display the real time blood oxygen level reading of the person wearing the band.
    @SuppressLint("DefaultLocale")
    public void DisplayBloodOxygenReading(int bloodOxygenLevel) {

        // This gets the view identified by the XML id.
        TextView bloodOxygenMeasurement = findViewById(R.id.bloodoxygenreading);
        TextView childSafety = findViewById(R.id.safetext);
        View bloodOxygenMonitorView = findViewById(R.id.bloodoxygenview);

        // If the values are within the safety threshold, a green background appears with the "safe" text
        // If values are not within the safety threshold, a red background appears with the "unsafe" text
        if ((bloodOxygenLevel >= LOWER_SAFETY_THRESHOLD) && (bloodOxygenLevel <= UPPER_SAFETY_THRESHOLD)) {
            bloodOxygenMonitorView.setBackgroundColor(Color.rgb(13,180,10));
            childSafety.setText("Safe");
        }
        else {
            bloodOxygenMonitorView.setBackgroundColor(Color.rgb(212,30,18));
            childSafety.setText("Unsafe");
        }

        // This displays the blood oxygen level measurement on the User Interface.
        String bloodOxygenSensorData = String.format("Blood Oxygen Reading: %d SpO2", bloodOxygenLevel);
        bloodOxygenMeasurement.setText(bloodOxygenSensorData);
    }

    // Unregister broadcast receiver when activity is destroyed by the system
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(bloodOxygenMessageReceiver);
    }

    private BroadcastReceiver bloodOxygenMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Launch Panic Button Activity if panic button pressed
            // The blood oxygen level (real time) value is obtained and the display method is called otherwise.
            int bloodOxygenInformation = intent.getIntExtra("BloodOxygenReading",0);
            int panicButtonPressed = intent.getIntExtra("PanicReading",0);
            if (panicButtonPressed  == 1) {
                Intent panicMode = new Intent(getApplicationContext(), PanicButton.class);
                startActivity(panicMode);
            }
            else {
                DisplayBloodOxygenReading(bloodOxygenInformation);
            }
        }
    };
}