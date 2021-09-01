package com.raj.allthingsbroadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ImplicitStaticSystemBroadcastReceivers extends BroadcastReceiver {
    //This method will run when the broadcast receiver is triggered...
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "Boot Completed",Toast.LENGTH_SHORT).show();
        }
    }
}
