package com.androidcookbook.hapticfeedback;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button customBtn = (Button) findViewById(R.id.btn_haptic);
        customBtn.setOnClickListener(new CustomHapticListener(100));
    }

    private class CustomHapticListener implements View.OnClickListener {

        // Duration in milliseconds to vibrate
        private final int durationMs;

        public CustomHapticListener(int ms) {
            durationMs = ms;
        }

        @Override
        public void onClick(View v) {
            Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibe.vibrate(durationMs);
        }
    }
}
