package com.example.emailandroid;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 *  Email With Attachments example
 */
public class Main extends Activity {
        private static final String TAG = "Main";
        private final static String EXTRA_RECIPIENT = "somebody@someplace.domain";

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);

                // Set the View Layer
                setContentView(R.layout.main);

                // The Event Listener onClicks are set in the XML
                
            }

        public void oneAttachment(View view) {
        	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        	emailIntent.setType("text/html");
        	emailIntent.putExtra(Intent.EXTRA_EMAIL, EXTRA_RECIPIENT);
        	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Subject");

        	// Obtain reference to (hard-coded) String and pass it to Intent
        	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
        			getString(R.string.my_text));
        	startActivity(emailIntent);
        }
        
        public void twoAttachments(View v) {
        	Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        	intent.setType("text/plain");
        	intent.putExtra(Intent.EXTRA_SUBJECT, "Test multiple attachments");
        	intent.putExtra(Intent.EXTRA_TEXT, "Mail with multiple attachments");
        	intent.putExtra(Intent.EXTRA_EMAIL, new String[]{EXTRA_RECIPIENT});

        	File baseDir = 
        		Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        	baseDir.mkdirs();
        	File f1 = new File(baseDir, "File1.txt");
        	writeToFile(f1, "This is file 1 contents");
        	File f2 = new File(baseDir, "File2.txt");
        	writeToFile(f2, "This is file 2 contents");
        	ArrayList<Uri> uris = new ArrayList<Uri>();
        	uris.add(Uri.fromFile(f1));
        	uris.add(Uri.fromFile(f2));

        	intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        	startActivity(intent);
        }

		private void writeToFile(File file, String contents) {
			file.delete();
			try (PrintWriter pout = new PrintWriter(file)) {
				pout.println(contents);
			} catch (IOException e) {
				Toast.makeText(this,"Improbable IOException writing: " + e, Toast.LENGTH_LONG).show();
			}
		}
}
