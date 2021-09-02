package com.raj.allthingsbroadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

/* Implicit Broadcast: Whatever component sends this broadcast doesn't specify this broadcast directly by its class name, instead specifies it
 * by an action... We listen to this action and react to it (This definition is from coding in flow broadcast receiver youtube series)
 *
 * Implicit Broadcasts are not recommended by android so don't use em! So nothing on those nasty implicit broadcasts here!
 */
public class BroadcastReceivers extends BroadcastReceiver {
    //This method will run when the broadcast receiver is triggered...
    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if(noConnectivity) {
                Toast.makeText(context, "Disconnected",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Connected",Toast.LENGTH_SHORT).show();
            }
        }
    }
}