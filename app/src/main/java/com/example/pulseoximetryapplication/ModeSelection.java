// This creates the Mode Selection Activity.
// It gives the option of choosing either the heart rate or blood oxygen level

package com.example.pulseoximetryapplication;

// Import classes/libraries/packages
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ModeSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_selection);

        // Register broadcast receiver when activity starts
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(panicAndBatteryMessageReceiver, new IntentFilter("VitalHealthInformation"));
    }

    // This method transitions the user to the heart rate monitor page
    public void HeartRateMode(View view) {
        Intent heartRateMonitor = new Intent(ModeSelection.this, HeartRateMonitor.class);
        startActivity(heartRateMonitor);
    }

    // This method transitions the user to the blood oxygen level monitor page
    public void BloodOxygenLevelMode(View view) {
        Intent bloodOxygenMonitor = new Intent(ModeSelection.this, BloodOxygenMonitor.class);
        startActivity(bloodOxygenMonitor);
    }

    // Unregister broadcast receiver when activity is destroyed by the system
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(panicAndBatteryMessageReceiver);
    }

    @SuppressLint("DefaultLocale")
    private BroadcastReceiver panicAndBatteryMessageReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Launches Panic Button Activity if panic button is pressed
            // The battery level for the band is obtained and displayed on the User Interface otherwise
            TextView batteryPercentage = findViewById(R.id.batteryleveltext);
            int bandBattery = intent.getIntExtra("BatteryReading",0);
            int panicButtonPressed = intent.getIntExtra("PanicReading",0);
            if (panicButtonPressed  == 1) {
                Intent panicMode = new Intent(getApplicationContext(), PanicButton.class);
                startActivity(panicMode);
            }
            else {
                String bandBatterySensorData = "Wrist Band Battery: " + bandBattery + "%";
                batteryPercentage.setText(bandBatterySensorData);
            }
        }
    };
}