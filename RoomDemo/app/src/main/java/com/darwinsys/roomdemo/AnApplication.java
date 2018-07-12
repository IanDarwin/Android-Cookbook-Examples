package com.darwinsys.roomdemo;

import android.app.Application;
import android.arch.persistence.room.Room;

import java.util.ArrayList;
import java.util.List;

public class AnApplication extends Application {

    private AppDatabase mDatabase;
    private final List<Expense> mList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase = Room.databaseBuilder(this,
                AppDatabase.class, "expenses.db").build();
    }

    AppDatabase getDatabase() {
        return mDatabase;
    }

    List<Expense> getList() {
        return mList;
    }
}
