package com.raj.allthingsnotifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.raj.allthingsnotifications.BaseApp.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NotificationManagerCompat notificationManagerCompat;
    private EditText etNotifTitle;
    private EditText etNotifDesc;
    private Button btnSendNotifCh1;
    private Button btnSendNotifCh2;
    private Button btnSendNotifCh3;
    private Button btnSendNotifCh4;
    private Button btnSendNotifCh5;
    private Button btnSendNotifCh6;

    private MediaSessionCompat mediaSession; //Media style notif... Like the spotify notif... Incase you ever gotta do a media player or somethin' like that

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManagerCompat = notificationManagerCompat.from(this);

        etNotifTitle = findViewById(R.id.ET_Notif_Title);
        etNotifDesc = findViewById(R.id.ET_Notif_Desc);
        btnSendNotifCh1 = findViewById(R.id.BTN_Send_On_Ch1);
        btnSendNotifCh2 = findViewById(R.id.BTN_Send_On_Ch2);
        btnSendNotifCh3 = findViewById(R.id.BTN_Send_On_Ch3);
        btnSendNotifCh4 = findViewById(R.id.BTN_Send_On_Ch4);
        btnSendNotifCh5 = findViewById(R.id.BTN_Send_On_Ch5);
        btnSendNotifCh6 = findViewById(R.id.BTN_Send_On_Ch6);

        mediaSession = new MediaSessionCompat(this, "tag"); //Config media session

        btnSendNotifCh1.setOnClickListener(this);
        btnSendNotifCh2.setOnClickListener(this);
        btnSendNotifCh3.setOnClickListener(this);
        btnSendNotifCh4.setOnClickListener(this);
        btnSendNotifCh5.setOnClickListener(this);
        btnSendNotifCh6.setOnClickListener(this);
    }

    //Bare bone notification with title and desc
    //Call this function to send a notification on channel 2
    public void sendOnChannel1(View v) {
        //Get text from edit text
        String notifTitle = etNotifTitle.getText().toString();
        String notifDesc = etNotifDesc.getText().toString();

        //Build the notification however you want it
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(notifTitle)
                .setContentText(notifDesc)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        //This line is the line that actually sends the notification
        notificationManagerCompat.notify(1, notification);
    }

    //Notif that goes to an activity in your app when clicked and notif that has a button on it that when clicked displays a toast
    //Call this function to send a notification on channel 1
    public void sendOnChannel2(View v) {
        //Get text from edit text
        String notifTitle = etNotifTitle.getText().toString();
        String notifDesc = etNotifDesc.getText().toString();

        //Intent to bring user to a specific activity when user clicks notif
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,activityIntent,0);

        //Intent to execute broadcast receiver when user clicks button on notif (toast button in our case)
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", "Toast trigg'ed by notif");
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //Build the notification however you want it
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(notifTitle)
                .setContentText(notifDesc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent) //Executed when user clicks notif... pass a pending intent here to go the some activity
                .setAutoCancel(true) //When user clicks on notif... The notif will auto dismiss
                .setOnlyAlertOnce(true) //Notif wont make any noise or buzz when notif is updated if this is true
                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent) //This adds a button called Toast to the notif and
                                                                            //triggers the pending intent called actionIntent passed to it
                                                                            //The image ic_launcher image is for older android versions only
                .build();

        //This line is the line that actually sends the notification
        notificationManagerCompat.notify(2, notification);
    }

    //Big text style notif with image (large icon)
    public void sendOnChannel3(View v) {
        //Get text from edit text
        String notifTitle = etNotifTitle.getText().toString();
        String notifDesc = etNotifDesc.getText().toString();

        //Converting png to bitmap... cuz only bitmap images can be displayed in notif
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.sheldon_cooper);

        //Build the notification however you want it
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(notifTitle)
                .setContentText(notifDesc)
                .setLargeIcon(largeIcon) //Setting image for the notif
                .setStyle(new NotificationCompat.BigTextStyle() //One of the many styles that are available for android notifs
                          .bigText(getString(R.string.long_dummy_text))
                          .setBigContentTitle("Big Content Title")
                          .setSummaryText("Summary Text"))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        //This line is the line that actually sends the notification
        notificationManagerCompat.notify(3, notification);
    }

    //Inbox style notif
    public void sendOnChannel4(View v) {
        //Get text from edit text
        String notifTitle = etNotifTitle.getText().toString();
        String notifDesc = etNotifDesc.getText().toString();

        //Build the notification however you want it
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(notifTitle)
                .setContentText(notifDesc)
                .setStyle(new NotificationCompat.InboxStyle() //Another notif style that is available in android
                          .addLine("This is line 1")
                          .addLine("This is line 2")
                          .addLine("This is line 3")
                          .addLine("This is line 4")
                          .addLine("This is line 5")
                          .addLine("This is line 6")
                          .addLine("This is line 7")
                          .addLine("This is line 8"))  /*Line 8 will not get displayed!!!*/
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        //This line is the line that actually sends the notification
        notificationManagerCompat.notify(4, notification);
    }

    //Big picture only notif
    public void sendOnChannel5(View v) {
        //Get text from edit text
        String notifTitle = etNotifTitle.getText().toString();
        String notifDesc = etNotifDesc.getText().toString();

        Bitmap bigPic = BitmapFactory.decodeResource(getResources(), R.drawable.howard_wolowitz);

        //Build the notification however you want it
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(notifTitle)
                .setContentText(notifDesc)
                .setLargeIcon(bigPic)
                .setStyle(new NotificationCompat.BigPictureStyle()
                          .bigPicture(bigPic)
                          .bigLargeIcon(null)) //this bit will prevent the large icon from fading out when the notif expanded
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        //This line is the line that actually sends the notification
        notificationManagerCompat.notify(5, notification);
    }

    //Media style notif (like on spotify) but no actual music playing functionality (and the buttons dont do anything)
    public void sendOnChannel6(View v) {
        //Get text from edit text
        String notifTitle = etNotifTitle.getText().toString();
        String notifDesc = etNotifDesc.getText().toString();

        Bitmap palettePic = BitmapFactory.decodeResource(getResources(), R.drawable.penny_hofstadter);

        //Build the notification however you want it
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(notifTitle)
                .setLargeIcon(palettePic)
                .addAction(R.drawable.icon_car, "Car", null)  //These are the small buttons on the notif... They dont do anything...
                .addAction(R.drawable.icon_train, "Train", null) //If you want them to do something... Can pass a pendingIntent with
                .addAction(R.drawable.icon_boat, "Boat", null)   //a broadcast rec like channel 2
                .addAction(R.drawable.icon_airplane, "Airplane", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                          .setShowActionsInCompactView(1,2,3) // 4 small buttons 0,1,2,3... this bit will make 1,2,3 be displayed when notif is not expanded
                          .setMediaSession(mediaSession.getSessionToken()))
                .setSubText("Sub Text")
                .setContentText(notifDesc)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        //This line is the line that actually sends the notification
        notificationManagerCompat.notify(6, notification);
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_Send_On_Ch1:
                sendOnChannel1(view);
                break;
            case R.id.BTN_Send_On_Ch2:
                sendOnChannel2(view);
                break;
            case R.id.BTN_Send_On_Ch3:
                sendOnChannel3(view);
                break;
            case R.id.BTN_Send_On_Ch4:
                sendOnChannel4(view);
                break;
            case R.id.BTN_Send_On_Ch5:
                sendOnChannel5(view);
                break;
            case R.id.BTN_Send_On_Ch6:
                sendOnChannel6(view);
                break;
        }
    }
}