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
 * When wifi is turned on or off... toast is triggered by an implicit system broadcast receiver registered dynamically
 * When green button is pressed... toast is triggered by an implicit custom broadcast receiver registered dynamically
 * When orange button is pressed... toast is triggered by an explicit custom broadcast receiver registered dynamically
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

    Button sendImplicitCustomBroadcastBtn;
    Button sendExplicitCustomBroadcastBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendImplicitCustomBroadcastBtn = findViewById(R.id.BTN_Send_Implicit_Custom_Broadcast);
        sendImplicitCustomBroadcastBtn.setOnClickListener(this);

        sendExplicitCustomBroadcastBtn = findViewById(R.id.BTN_Send_Explicit_Custom_Broadcast);
        sendExplicitCustomBroadcastBtn.setOnClickListener(this);

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
        }
    }

    //This method can also be put in another app... and send this broadcast with the same action name from other app and receive the broadcast in this app.. Inter-phone comms!
    private void sendImplicitCustomBroadcast(View view) {
        Intent intent = new Intent("com.raj.allthingsbroadcastreceivers.EXAMPLE_ACTION");
        intent.putExtra("com.raj.allhthingsbroadcastreceivers.EXTRA_TEXT", "Implicit broadcast receiver triggered!!!");
        sendBroadcast(intent);
    }

    private void sendExplicitCustomBroadcast(View view) {
        Intent intent = new Intent(this, ExplicitCustomBroadcastReceiver.class);
        intent.putExtra("com.raj.allhthingsbroadcastreceivers.EXTRA_TEXT", "Explicit broadcast receiver triggered!!!");
        sendBroadcast(intent);

    }
}