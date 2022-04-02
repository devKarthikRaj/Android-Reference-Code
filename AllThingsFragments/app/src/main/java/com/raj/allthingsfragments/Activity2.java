package com.raj.allthingsfragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity implements View.OnClickListener {

    TextView tvTitle;
    Button btnGotoActivityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        tvTitle = findViewById(R.id.TV_Title);
        btnGotoActivityMain = findViewById(R.id.BTN_Goto_Activity_Main);

        btnGotoActivityMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_Goto_Activity_Main:
                startActivity(new Intent(Activity2.this, MainActivity.class));
                break;
        }
    }
}