package com.example.pscapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JWTHelper {

    private String header;
    private String payload;
    private String signature;

    public JWTHelper(String header, String payload, String signature) {
        this.header = header;
        this.payload = payload;
        this.signature = signature;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPayload() {
        return this.payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public static String[] splitJWT(String jwt) {
        String[] parts = jwt.split("\\.");
        return parts;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decode(String encdodedString) {
        return new String(Base64.getUrlDecoder().decode(encdodedString));
    }

    public static String hMacSha256(String data, String secret) {
        try {
            byte[] hash = secret.getBytes(StandardCharsets.UTF_8);
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);

            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return encode(Arrays.toString(signedBytes));
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String encode(String string) {
        String result = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            result = Base64.getUrlEncoder().withoutPadding().encodeToString(string.getBytes());
        }
        return result;
    }
}