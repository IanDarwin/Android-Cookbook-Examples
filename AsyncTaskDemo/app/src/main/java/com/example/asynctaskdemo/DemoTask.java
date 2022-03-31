package com.example.asynctaskdemo;

import android.os.AsyncTask;
import android.util.Log;

/**
 * This class is the actual task, started from the main.
 * Type params are Input, Progress, Result
 */
public class DemoTask extends AsyncTask<String, Integer, String> {

    final static String TAG = DemoTask.class.getSimpleName();

    final MainActivity main;

    DemoTask(MainActivity main) {
        this.main = main;
    }

    /**
     *  Called when the client invokes execute() on the task.
     */
    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute");
        super.onPreExecute();
    }

    /** Called in the background thread
     * @param strings The inputs, from execute(). Only [0] is used.
     */
    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: " + strings[0]);
        for (int i = 10; i > 0; i--) {
            try {
                publishProgress(Integer.valueOf(i));
                Thread.sleep(1000);
            } catch (InterruptedException cantHappen) {
                cantHappen.printStackTrace();
            }
        }
        return null;
    }

    /** Called on the UI thread in response to publishProgress() above.
     *
     * @param values The String that was passed into publishProgress().
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(TAG, "onProgressUpdate");
        main.label.setText(Integer.toString(values[0]));
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute");
        super.onPostExecute(s);
    }

}