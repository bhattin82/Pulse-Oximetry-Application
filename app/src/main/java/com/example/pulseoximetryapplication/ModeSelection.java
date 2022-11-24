package com.example.pulseoximetryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ModeSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_selection);
    }

    // This method transitions the user to the heart rate monitor page
    public void HeartRateMode(View view) {
        Intent heartPulse = getIntent();
        int heartRate = heartPulse.getIntExtra("HeartRateReading", 0);
        int battery = heartPulse.getIntExtra("BatteryPercentReading", 0);
        Intent heartRateMonitor = new Intent(ModeSelection.this, HeartRateMonitor.class);
        heartRateMonitor.putExtra("HeartRateReading", heartRate);
        heartRateMonitor.putExtra("BatteryPercentReading", battery);
        startActivity(heartRateMonitor);
    }

    // This method transitions the user to the blood oxygen level monitor page
    public void BloodOxygenLevelMode(View view) {
        Intent bloodoxygen = getIntent();
        int bloodoxygenlevel = bloodoxygen.getIntExtra("BloodOxygenLevelReading", 0);
        int battery = bloodoxygen.getIntExtra("BatteryPercentReading", 0);
        Intent bloodOxygenMonitor = new Intent(ModeSelection.this, BloodOxygenMonitor.class);
        bloodOxygenMonitor.putExtra("BloodOxygenLevelReading", bloodoxygenlevel);
        bloodOxygenMonitor.putExtra("BatteryPercentReading", battery);
        startActivity(bloodOxygenMonitor);
    }
}