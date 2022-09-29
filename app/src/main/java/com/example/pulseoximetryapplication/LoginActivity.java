package com.example.pulseoximetryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Method call to establish a bluetooth connection with device
        try {
            EstablishBluetoothConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method authenticates the credentials.
    public void CheckCredentials(View view) {

        // Identifies relevant id in xml file
        TextView enteredUsername = findViewById(R.id.usernamebox);
        TextView enteredPassword = findViewById(R.id.passwordbox);

        /* If the correct credentials are entered, a success toast pops up.
        The user is transitioned to the mode selection page.
        The username and password fields are cleared. */
        if ((enteredUsername.getText().toString().equals(BuildConfig.correctUsername)) && (enteredPassword.getText().toString().equals(BuildConfig.correctPassword))) {
            Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            Intent selectMode = new Intent(LoginActivity.this, ModeSelection.class);
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

    // This method establishes a bluetooth connection with the microcontroller.
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public void EstablishBluetoothConnection() throws IOException {

        // Declare variables
        String esp32Address = "C4:DD:57:CA:D1:46";
        boolean connectedToMicrocontroller = false;
        int bluetoothSuccess = 0;

        // Obtain the universally unique identifier for bluetooth
        // Obtain the data for the bluetooth hardware
        UUID uniqueIdentifier = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice esp32Microcontroller = adapter.getRemoteDevice(esp32Address);

        // Create a communication socket until bluetooth connection with esp32(microcontroller)
        while (!connectedToMicrocontroller) {
            BluetoothSocket  bluetoothSocket = esp32Microcontroller.createInsecureRfcommSocketToServiceRecord(uniqueIdentifier);
            bluetoothSocket.connect();
            if (bluetoothSuccess == Boolean.compare(true, bluetoothSocket.isConnected())) {
                connectedToMicrocontroller = true;
            }
        }
    }
}
