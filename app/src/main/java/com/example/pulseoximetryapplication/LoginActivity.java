package com.example.pulseoximetryapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        EstablishBluetoothConnection();
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
            LocalBroadcastManager.getInstance(this).registerReceiver(healthInformationMessageReceiver, new IntentFilter("VitalHealthInformation"));
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

    public void EstablishBluetoothConnection() {

        // Declare variables
        String esp32Address = "C4:DD:57:CA:D1:46";

        // Obtain the universally unique identifier for bluetooth
        // Obtain the data for the bluetooth hardware
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice esp32Microcontroller = adapter.getRemoteDevice(esp32Address);
        ConnectThread bluetoothThread = new ConnectThread(esp32Microcontroller);
        bluetoothThread.start();
    }

    private BroadcastReceiver healthInformationMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("VitalHealthInformation")) {
                int panicButtonPressed = intent.getIntExtra("PanicReading",0);
                if (panicButtonPressed  == 1) {
                    Intent panicMode = new Intent(LoginActivity.this, PanicButton.class);
                    startActivity(panicMode);
                }
            }
        }
    };

    private class ConnectThread extends Thread {
        private final UUID uniqueIdentifier = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        private final BluetoothSocket bluetoothSocket;

        @SuppressLint("MissingPermission")
        public ConnectThread(BluetoothDevice btdevice) {
            BluetoothSocket btSocket = null;
            try {
                btSocket = btdevice.createRfcommSocketToServiceRecord(uniqueIdentifier);
            } catch (IOException socketException) {
                System.out.println("Bluetooth Socket Creation Unsuccessful");
            }
            bluetoothSocket = btSocket;
        }

        @SuppressLint("MissingPermission")
        public void run() {
            try {
                bluetoothSocket.connect();
                if (!bluetoothSocket.isConnected()) {
                    Toast.makeText(LoginActivity.this, "Please check your Bluetooth Connection", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException connectException) {
                try {
                    bluetoothSocket.close();
                } catch (IOException closeException) {
                   System.out.println("Unable to close client socket");
                }
            }

            ConnectedThread connectedThread = new ConnectedThread(bluetoothSocket);
            connectedThread.start();
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final int RECEIVEDMESSAGE = 1;

        public ConnectedThread(BluetoothSocket btsocket) {
            bluetoothSocket = btsocket;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while (true) {
                try {
                    InputStream readData = bluetoothSocket.getInputStream();
                    bytesRead = readData.read(buffer);
                    messageHandler.obtainMessage(1, bytesRead, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        Handler messageHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                byte[] readBuffer = (byte[]) msg.obj;

                if (msg.what == RECEIVEDMESSAGE) {

                    String sensorReadings = new String(readBuffer, 0, msg.arg1);
                    String[] sensorData = sensorReadings.split("\\W+");
                    List<String> sensorMeasurements = Arrays.asList(sensorData);

                    if (!sensorMeasurements.isEmpty()) {
                        try {
                            int heartRateReading = Integer.parseInt(sensorMeasurements.get(1));
                            int bloodReading = Integer.parseInt(sensorMeasurements.get(3));
                            int batteryReading = Integer.parseInt(sensorMeasurements.get(5));
                            int panicReading = Integer.parseInt(sensorMeasurements.get(7));
                            Intent healthInformationIntent = new Intent("VitalHealthInformation");
                            healthInformationIntent.putExtra("HeartRateReading", heartRateReading);
                            healthInformationIntent.putExtra("BloodOxygenReading", bloodReading);
                            healthInformationIntent.putExtra("BatteryReading", batteryReading);
                            healthInformationIntent.putExtra("PanicReading", panicReading);
                            LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(healthInformationIntent);
                        }
                        catch (NumberFormatException numberFormatException) {
                            System.out.println("String to be converted does not have appropriate format");
                        }
                    }
                }
            }
        };
    }
}

