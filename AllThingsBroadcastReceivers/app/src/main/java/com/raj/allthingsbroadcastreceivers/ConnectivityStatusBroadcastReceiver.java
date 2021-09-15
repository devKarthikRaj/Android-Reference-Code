package com.raj.allthingsbroadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class ConnectivityStatusBroadcastReceiver extends BroadcastReceiver {
    //This method will run when the broadcast receiver is triggered...
    @Override
    public void onReceive(Context context, Intent intent) {
        //If connectivity status of phone has changed...
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //CONNECTIVITY_ACTION only tells you if connectivity status has changed...
            //EXTRA_NO_CONNECTIVITY tells you if phone is connected or disconnected
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            //if true... not conencted!
            if(noConnectivity) {
                Toast.makeText(context, "Disconnected",Toast.LENGTH_SHORT).show();
            //if false... connected!
            } else {
                Toast.makeText(context, "Connected",Toast.LENGTH_SHORT).show();
            }
        }
    }
}