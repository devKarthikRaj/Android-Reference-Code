package com.raj.allthingspopupdialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener, PopUpDialog.PopUpDialogListener {

    private TextView tvUsername;
    private TextView tvPassword;
    private Button btnOpnDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvUsername = findViewById(R.id.tv_username);
        tvPassword = findViewById(R.id.tv_password);
        btnOpnDlg = findViewById(R.id.btn_open_dialog);

        btnOpnDlg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_open_dialog:
                openDialog();
        }
    }

    public void openDialog() {
        PopUpDialog popUpDialog = new PopUpDialog();
        popUpDialog.show(getSupportFragmentManager(), "Pop Up Dialog");
    }

    //When the user clicks the okay aka positive button in the pop up dialog... the username and password that the user entered in the
    //pop up dialog is sent to the applyTexts method through which it is accessed by the main activity
    @Override
    public void applyTexts(String username, String password) {
        tvUsername.setText(username);
        tvPassword.setText(password);
    }
}

