package com.rz.satispay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SignatureBuilder {

    private final static String BEGIN_KEY_HEADER = "-----BEGIN PRIVATE KEY-----";
    private final static String END_KEY_HEADER = "-----END PRIVATE KEY-----";
    private final static String END_LINE = "\n";
    private final static String KEY_FACTORY_ALG = "RSA";
    private final static String SIGNATURE_ALG = "SHA256withRSA";
    private final static String ENCODING_CHARSET = "UTF-8";

    static String createSignature(String stringToSign, String privateKeyPath) {
        try {
            String privateKey = getPrivateKey(privateKeyPath);

            return signAndEncode(stringToSign.trim(), privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | UnsupportedEncodingException | SignatureException e) {
            e.printStackTrace();

            throw new IllegalStateException("Signature failed: " + e.getMessage(), e);
        }

    }

    private static String getPrivateKey(String privateKeyPath) {
        try {
            return readFile(privateKeyPath);
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Private key exception: " + e.getMessage(), e);
        }
    }

    private static String readFile(String privateKeyPath) throws IOException, URISyntaxException {

        Path path = Paths.get(SignatureBuilder.class
                .getClassLoader()
                .getResource(privateKeyPath)
                .toURI());

        Stream<String> lines = Files.lines(path);

        return lines.collect(Collectors.joining("\n"));
    }

    private static String signAndEncode(String stringToSign, String privateKey) throws InvalidKeySpecException,
            SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        byte[] s = sign(stringToSign, privateKey);
        return Base64.getEncoder().encodeToString(s);
    }

    private static byte[] sign(String stringToSign, String key) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        String strippedPrivateKey = stripKey(key);

        byte[] b1 = Base64.getDecoder().decode(strippedPrivateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
        KeyFactory kf = KeyFactory.getInstance(KEY_FACTORY_ALG);

        Signature privateSignature = Signature.getInstance(SIGNATURE_ALG);
        privateSignature.initSign(kf.generatePrivate(spec));
        privateSignature.update(stringToSign.getBytes(ENCODING_CHARSET));

        return privateSignature.sign();
    }

    private static String stripKey(String key) {
        return key
                .replaceAll(BEGIN_KEY_HEADER, "")
                .replaceAll(END_KEY_HEADER, "")
                .replaceAll(END_LINE, "");
    }

}


