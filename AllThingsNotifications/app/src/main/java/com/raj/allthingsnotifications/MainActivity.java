package com.raj.allthingsnotifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManagerCompat = notificationManagerCompat.from(this);

        etNotifTitle = findViewById(R.id.ET_Notif_Title);
        etNotifDesc = findViewById(R.id.ET_Notif_Desc);
        btnSendNotifCh1 = findViewById(R.id.BTN_Send_On_Ch1);
        btnSendNotifCh2 = findViewById(R.id.BTN_Send_On_Ch2);

        btnSendNotifCh1.setOnClickListener(this);
        btnSendNotifCh2.setOnClickListener(this);
    }

    //Call this function to send a notification on channel 1
    public void sendOnChannel1(View v) {
        //Get text from edit text
        String notifTitle = etNotifTitle.getText().toString();
        String notifDesc = etNotifDesc.getText().toString();

        //Build the notification however you want it
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(notifTitle)
                .setContentText(notifDesc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        //This line is the line that actually sends the notification
        notificationManagerCompat.notify(1, notification);
    }

    //Call this function to send a notification on channel 2
    public void sendOnChannel2(View v) {
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
        notificationManagerCompat.notify(2, notification);
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
        }
    }
}