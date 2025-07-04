package com.example.passwordmanager.utils;

import android.util.Base64;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78"; // 16 символов для AES-128

    public static String encrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        return Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
    }

    public static String decrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue = Base64.decode(value, Base64.DEFAULT);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue);
        return new String(decryptedByteValue, "utf-8");
    }

    private static Key generateKey() {
        return new SecretKeySpec(KEY.getBytes(), ALGORITHM);
    }
}