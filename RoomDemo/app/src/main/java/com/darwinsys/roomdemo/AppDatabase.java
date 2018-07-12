package com.darwinsys.roomdemo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Expense.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExpenseDao expenseDao();
}
