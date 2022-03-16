package com.example.filelocationsdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    final String TAG = "FileLocationsDemo.MyDatabaseHelper";

    MyDatabaseHelper(Context ctx) {
        super(ctx, "mydb.sqllite3", null, 1);
        Log.d(TAG, "<Constructor>");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        createDatabase(db);
    }

    private void createDatabase(SQLiteDatabase db) {
        db.execSQL("create table demo (_id integer, name varchar)");
        Log.d(TAG, "Created table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int ov, int nv) {
        throw new UnsupportedOperationException("MyDatabaseHelper::onUpgrade");
    }
}
