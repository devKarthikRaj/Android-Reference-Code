package com.raj.allthingsbroadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class OrderedBroadcastReceiver1 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "OR1 triggered!!!", Toast.LENGTH_SHORT).show();

        int resultCode = getResultCode();
        String resultData = getResultData();
        Bundle resultExtras = getResultExtras(true);
        String stringExtra = resultExtras.getString("stringExtra");

        resultCode++;
        stringExtra += "->OR1";

        String toastText = "OR1\n" +
                "resultCode: " + resultCode + "\n" + //Inital code (int)
                "resultData: " + resultData + "\n" + //Initial data (string)
                "stringExtra: " + stringExtra; //Actual data

        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

        //Set the new result code, result data and result extras
        resultData = "added by OR1";
        resultExtras.putString("stringExtra", stringExtra);
        setResult(resultCode, resultData, resultExtras);
    }
}