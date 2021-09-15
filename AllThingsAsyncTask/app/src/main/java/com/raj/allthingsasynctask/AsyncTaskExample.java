package com.raj.allthingsasynctask;

import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

//Note: We are using weak reference to access the progress bar init'ed in the main activity

//Integer with a capital "I"!!!
//AsyncTask <how long you want async task to run, how long, what to show when async task is running, what to send when async task is done>
public class AsyncTaskExample extends AsyncTask <Integer, Integer, String> {
    private WeakReference<MainActivity> mainActivityWeakReference;

    //constructor (mainly only used for the weak reference thingy)
    public AsyncTaskExample(MainActivity mainActivity) {
        mainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
    }

    //Runs on ui thread
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        MainActivity mainActivityStrongReference = mainActivityWeakReference.get();
        if(mainActivityStrongReference == null || mainActivityStrongReference.isFinishing()) {
            return;
        }
    }

    //Obviously runs in the background as the name suggested (that is this method does NOT run on the ui thread)
    @Override
    protected String doInBackground(Integer... integers) {
        //Some dummy work for which the progress bar updates
        for (int i = 0; i < integers[0]; i++) {
            //calculating a percentage from the i value to update the progress bar
            publishProgress((i * 100) / integers[0]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "Finished!";
    }

    //Runs on ui thread
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        MainActivity mainActivityStrongReference = mainActivityWeakReference.get();
        if (mainActivityStrongReference == null || mainActivityStrongReference.isFinishing()) {
            return;
        }

        //update current progress on progress bar
        mainActivityStrongReference.progressBar.setProgress(values[0]);
    }

    //Runs on ui thread
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        MainActivity mainActivityStrongReference = mainActivityWeakReference.get();
        if (mainActivityStrongReference == null || mainActivityStrongReference.isFinishing()) {
            return;
        }

        Toast.makeText(mainActivityStrongReference, s, Toast.LENGTH_SHORT).show();

        //reset progress bar
        mainActivityStrongReference.progressBar.setProgress(0);
    }
}