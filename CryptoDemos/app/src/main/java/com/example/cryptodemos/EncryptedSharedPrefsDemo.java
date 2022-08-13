package com.example.cryptodemos;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class EncryptedSharedPrefsDemo {
    void encryptedSharedPrefsDemo(Context context) throws GeneralSecurityException, IOException {
        String sharedPrefsFile = context.getFilesDir() + "/tempfile5678.dat"; // in private storage, see I/O discussion
        String mainKeyAlias = "dancing_dufflepuds";
        SharedPreferences preferences = EncryptedSharedPreferences.create(
                sharedPrefsFile,
                mainKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

        // Use preferences.getXXX() or preferences.edit.setXXX().commit just like regular SharedPreferences
        String secretDecoder = "manic_moonshine";
        preferences.edit().putString("secret", secretDecoder).commit();

        String returned = preferences.getString("secret", "FAIL!");

        if (!returned.equals(secretDecoder)) {
            throw new AssertionError("Did not return correct string!");
        }
    }
}
