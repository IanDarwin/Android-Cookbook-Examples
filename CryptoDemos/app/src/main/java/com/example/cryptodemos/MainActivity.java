package com.example.cryptodemos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "CryptoDemos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.enc_hash_demo).setOnClickListener(v -> { try {
            final EncryptOrHashDemo encryptOrHashDemo = new EncryptOrHashDemo();
            final String message = "Rule Brittania!";
            encryptOrHashDemo.encryptMessageDemo(message);
            encryptOrHashDemo.hashMessageDemo(message);
        } catch (Exception e) {
            Log.wtf(TAG, "Hash Demo Blew: " + e);
        }});

        findViewById(R.id.enc_prefs_demo).setOnClickListener(v -> {
            try {new EncryptedSharedPrefsDemo().encryptedSharedPrefsDemo(this); } catch (Exception e) {
            Log.wtf(TAG, "Shared Prefs Demo Blew: " + e);
        }});

        findViewById(R.id.enc_file_demo).setOnClickListener(v -> {
            try {new EncryptedReadWriteDemo().writeReadFile(this, getFilesDir().getAbsolutePath(), "tmp4231", "My secret location is 0.00, 0.00"); } catch (Exception e) {
            Log.wtf(TAG, "Read/Write Demo Blew: " + e);
        }});
    }
}