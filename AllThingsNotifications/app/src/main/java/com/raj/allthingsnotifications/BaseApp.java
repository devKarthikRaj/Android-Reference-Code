package com.raj.allthingsnotifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

//This class runs at the start of the app's lifecycle (this is defined in the manifest
//We are starting all the notification channels that we want to use in this app here... at the start of the app's lifecycle (its recc this way)

public class BaseApp extends Application {
    public final static String CHANNEL_1_ID = "channel1";
    public final static String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID,"Channel 1",NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is notification channel 1");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID,"Channel 2",NotificationManager.IMPORTANCE_LOW);
            channel1.setDescription("This is notification channel 2");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }
    }
}
