package com.example.emailandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity implements OnClickListener {
        private static final String TAG = "Main";
        private Button emailButton;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);

                // Set the View Layer
                setContentView(R.layout.main);

                // Get reference to Email Button
                this.emailButton = (Button) this.findViewById(R.id.emailButton);

                // Sets the Event Listener onClick
                this.emailButton.setOnClickListener(this);

            }

        @Override
        public void onClick(View view) {
            if (view == this.emailButton) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                emailIntent.putExtra(android.content.Intent.EXTRA_TITLE, "My Title");
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Subject");

                // Obtain reference to (hard-coded) String and pass it to Intent
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
                    getString(R.string.my_text));
                startActivity(emailIntent);
            }
        }
}
