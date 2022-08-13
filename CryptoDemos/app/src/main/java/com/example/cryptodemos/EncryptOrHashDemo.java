package com.example.cryptodemos;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class EncryptOrHashDemo {
    private static final String TAG = "EncryptOrHashDemo";
    String message = "Rule Brittania!";

    void encryptMessageDemo() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] plaintext = message.getBytes(StandardCharsets.UTF_8);
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256);
        SecretKey key = keygen.generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] ciphertext = cipher.doFinal(plaintext);
        byte[] iv = cipher.getIV();
        Log.d(TAG, "Encrypted message is " + ciphertext);
    }

    void hashMessageDemo() throws NoSuchAlgorithmException {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] digestBytes = digest.digest(messageBytes);
        Log.d(TAG, "Message signature is " + digestBytes);
    }
}
