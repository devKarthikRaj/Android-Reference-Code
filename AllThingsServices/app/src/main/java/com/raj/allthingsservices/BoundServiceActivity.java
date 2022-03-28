package com.raj.allthingsservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.raj.allthingsservices.BoundService.boundServiceLocalBinder; //Import the local binder class from the bound service class

public class BoundServiceActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvCurrentTimeDisplay;
    Button btnGetCurrentTime;

    BoundService boundService;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_service);

        tvCurrentTimeDisplay = findViewById(R.id.TV_Current_Time);
        btnGetCurrentTime = findViewById(R.id.BTN_Get_Current_Time);

        btnGetCurrentTime.setOnClickListener(this);

        Intent serviceIntent = new Intent(this, BoundService.class); //Create a service intent for the bound service
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE); //Bind with the bound service
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_Get_Current_Time:
                showTime(view);
                break;
        }
    }

    //We need the service connection class to tell Android what to do when we connect to the bound service and disconnect from the bound service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            boundServiceLocalBinder localBinder = (boundServiceLocalBinder) iBinder; //Get the local binder from the bound service class
            boundService = localBinder.getService(); //Call getService in the local binder class that we got above to get access to the entire bound service class
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    public void showTime(View view) {
        String currentTime = boundService.getCurrentTime(); //Get current time from the getCurrentTime method inside the bound service
        tvCurrentTimeDisplay.setText(currentTime);
    }
}












