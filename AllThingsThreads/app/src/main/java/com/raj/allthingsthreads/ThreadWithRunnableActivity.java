package com.raj.allthingsthreads;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThreadWithRunnableActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ThreadWithRunnableActivity";

    Button startThreadBtn;
    Button stopThreadBtn;
    TextView tv1; //When the thread has run for 5s... Text in this textview will change from Text 1 to Text 2

    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_with_runnable);

        startThreadBtn = findViewById(R.id.BTN_start_thread);
        stopThreadBtn = findViewById(R.id.BTN_stop_thread);
        tv1 = findViewById(R.id.TV1);

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
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable(10); //Create an instance of the example runnable and pass it the required var(s)
        new Thread(runnable).start(); //Start thread
    }

    public void stopThread(View view) {
        stopThread = true;
        tv1.setText("Text 1"); //Reset back to "Text 1"... So that we can run this again
    }

    class ExampleRunnable implements Runnable {
        int seconds;

        //Constructor
        ExampleRunnable(int seconds) {
            this.seconds = seconds;
        }

        @SuppressLint("LongLogTag")
        @Override
        public void run() {
            for (int i = 0; i < seconds; i++) {
                if (stopThread)
                    return;
                if (i == 5) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Change textview text from Text 1 to Text 2
                            tv1.setText("Text 2"); //This is just to demonstrate how to access UI from a runnable
                        }
                    });
                }
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