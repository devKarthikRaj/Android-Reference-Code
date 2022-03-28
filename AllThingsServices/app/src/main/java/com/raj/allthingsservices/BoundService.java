package com.raj.allthingsservices;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//The setting up part of this class... That boundServiceLocalBinder and boundServiceBinder is kinda stupid but that's how Android want it done
//So just gotta go with the flow here
//Orite so when the main activity (BoundServiceActivity in this case) wants to bind with the BoundService class... It goes and looks for the OnBind method...
//The OnBind method returns the boundServiceBinder
//So what is this boundServiceBinder that the OnBind method is returning... Its an instance of the boundServiceLocalBinder class...
//This boundServiceLocalBinder returns the BoundService class which is this class we are now in... It returns a reference to this BoundService class
//So we gotta do all that just to return an instance of the BoundService class itself... Kinda stupid but that's how Android wants you to do it...
//The wild goose chase goes like this... OnBind > boundServiceBinder > boundServiceLocalBinder > getService > BoundService

public class BoundService extends Service {

    private final IBinder boundServiceBinder = new boundServiceLocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return boundServiceBinder;
    }

    //When the main activity (BoundServiceActivity in this case) is successfully bound to this Bound Service class...
    //The main activity can access this getCurrentTime method to get the current time!
    //We are creating this bound service just to get the current time... This is just an example!
    //Bound services are usually used when the service needs to access the UI thread to update some of the components on the UI
    public String getCurrentTime() {
        //This simple date format stuff is Java stuff... Its not Android specific
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        return (simpleDateFormat.format(new Date()));
    }

    public class boundServiceLocalBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }
}
