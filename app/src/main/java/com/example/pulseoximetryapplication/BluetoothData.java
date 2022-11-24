package com.example.pulseoximetryapplication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BluetoothData {
    private static BluetoothData instance;
    BluetoothSocket bluetoothSocket = null;
    InputStream readData = null;

    private BluetoothData() {};

    public static BluetoothData getInstance() {
        if (instance == null) {
            instance = new BluetoothData();
        }
        return instance;
    }

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
            bluetoothSocket = esp32Microcontroller.createRfcommSocketToServiceRecord(uniqueIdentifier);
            bluetoothSocket.connect();
            if (bluetoothSuccess == Boolean.compare(true, bluetoothSocket.isConnected())) {
                connectedToMicrocontroller = true;
                System.out.println(bluetoothSocket.isConnected());
            }
        }
    }

    public List<String> SensorModeReadings() throws IOException {
        byte[] buffer = new byte[1024];
        readData = bluetoothSocket.getInputStream();
        int bytesRead = readData.read(buffer);
        String sensorReading = new String(buffer, 0, bytesRead);
        String[] sensorData = sensorReading.split("\\W+");
        return Arrays.asList(sensorData);
    }
}
