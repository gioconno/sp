package com.rz.satispay;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class DigestBuilder {

    static String getBodyDigest() throws NoSuchAlgorithmException {
        return getBodyDigest(null);
    }


    static String getBodyDigest(String body) throws NoSuchAlgorithmException {

        String digest;
        if (body == null) {
            digest = hashAndEncodeString("");
        } else {
            digest = hashAndEncodeString(body);
        }

        return String.format("%s=%s", Constants.ENCRYPT_ALG.getValue(), digest);
    }

    private static String hashAndEncodeString(String originalString) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(Constants.ENCRYPT_ALG.getValue());

        byte[] out = messageDigest.digest(originalString.getBytes(StandardCharsets.UTF_8));

        return new String(Base64.getEncoder().encode(out), StandardCharsets.UTF_8);
    }


}


