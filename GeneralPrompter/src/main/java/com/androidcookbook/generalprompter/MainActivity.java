package com.androidcookbook.generalprompter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Launch an "any type of file" prompter
 * @author Ian Darwin based on
 * https://developer.android.com/guide/topics/providers/document-provider.html
 * and
 * http://stackoverflow.com/questions/1698050/multiple-mime-types-in-android
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    final static int REQUEST_CODE_OPEN_ANY = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button fab = (Button) findViewById(R.id.go);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                String[] mimetypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(intent, REQUEST_CODE_OPEN_ANY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_CODE_OPEN_ANY:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "YAY!");
                } else {
                    Log.i(TAG, "Boo! Hiss!");
                }
                break;
            default:
                Log.e(TAG, "Unknown request code " + requestCode);
        }
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
