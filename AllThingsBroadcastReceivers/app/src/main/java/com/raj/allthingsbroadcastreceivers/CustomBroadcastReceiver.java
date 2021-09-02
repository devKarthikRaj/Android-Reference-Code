package com.raj.allthingsbroadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

//This is a custom broadcast receiver... you can send a broadcast for anything that's not already available as a system broadcast!
public class CustomBroadcastReceiver extends BroadcastReceiver {
    @Override
    //This method will run when custom broadcast receiver is triggered...
    public void onReceive(Context context, Intent intent) {
        //if custom broadcast receiver is triggered... this is just a secondary to check to make sure custom broadcast receiver has been triggered...
        if("com.raj.allthingsbroadcastreceivers.EXAMPLE_ACTION".equals(intent.getAction())) {
            //Get the extra text from the custom broadcast receiver
            String receivedText = intent.getStringExtra("com.raj.allhthingsbroadcastreceivers.EXTRA_TEXT");
            Toast.makeText(context, receivedText, Toast.LENGTH_SHORT).show();
        }
    }
}
