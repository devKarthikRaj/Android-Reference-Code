package com.raj.allthingsservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnForegroundService;
    Button btnBackgroundService;
    Button btnBoundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnForegroundService = findViewById(R.id.BTN_foreground_service);
        btnBackgroundService = findViewById(R.id.BTN_background_service);
        btnBoundService = findViewById(R.id.BTN_bound_service);

        btnForegroundService.setOnClickListener(this);
        btnBackgroundService.setOnClickListener(this);
        btnBoundService.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_foreground_service:
                startActivity(new Intent(MainActivity.this, ForegroundServiceActivity.class));
                break;
            case R.id.BTN_background_service:
                startActivity(new Intent(MainActivity.this, BackgroundServiceActivity.class));
                break;
            case R.id.BTN_bound_service:
                startActivity(new Intent(MainActivity.this, BoundServiceActivity.class));
                break;
        }
    }
}











