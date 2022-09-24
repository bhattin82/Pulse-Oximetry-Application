package com.example.pulseoximetryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        TextView enteredUsername = findViewById(R.id.username);
        TextView enteredPassword = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.button);
        String correctUsername = "Stacy007";
        String correctPassword = "health";

        loginButton.setOnClickListener(v -> {
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
        });
    }
}