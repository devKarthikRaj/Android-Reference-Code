package com.raj.allthingsthreads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ThreadActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ThreadActivity";

    private volatile boolean threadStop = true; //Initially thread is not running so threadStop is true

    Button startThreadBtn;
    Button stopThreadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        startThreadBtn = findViewById(R.id.BTN_start_thread);
        stopThreadBtn = findViewById(R.id.BTN_stop_thread);

        startThreadBtn.setOnClickListener(this);
        stopThreadBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BTN_start_thread:
                startThread(view);
                break;
            case R.id.BTN_stop_thread:
                stopThread(view);
                break;
        }
    }

    public void startThread(View view) {
        threadStop = false;
        ExampleThread thread = new ExampleThread(10); //Create an instance of the example thread and pass it the required var(s)
        thread.start(); //Start thread
    }

    public void stopThread(View view) {
        threadStop = true;
    }

    class ExampleThread extends Thread{
        int seconds;

        //Constructor
        ExampleThread(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            for (int i = 0; i < seconds; i++) {
                //If threadStop is true... get out of this method to stop thread
                if(threadStop)
                    return;
                Log.d(TAG, "startThread: " + i);
                try {
                    Thread.sleep(1000); //Dummy work
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}