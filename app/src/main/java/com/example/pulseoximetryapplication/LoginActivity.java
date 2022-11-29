// This creates the login activity page.
// It also establishes bluetooth communication between the client and server.

package com.example.pulseoximetryapplication;

// Imports classes/packages/libraries
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

        BluetoothConnection();
    }

    // This method authenticates the credentials.
    public void CheckCredentials(View view) {

        // Identifies view with respect to the id in the xml file
        TextView enteredUsername = findViewById(R.id.usernamebox);
        TextView enteredPassword = findViewById(R.id.passwordbox);

        /* If the correct credentials are entered, a success toast pops up.
        The user is transitioned to the mode selection page.
        The username and password fields are cleared. */
        if ((enteredUsername.getText().toString().equals(BuildConfig.correctUsername)) && (enteredPassword.getText().toString().equals(BuildConfig.correctPassword))) {

            Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();

            // Register broadcast receiver when activity starts
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(healthInformationMessageReceiver, new IntentFilter("VitalHealthInformation"));

            Intent selectMode = new Intent(getApplicationContext(), ModeSelection.class);
            startActivity(selectMode);
            enteredUsername.setText("");
            enteredPassword.setText("");
        }

        /* If the incorrect credentials are entered, an "invalid login" toast pops up.
        The user can attempt again. */
        else {
            Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_SHORT).show();
        }
    }

    // This method tries to establish a bluetooth connection with the microcontroller and the Android application
    public void BluetoothConnection() {

        // Declare variable
        String esp32Address = "C4:DD:57:CA:D1:46";

        // Creates a bluetooth adapter and bluetooth device object.
        // If adapter returns null, then the device does not support bluetooth.
        // At this point, the selected device for communication is the esp32 microcontoller.
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice esp32Microcontroller = adapter.getRemoteDevice(esp32Address);

        // A new thread is created for bluetooth and begins execution
        ConnectThread bluetoothThread = new ConnectThread(esp32Microcontroller);
        bluetoothThread.start();
    }

    // Unregister broadcast receiver when activity is no longer visible
    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(healthInformationMessageReceiver);
    }

    // Unregister broadcast receiver when activity is destroyed by the system
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(healthInformationMessageReceiver);
    }

    // Broadcast onReceive method runs on main (UI) thread.
    private BroadcastReceiver healthInformationMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Receives data associated with the intent "VitalHealthInformation"
            // Launches Panic Button Activity if panic button is pressed
            if (intent.getAction().equals("VitalHealthInformation")) {
                int panicButtonPressed = intent.getIntExtra("PanicReading",0);
                if (panicButtonPressed  == 1) {
                    Intent panicMode = new Intent(getApplicationContext(), PanicButton.class);
                    startActivity(panicMode);
                }
            }
        }
    };

    // The ConnectThread class is to create a new thread besides the main (UserInterface) thread
    private class ConnectThread extends Thread {

        // Declare variables/constants
        private final UUID uniqueIdentifier = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        private final BluetoothSocket bluetoothSocket;

        // Class Constructor
        @SuppressLint("MissingPermission")
        public ConnectThread(BluetoothDevice btdevice) {

            // A bluetooth socket is created that allows the client (Android Application) to connect to the server (esp32 microcontroller)
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

            // A connection is made through the bluetooth socket
            try {
                bluetoothSocket.connect();

                // A toast pops up if connection is unsuccessful
                if (!bluetoothSocket.isConnected()) {
                    Toast.makeText(getApplicationContext(), "Please check your Bluetooth Connection", Toast.LENGTH_SHORT).show();
                }

                // If connection is not successful, then close the bluetooth socket
            } catch (IOException connectException) {
                try {
                    bluetoothSocket.close();
                } catch (IOException closeException) {
                    System.out.println("Unable to close client socket");
                }
            }

            // A new thread is created for data transfer via bluetooth
            ConnectedThread connectedThread = new ConnectedThread(bluetoothSocket);
            connectedThread.start();
        }
    }

    // The ConnectedThread class creates a new thread for data transfer.
    private class ConnectedThread extends Thread {

        // Declare variables/constants
        private final BluetoothSocket bluetoothSocket;

        // Class constructor
        public ConnectedThread(BluetoothSocket btsocket) {
            bluetoothSocket = btsocket;
        }

        public void run() {

            // Declare variables/constants
            byte[] buffer = new byte[1024];
            int bytesRead;

            // The input stream reads the data and the handler is invoked
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

        public void cancel() throws IOException {
            bluetoothSocket.close();
        }

    }

    // Handler used to update main (UI) thread
    Handler messageHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            final int RECEIVED_MESSAGE = 1;
            byte[] readBuffer = (byte[]) msg.obj;

            if (msg.what == RECEIVED_MESSAGE) {

                // Create a java string and split according to non-alphanumeric terms
                // Convert the split data to a list
                String sensorReadings = new String(readBuffer, 0, msg.arg1);
                String[] sensorData = sensorReadings.split("\\W+");
                List<String> sensorMeasurements = Arrays.asList(sensorData);

                // Execute if statement only if updated list is not empty
                if (!sensorMeasurements.isEmpty()) {
                    try {

                        //  Filter the vital health information from the sensorMeasurements list
                        int heartRateReading = Integer.parseInt(sensorMeasurements.get(1));
                        int bloodReading = Integer.parseInt(sensorMeasurements.get(3));
                        int batteryReading = Integer.parseInt(sensorMeasurements.get(5));
                        int panicReading = Integer.parseInt(sensorMeasurements.get(7));

                        // Create an intent to pass vital health information
                        Intent healthInformationIntent = new Intent("VitalHealthInformation");
                        healthInformationIntent.putExtra("HeartRateReading", heartRateReading);
                        healthInformationIntent.putExtra("BloodOxygenReading", bloodReading);
                        healthInformationIntent.putExtra("BatteryReading", batteryReading);
                        healthInformationIntent.putExtra("PanicReading", panicReading);

                        // Send Broadcast each time data is updated
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(healthInformationIntent);
                    }
                    catch (NumberFormatException numberFormatException) {
                        System.out.println("String to be converted does not have appropriate format");
                    }
                }
            }
        }
    };
}