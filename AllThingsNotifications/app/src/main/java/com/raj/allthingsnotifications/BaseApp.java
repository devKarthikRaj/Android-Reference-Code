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
    public final static String CHANNEL_3_ID = "channel3";
    public final static String CHANNEL_4_ID = "channel4";
    public final static String CHANNEL_5_ID = "channel5";
    public final static String CHANNEL_6_ID = "channel6";

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

            NotificationChannel channel3 = new NotificationChannel(CHANNEL_3_ID,"Channel 3",NotificationManager.IMPORTANCE_LOW);
            channel1.setDescription("This is notification channel 3");

            NotificationChannel channel4 = new NotificationChannel(CHANNEL_4_ID,"Channel 4",NotificationManager.IMPORTANCE_LOW);
            channel1.setDescription("This is notification channel 4");

            NotificationChannel channel5 = new NotificationChannel(CHANNEL_5_ID,"Channel 5",NotificationManager.IMPORTANCE_LOW);
            channel1.setDescription("This is notification channel 5");

            NotificationChannel channel6 = new NotificationChannel(CHANNEL_6_ID,"Channel 6",NotificationManager.IMPORTANCE_LOW);
            channel1.setDescription("This is notification channel 6");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
            notificationManager.createNotificationChannel(channel3);
            notificationManager.createNotificationChannel(channel4);
            notificationManager.createNotificationChannel(channel5);
            notificationManager.createNotificationChannel(channel6);
        }
    }
}
