package com.raj.allthingsservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class BackgroundServiceActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnStartBackgroundService;
    Button btnStopBackgroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_service);

        btnStartBackgroundService = findViewById(R.id.BTN_Start_Background_Service);
        btnStopBackgroundService = findViewById(R.id.BTN_Stop_Background_Service);

        btnStartBackgroundService.setOnClickListener(this);
        btnStopBackgroundService.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_Start_Background_Service:
                startBackgroundService();
                break;
            case R.id.BTN_Stop_Background_Service:
                stopBackgroundService();
                break;
        }
    }

    public void startBackgroundService() {
        //With the service intent... we can run or stop our service class!
        Intent serviceIntent = new Intent(this, BackgroundService.class);
        startService(serviceIntent);
    }

    //The background service runs really fast so we won't need to use the stopBackgroundService method
    //But in an actual app... Background service may run for hours on end... So this stopBackgroundService method will come in handy then!
    public void stopBackgroundService() {
        Intent serviceIntent = new Intent(this, BackgroundService.class);
        stopService(serviceIntent);
    }
}