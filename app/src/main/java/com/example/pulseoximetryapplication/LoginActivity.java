package com.example.pulseoximetryapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println(adapter.getBondedDevices());

    }

    public void CheckCredentials(View view) {
        TextView enteredUsername = findViewById(R.id.username);
        TextView enteredPassword = findViewById(R.id.password);
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
