package com.example.filelocationsdemo;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "FileLocationsDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MyDatabaseHelper mdb = new MyDatabaseHelper(this);
        SQLiteDatabase db = mdb.getWritableDatabase();
        db.execSQL("Insert into demo(_id,name) values(1, 'Hello')");
        mdb.close();

        SharedPreferences pref = getSharedPreferences("MyPrefs", 0);
        pref.edit().putString("prefName", "prefValue").commit();

        File f = new File(getDataDir(), "myDataFile");
        try {
            f.createNewFile();
            Log.d(TAG, "Created file " + f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String externalStorageState = Environment.getExternalStorageState();
        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            File downloadCacheDirectory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            Log.d(TAG, "External Files Download dir = " + downloadCacheDirectory);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Do nothing", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}