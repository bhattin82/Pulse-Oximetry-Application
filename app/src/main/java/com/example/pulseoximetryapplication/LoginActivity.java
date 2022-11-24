package com.example.pulseoximetryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Method call to establish a bluetooth connection with device
        try {
            BluetoothData establishConnection = BluetoothData.getInstance();
            establishConnection.EstablishBluetoothConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method authenticates the credentials.
    public void CheckCredentials(View view) throws IOException {

        // Identifies relevant id in xml file
        TextView enteredUsername = findViewById(R.id.usernamebox);
        TextView enteredPassword = findViewById(R.id.passwordbox);
        int heartRateReading = 0;
        int bloodOxygenReading = 0;
        int batteryPercentReading = 0;
        int panicButtonReading = 0;

        /* If the correct credentials are entered, a success toast pops up.
        The user is transitioned to the mode selection page.
        The username and password fields are cleared. */
        if ((enteredUsername.getText().toString().equals(BuildConfig.correctUsername)) && (enteredPassword.getText().toString().equals(BuildConfig.correctPassword))) {
            Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            BluetoothData modeReadings = BluetoothData.getInstance();
            List<String> sensorMeasurements = modeReadings.SensorModeReadings();
            Intent selectMode = new Intent(LoginActivity.this, ModeSelection.class);
            //Intent selectMode2 = new Intent(LoginActivity.this, PanicButton.class);

            heartRateReading = Integer.parseInt(sensorMeasurements.get(1));
            bloodOxygenReading = Integer.parseInt(sensorMeasurements.get(3));
            batteryPercentReading = Integer.parseInt(sensorMeasurements.get(5));
            panicButtonReading = Integer.parseInt(sensorMeasurements.get(7));
            selectMode.putExtra("HeartRateReading", heartRateReading);
            selectMode.putExtra("BloodOxygenLevelReading", bloodOxygenReading);
            selectMode.putExtra("BatteryPercentReading", batteryPercentReading);
            startActivity(selectMode);

            enteredUsername.setText("");
            enteredPassword.setText("");
        }

        /* If the incorrect credentials are entered, an "invalid login" toast pops up.
        The user can attempt again. */
        else {
            Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
        }
    }
}

