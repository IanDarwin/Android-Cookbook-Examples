package com.androidcookbook.aboutboxdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutBox {
    static String VersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }

    public static void Show(Activity callingActivity) {
        //Use a Spannable to allow for links highlighting
        SpannableString aboutText = new SpannableString("Version " +
                VersionName(callingActivity) + "\n\n" +
                callingActivity.getString(R.string.about));
        // views to pass to AlertDialog.Builder and to set the text
        View about;
        TextView tvAbout;
        try {
            //Inflate the custom view
            LayoutInflater inflater = callingActivity.getLayoutInflater();
            about = inflater.inflate(R.layout.aboutbox,
                    (ViewGroup) callingActivity.findViewById(R.id.aboutView));
            tvAbout = (TextView) about.findViewById(R.id.aboutText);
        } catch (InflateException e) {
            // Unchecked exception, unlikely, but default to TextView if it occurs
            about = tvAbout = new TextView(callingActivity);
        }
//Set the about text
        tvAbout.setText(aboutText);
// Now Linkify the text
        Linkify.addLinks(tvAbout, Linkify.ALL);
//Build and show the dialog
        new AlertDialog.Builder(callingActivity)
                .setTitle("About " + callingActivity.getString(R.string.app_name))
                .setCancelable(true)
                .setIcon(R.drawable.icon)
                .setPositiveButton("OK", null)
                .setView(about)
                .show(); //Builder method returns allow for method chaining
    }
}