package com.raj.allthingspopupdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class PopUpDialog extends AppCompatDialogFragment {
    private EditText etUsername;
    private EditText etPassword;
    private PopUpDialogListener popUpDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pop_up, null);

        //Configuring the pop up dialog
        //What happens when the user clicks the cancel aka negative button of the dialog
        builder.setView(view).setTitle("Title!!!").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })
                //What happens when the user clicks the okay aka positive button of the dialog
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = etUsername.getText().toString();
                        String password = etPassword.getText().toString();
                        popUpDialogListener.applyTexts(username,password); //Pass the username and password to the applyTexts method of the pop up dialog listener
                    }
                });

        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);

        return builder.create();
    }

    //Interface for background activity that launched the pop up dialog to access the info that the user entered in the pop up dialog
    public interface PopUpDialogListener {
        void applyTexts(String username, String password);
    }

    //This onAttach method is called when a fragment attaches itself to an activity...
    //In this case its called when the pop up dialog fragment attaches itself to the main activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        popUpDialogListener = (PopUpDialogListener) context ;
    }
}