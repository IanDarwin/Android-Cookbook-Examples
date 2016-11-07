package com.androidcookbook.appsingleton;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class AndroidApplication extends Application {
    private static AndroidApplication sInstance;
    private SessionHandler sessionHandler; // Generic your-application handler

    public static AndroidApplication getInstance() {
        return sInstance;
    }
    public SessionHandler getSessionHandler() {
        return sessionHandler;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sInstance.initializeInstance();
    }
    protected void initializeInstance() {
        // do all your initialization here
        sessionHandler = new SessionHandler(
                this.getSharedPreferences( "PREFS_PRIVATE", Context.MODE_PRIVATE ) );
    }

    /** This is a stand-in for some applicatin-specific session handler. */
    class SessionHandler {
        SharedPreferences sp;
        SessionHandler(SharedPreferences sp) {
            this.sp = sp;
        }
    }
}