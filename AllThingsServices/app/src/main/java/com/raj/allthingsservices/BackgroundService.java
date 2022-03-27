package com.raj.allthingsservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BackgroundService extends Service {
    public static final String TAG = "BackgroundService";

    //onCreate is triggered the FIRST time we create our service
    @Override
    public void onCreate() {
        super.onCreate();
    }

    //The onStartCommand method is triggered when we start our service.. That is whenever we call startService
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Run the background service stuff in a new thread so that we dont clog up the UI thread with intensive tasks
        new Thread(
                //Define and pass a runnable to thread... Cuz runnables are portable!
                new Runnable() {
                    @Override
                    public void run() {
                        //Essentially all we are doing in this runnable is just printing to logcat... Just to simulate intensive background tasks
                        Log.d(TAG, "Starting background service inside runnable in another thread...");
                        Log.d(TAG, "Doing some intensive work in background service..."); //Intensive work to be done in background service goes here
                        Log.d(TAG, "Background service has ended");
                    }
                }
        ).start();

        return super.onStartCommand(intent, flags, startId);
    }

    //This method is called when stopService is called
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //This method just has to be there but we wont use it... A null method... This method is for bound services!!!
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
