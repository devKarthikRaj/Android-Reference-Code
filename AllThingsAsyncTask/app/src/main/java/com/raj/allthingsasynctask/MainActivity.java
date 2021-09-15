package com.raj.allthingsasynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

//The async task that we are going to run here is a progress bar update

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnStartAsyncTask;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartAsyncTask = findViewById(R.id.BTN_start_async_task);
        progressBar = findViewById(R.id.linearProgressIndicator);

        btnStartAsyncTask.setOnClickListener(this);
    }

    //This is the method that actually starts the async task
    public void startAsyncTask(View view) {
        AsyncTaskExample asyncTaskExample = new AsyncTaskExample(this);
        asyncTaskExample.execute(10); //run the async task for 10s cuz if we pass 10 here the progress bar updates for 10s
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_start_async_task:
                startAsyncTask(view);
                break;
        }
    }
}