package com.raj.allthingsservices;

import static com.raj.allthingsservices.BaseApp.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {

    //onCreate is triggered the FIRST time we create our service
    @Override
    public void onCreate() {
        super.onCreate();
    }

    //The onStartCommand method is triggered when we start our service.. That is whenever we call startService
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0); //req for notification

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_notification) //Just using some random icon for the notification
                .setContentIntent(pendingIntent) //this is req for notification
                .build();

        startForeground(1, notification);

        //Do some work (if doing heavy work... do the work on a background thread)

        //Then stop the thread
        //stopSelf(); //Not stopping the thread here thru the code cuz we are not doing any real work here and we have a stop button to stop the foreground service

        //What we return here tells android what to do when it kills our service
        //We have 3 options:
        //> START_NOT_STICKY: Once killed... Our service will be gone and won't be restarted!
        //> START_STICKY: Once killed... Android will restart our service as soon as possible but the intent we passed to it will be null (so input in this case wont be passed to it)
        //> START_REDELIVER_INTENT: Once killed... Android will restart our service AND it'll pass the last intent to the service again
        // All this matters more only for background services actually cuz foreground services are unlikely to be killed
        return START_NOT_STICKY; //Here just use not sticky cuz we really care what happens to the service if android kills it
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
