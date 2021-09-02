package com.raj.allthingsbroadcastreceivers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    BroadcastReceivers broadcastReceivers = new BroadcastReceivers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //onStart() will run when app comes into foreground in the android app lifecycle
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceivers, intentFilter);
    }

    //onStop() will run when app goes into background in the android app lifecycle
    @Override
    protected void onStop() {
        super.onStop();
        //unregistering the receiver is very impt... Do not forget to unregister the receiver!
        //If receiver is not unregistered... This app will WASTE the phone's resources such as battery life
        unregisterReceiver(broadcastReceivers);
    }
}