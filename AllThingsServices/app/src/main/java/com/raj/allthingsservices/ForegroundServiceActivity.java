package com.raj.allthingsservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForegroundServiceActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etForegroundServiceNotifText;
    Button btnStartForegroundService;
    Button btnStopForegroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreground_service);

        etForegroundServiceNotifText = findViewById(R.id.ET_Foreground_Service_Notification_Text);
        btnStartForegroundService = findViewById(R.id.BTN_Start_Foreground_Service);
        btnStopForegroundService = findViewById(R.id.BTN_Stop_Foreground_Service);

        btnStartForegroundService.setOnClickListener(this);
        btnStopForegroundService.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_Start_Foreground_Service:
                startService(view);
                break;
            case R.id.BTN_Stop_Foreground_Service:
                stopService(view);
                break;
        }
    }

    //This method starts the foreground service and passes a string to the service
    //Passing a string is not necessary... It is done here just to demonstrate how to do so if required
    public void startService(View v) {
        String input = etForegroundServiceNotifText.getText().toString();

        //With the service intent... we can run or stop our service class!
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", input);

        //After calling the below method... we have 5 seconds to call startForeground() method in the ForegroundService class
        //If we take more than 5 seconds to call startForground()... Android will kill the service!!!
        //Then why use startForegroundService? cuz it can be called when the app is running in the background too... So its rec to use it
        //Our other option is startService but if you somehow by mistake try to call startService from the background... It'll throw an
        //illegal state exception and BOOM... app will crash!!!
        // So just use startForegroundService instead !!!
        //BUT this startForegroundService method is available only from API 26 and above...
        //So we use ContextCompat.startForegroundService... Which is a convenience method... It checks if the API level...
        //If API level is 26 or more than 26... Then startForegroundService will be used... If not then startService will be used
        //Orite end of long story... Thanks for reading :)
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }
}