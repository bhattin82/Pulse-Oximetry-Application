// This creates the Panic Button Activity.
// It displays the following text: "Immediate Attention Required! Panic Button Pressed!"

package com.example.pulseoximetryapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PanicButton extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panic_button);
    }
}