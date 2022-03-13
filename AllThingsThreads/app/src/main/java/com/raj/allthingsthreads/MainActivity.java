package com.raj.allthingsthreads;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/* Summary of what this code is all about...
 *  To see if thread is running... Look at the app logs in the run tab
 *  The slide switch is to put there to move and see when thread is running...
 *  When thread is running... If you move the slide switch... The app shouldn't crash!
 *  ^ This is just to show that you are free to do stuff with the UI thread when the thread you created and started is running in the background
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button threadBtn;
    Button threadWithRunnableBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        threadBtn = findViewById(R.id.BTN_thread);
        threadWithRunnableBtn = findViewById(R.id.BTN_thread_with_runnable);

        threadBtn.setOnClickListener(this);
        threadWithRunnableBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_thread:
                //In this activity, code to be run in a separate thread is written directly into the thread's run method
                startActivity(new Intent(MainActivity.this, ThreadActivity.class));
                break;
            case R.id.BTN_thread_with_runnable:
                //In this activity, code to be run in a separate thread is written as a runnable and passed to the thread
                //This method makes the code to be run in a separate thread more portable!
                startActivity(new Intent(MainActivity.this, ThreadWithRunnableActivity.class));
                break;
        }
    }
}