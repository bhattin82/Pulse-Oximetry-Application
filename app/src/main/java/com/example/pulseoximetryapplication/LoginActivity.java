package com.example.pulseoximetryapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        /*BluetoothSocket m = null;
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice h = adapter.getRemoteDevice("18:54:CF:90:72:EF");
        try {
            m  = h.createRfcommSocketToServiceRecord(uuid);
            m.connect();
            System.out.println(m.isConnected());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void CheckCredentials(View view) {
        TextView enteredUsername = findViewById(R.id.usernamebox);
        TextView enteredPassword = findViewById(R.id.passwordbox);
        String correctUsername = "Stacy007";
        String correctPassword = "health";
        if ((enteredUsername.getText().toString().equals(correctUsername)) && (enteredPassword.getText().toString().equals(correctPassword))) {
            Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            Intent selectMode = new Intent(LoginActivity.this, ModeSelection.class);
            startActivity(selectMode);
            enteredUsername.setText("");
            enteredPassword.setText("");
        }
        else {
            Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
        }
    }
}
