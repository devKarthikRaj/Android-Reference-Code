package com.raj.allthingsbroadcastreceivers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/* Broadcast receivers can be implicit or explicit
 * Implicit: Implicit ~ implied ~ suggested but not directly expressed...
 *           Target all apps... Ex: System broadcasts!
 *           Implicit broadcasts are not recommended by Android! Especially, do not broadcast sensitive info with implicit! Try not to use em!
*            Its not not good to send sensitive stuff through implicit because all apps can access this implicit info
 * Explicit: Explicit ~ stated clearly
 *           Target our app
 *
 * Broadcast receivers can be "REGISTERED" as static or dynamic:
 * Static: Registered in the manifest file with an action specified
 * Dynamic: Registered during the lifecycle of the app in onCreate, onStart and so on!
 */

/* Stuff that is going on in this project...
 * 1 -> When wifi is turned on or off... toast is triggered by an implicit system broadcast receiver registered dynamically
 * 2 -> When green button is pressed... toast is triggered by an implicit custom broadcast receiver registered dynamically
 * 3 -> When orange button is pressed... toast is triggered by an explicit custom broadcast receiver registered dynamically
 * 4 -> When red button is pressed... A chain of toasts are triggered by an ordered broadcast registered dynamically
 *
 * Local broadcast manager is deprecated and so, its not here!
 */

/* Rule of thumb for DYNAMIC broadcast receiver (applicable to most cases... not all cases... this is not universal)
 * Where to register a dynamic broadcast?
 *  If dynamic broadcast receiver is registered in onStart()... unregister it in onStop()
 *  If dynamic broadcast receiver is registered in onCreate()... unregister it in onDestroy()
 *
 *  This is a just a good habit!
 */

//I know this is a lotta notes but it helps? Well, I hope it helps!

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ConnectivityStatusBroadcastReceiver connectivityStatusBroadcastReceiver = new ConnectivityStatusBroadcastReceiver();
    ImplicitCustomBroadcastReceiver customBroadcastReceiver = new ImplicitCustomBroadcastReceiver();
    OrderedBroadcastReceiver1 orderedBroadcastReceiver1 = new OrderedBroadcastReceiver1();
    OrderedBroadcastReceiver2 orderedBroadcastReceiver2 = new OrderedBroadcastReceiver2();

    Button sendImplicitCustomBroadcastBtn;
    Button sendExplicitCustomBroadcastBtn;
    Button sendOrderedBroadcastBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendImplicitCustomBroadcastBtn = findViewById(R.id.BTN_Send_Implicit_Custom_Broadcast);
        sendImplicitCustomBroadcastBtn.setOnClickListener(this);

        sendExplicitCustomBroadcastBtn = findViewById(R.id.BTN_Send_Explicit_Custom_Broadcast);
        sendExplicitCustomBroadcastBtn.setOnClickListener(this);

        sendOrderedBroadcastBtn = findViewById(R.id.BTN_Send_Ordered_Broadcast);
        sendOrderedBroadcastBtn.setOnClickListener(this);

        //Register implicit custom broadcast receiver
        IntentFilter customIntentFilter = new IntentFilter("com.raj.allthingsbroadcastreceivers.EXAMPLE_ACTION");
        registerReceiver(customBroadcastReceiver, customIntentFilter);

        //Register explicit ordered broadcast receiver 1
        IntentFilter orderedIntentFilter1 = new IntentFilter("com.raj.allthingsbroadcastreceivers.EXAMPLE_ACTION_FOR_ORDERED_BROADCAST");
        orderedIntentFilter1.setPriority(2); //Ordered receiver 1 will be triggered first (higher number = higher priority)
        registerReceiver(orderedBroadcastReceiver1, orderedIntentFilter1);

        //Register explicit ordered broadcast receiver 2
        IntentFilter orderedIntentFilter2 = new IntentFilter("com.raj.allthingsbroadcastreceivers.EXAMPLE_ACTION_FOR_ORDERED_BROADCAST");
        orderedIntentFilter2.setPriority(1); //Ordered receiver 2 will be triggered second (higher number = higher priority)
        registerReceiver(orderedBroadcastReceiver2, orderedIntentFilter2);

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
        //unregister broadcast receivers
        unregisterReceiver(customBroadcastReceiver);
        unregisterReceiver(orderedBroadcastReceiver1);
        unregisterReceiver(orderedBroadcastReceiver2);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_Send_Implicit_Custom_Broadcast:
                sendImplicitCustomBroadcast(view);
                break;

            case R.id.BTN_Send_Explicit_Custom_Broadcast:
                sendExplicitCustomBroadcast(view);
                break;

            case R.id.BTN_Send_Ordered_Broadcast:
                sendOrderedBroadcast(view);
                break;
        }
    }

    //This method can also be put in another app... and send this broadcast with the same action name from other app and receive the broadcast in this app.. Inter-phone comms!
    private void sendImplicitCustomBroadcast(View view) {
        //An implicit broadcast has an action tied to it
        Intent intent = new Intent("com.raj.allthingsbroadcastreceivers.EXAMPLE_ACTION");
        intent.putExtra("com.raj.allhthingsbroadcastreceivers.EXTRA_TEXT", "Implicit broadcast receiver triggered!!!");
        sendBroadcast(intent);
    }

    private void sendExplicitCustomBroadcast(View view) {
        //An explicit broadcast directly specifies the broadcast receiver class name
        Intent intent = new Intent(this, ExplicitCustomBroadcastReceiver.class);
        intent.putExtra("com.raj.allhthingsbroadcastreceivers.EXTRA_TEXT", "Explicit broadcast receiver triggered!!!");
        sendBroadcast(intent);
    }

    private void sendOrderedBroadcast(View view) {
        Intent intent = new Intent("com.raj.allthingsbroadcastreceivers.EXAMPLE_ACTION_FOR_ORDERED_BROADCAST");

        Bundle extras = new Bundle();
        //Send the actual data here... In the place of "Init"
        extras.putString("stringExtra", "Init");

        sendOrderedBroadcast(intent,
                               null,
                               new OrderedBroadcastChainEnd() /*you can specify a broadcast receiver here which will be triggered at the end of the chain of ordered broadcasts*/,
                               null /*you can pass a handler here if you want to execute this broadcast receiver on another thread*/,
                               0 /*just a constant that is also passed for any status indications*/,
                               "Added by init" /*just a string that is also passed for any status indications*/,
                               extras /*actual data this is to be sent over the broadcast receiver*/);
    }
}













