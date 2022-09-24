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

    public void HeartRateMode(View view) {
        Intent heartRateMonitor = new Intent(ModeSelection.this, HeartRateMonitor.class);
        startActivity(heartRateMonitor);
    }

    public void BloodOxygenLevelMode(View view) {
        Intent bloodOxygenLevelMonitor = new Intent(ModeSelection.this, BloodOxygenMonitor.class);
        startActivity(bloodOxygenLevelMonitor);
    }
}