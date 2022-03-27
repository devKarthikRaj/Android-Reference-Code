package com.raj.allthingsservices;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

//This class runs at the start of the app's lifecycle (this is defined in the manifest
//We are starting the notification channel that we want to use in this app here... at the start of the app's lifecycle (its rec this way)

public class BaseApp extends Application {
    public static final String CHANNEL_ID = "foregroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel foregroundServiceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel ID",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(foregroundServiceChannel);
        }
    }
}
