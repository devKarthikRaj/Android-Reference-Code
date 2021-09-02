package com.raj.allthingsbroadcastreceivers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/* Rule of thumb (applicable to most cases... not all cases... this is not universal)
 *  If broadcast receiver is registered in onStart()... unregister it in onStop()
 *  If broadcast receiver is registered in onCreate()... unregister it in onDestroy()
 *
 *  This is a just a good habit!
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ConnectivityStatusBroadcastReceiver connectivityStatusBroadcastReceiver = new ConnectivityStatusBroadcastReceiver();
    CustomBroadcastReceiver customBroadcastReceiver = new CustomBroadcastReceiver();

    Button sendCustomBroadcastBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendCustomBroadcastBtn = findViewById(R.id.BTN_Send_Custom_Broadcast);

        sendCustomBroadcastBtn.setOnClickListener(this);

        //Register custom broadcast receiver
        IntentFilter customIntentFilter = new IntentFilter("com.raj.allthingsbroadcastreceivers.EXAMPLE_ACTION");
        registerReceiver(customBroadcastReceiver, customIntentFilter);
    }

    //onStart() will run when app comes into foreground in the android app lifecycle
    @Override
    protected void onStart() {
        super.onStart();

        //Register connectivity status broadcast receiver
        IntentFilter connectivityStatusIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityStatusBroadcastReceiver, connectivityStatusIntentFilter);
    }

    //onStop() will run when app goes into background in the android app lifecycle
    @Override
    protected void onStop() {
        super.onStop();
        //unregistering the receiver is very impt... Do not forget to unregister the receiver!
        //If receiver is not unregistered... This app will WASTE the phone's resources such as battery life
        unregisterReceiver(connectivityStatusBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregister custom broadcast receiver
        unregisterReceiver(customBroadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_Send_Custom_Broadcast:
                sendBroadcast(view);
                break;
        }
    }

    //This method can also be put in another app... and send this broadcast with the same action name from other app and receive the broadcast in this app.. Inter-phone comms!
    private void sendBroadcast(View view) {
        Intent intent = new Intent("com.raj.allthingsbroadcastreceivers.EXAMPLE_ACTION");
        intent.putExtra("com.raj.allhthingsbroadcastreceivers.EXTRA_TEXT", "Broadcast received");
        sendBroadcast(intent);
    }
}