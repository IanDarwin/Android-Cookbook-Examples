package uk.co.webwrights.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;


public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final String TAG = "MainActivity";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    // Whether the Log Fragment is currently shown.
    private boolean mLogShown;

    /**
     * Root of the layout of this Activity.
     */
    private View mLayout;

    public void accessFiles(View view) {
        Log.i(TAG, "Access File. Checking permission.");
        // Check if the WRITE_EXTERNAL_STORAGE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // WRITE_EXTERNAL_STORAGE permission has not been granted.

            requestExternalStoragePermissions();

        } else {

            // External storage permissions is already available

            Log.i(TAG,
                    "External storage permissions have already been granted. Writing and Reading.");
            try {
                writeAndReadFile();

                Snackbar.make(mLayout, R.string.file_permissions_worked,
                        Snackbar.LENGTH_LONG).show();
            } catch (IOException e) {
                Snackbar.make(mLayout, R.string.file_permissions_did_not_work,
                        Snackbar.LENGTH_LONG).show();
            }
        }

    }



    /**
     * Requests the WRITE_EXTERNAL_STORAGE and READ_EXTERNAL_STORAGE permissions.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */

    private void requestExternalStoragePermissions() {
        Log.i(TAG, "WRITE_EXTERNAL_STORAGE permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional explanation to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            Log.i(TAG,
                    "Displaying WRITE_EXTERNAL_STORAGE permission rationale to provide additional context.");
            Snackbar.make(mLayout, R.string.external_storage_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_EXTERNAL_STORAGE);
                        }
                    }).show();
        } else {

            // WRITE_EXTERNAL_STORAGE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        }

    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        boolean granted = true;
         if (requestCode == REQUEST_EXTERNAL_STORAGE) {
                // Received permission result for external storage permission.
                Log.i(TAG, "Received response for external storage permission request.");

                // Check if the permissions have been granted
                if (grantResults.length > 0 ) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            granted = false;
                        }
                    }
                } else {
                    granted = false;
                }

                if (granted){
					// permission has been granted, we can proceed
                    Log.i(TAG, "External storage permissions have now been granted.");
                    Snackbar.make(mLayout, R.string.permission_available_external_storage,
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "External storage permissions were NOT granted.");
                    Snackbar.make(mLayout, R.string.permissions_not_granted,
                            Snackbar.LENGTH_SHORT).show();

                }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    public void onBack(View view) {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.sample_main_layout);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            PermissionsFragment fragment = new PermissionsFragment();
            transaction.replace(R.id.content_fragment, fragment);
            transaction.commit();
        }
    }


    /**
     * This method has nothing to do with permissions other than checking that they were actually granted!
     * If permissions are not granted then we will see an IOException thrown
     * @throws IOException
     */
    private void writeAndReadFile() throws IOException {
        File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        f.mkdirs();
        File testFile = new File(f,"test.txt");
        PrintStream ps = null;
        try {
            ps = new PrintStream( new FileOutputStream(testFile));
            ps.println("Some data");
        } catch (FileNotFoundException e) {
            Toast.makeText(MainActivity.this, "Exception: see the logs", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if(null != ps) {
                ps.close();
            }
        }
        // Check we can read the file
        BufferedReader br = new BufferedReader(new FileReader(testFile));
        try {
            String s = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                br.close();
            }
        }


    }
}
