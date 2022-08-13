package com.example.cryptodemos;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;

import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKeys;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

public class EncryptedReadWriteDemo {

    void writeReadFile(Context context, String dirName, String fileName, String message) throws GeneralSecurityException, IOException {

        // Although you can define your own key generation parameter specification,
        // Google recommends using the value specified here.
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

        // WRITE THE FILE

        // Create a file with this name, or replace an entire existing file
        // that has the same name. Note that you cannot append to an existing file,
        // and the file name cannot contain path separators.
        EncryptedFile encryptedFileOut = new EncryptedFile.Builder(
                new File(dirName, fileName),
                context,
                mainKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build();

        byte[] fileContent = message.getBytes(StandardCharsets.UTF_8);
        OutputStream outputStream = encryptedFileOut.openFileOutput();
        outputStream.write(fileContent);
        outputStream.flush();
        outputStream.close();

        // READ THE FILE BACK
        EncryptedFile encryptedFile = new EncryptedFile.Builder(
                new File(dirName, fileName),
                context,
                mainKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build();

        InputStream inputStream = encryptedFile.openFileInput();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int nextByte = inputStream.read();
        while (nextByte != -1) {
            byteArrayOutputStream.write(nextByte);
            nextByte = inputStream.read();
        }

        byte[] plaintext = byteArrayOutputStream.toByteArray();

        if (!message.equals(new String(plaintext))) {
            throw new IllegalStateException("Input not read back successfully");
        }
    }
}
