package com.rz.satispay;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {

    private final static String PROTOCOL = "https";
    private final static String HOST = "staging.authservices.satispay.com";

    private final static String REQUEST_PATH = "/wally-services/protocol/tests/signature";

    private final static String ENC_ALG = "SHA-256";
    private final static String SIGN_ALG = "rsa-sha256";

    private final static String PRIVATE_KEY_FILE = "client-rsa-private-key.pem";

    private final static String KEY_ID = "signature-test-66289";

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, URISyntaxException, InvalidKeySpecException, InvalidKeyException, SignatureException {

        Application application = new Application();

        application.checkSingleParts();

    }

    private void checkSingleParts() throws NoSuchAlgorithmException, IOException {
        System.out.println("----------------< CHECK SINGLE PARTS >----------------");

//        String digest = getBodyDigest("{\n" +
//                "  \"flow\": \"MATCH_CODE\",\n" +
//                "  \"amount_unit\": 100,\n" +
//                "  \"currency\": \"EUR\"\n" +
//                "}");

        String digest = getBodyDigest();

        System.out.println("|- Digest " + digest);

        Map<String, String> strings = createGetRequestMap(digest);
        StringBuilder sb = new StringBuilder();
        strings.forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n"));
        System.out.println(sb.toString());

        String privateKey = getPrivateKey();
//        System.out.println("|- PrivateKey " + privateKey);

        String signature = createSignature(sb.toString().trim(), privateKey);
        System.out.println("|- Signature " + signature);

        String authHeader = createAuthHeader(strings, signature);
        System.out.println(authHeader);

        getWithSignature(strings, authHeader);

    }


    private String getBodyDigest() throws NoSuchAlgorithmException {
        return getBodyDigest(null);
    }


    private String getBodyDigest(String body) throws NoSuchAlgorithmException {

        String digest;
        if (body == null) {
            digest = hashAndEncodeString("");
        } else {
            digest = hashAndEncodeString(body);
        }


        return String.format("%s=%s", ENC_ALG, digest);

    }

    private Map<String, String> createGetRequestMap(String digest) {

        Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("(request-target)", HTTPMethod.GET.toString().toLowerCase() + " " + REQUEST_PATH.toLowerCase());
        requestMap.put("host", HOST.toLowerCase());
        requestMap.put("date", ZonedDateTime.now().toInstant().toString());
        requestMap.put("digest", digest);

        return requestMap;

    }

    private String hashAndEncodeString(String originalString) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(ENC_ALG);

        byte[] out = messageDigest.digest(originalString.getBytes(StandardCharsets.UTF_8));

        return new String(Base64.getEncoder().encode(out), StandardCharsets.UTF_8);
    }

    private String createSignature(String stringToSign, String privateKey) {
        try {
            String strippedPrivateKey = privateKey
                    .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("-----END PRIVATE KEY-----", "")
                    .replaceAll("\n", "");

            byte[] b1 = Base64.getDecoder().decode(strippedPrivateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(kf.generatePrivate(spec));
            privateSignature.update(stringToSign.getBytes("UTF-8"));
            byte[] s = privateSignature.sign();
            return Base64.getEncoder().encodeToString(s);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | UnsupportedEncodingException | SignatureException e) {
            e.printStackTrace();

            throw new IllegalStateException("Signature failed: " + e.getMessage(), e);
        }

    }

    private String getPrivateKey() {
        try {
            return readFile(PRIVATE_KEY_FILE);
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Private key exception: " + e.getMessage(), e);
        }
    }

    private String readFile(String fileName) throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());

        Stream<String> lines = Files.lines(path);

        return lines.collect(Collectors.joining("\n"));
    }

    private String createAuthHeader(Map<String, String> requestHeaderMap, String signature) {

        StringBuilder headersStringBuilder = new StringBuilder("headers=\"");
        requestHeaderMap.keySet().forEach(k -> headersStringBuilder.append(k).append(" "));


        return "Authorization: Signature keyId=\""
                + KEY_ID
                + "\", algorithm=\""
                + SIGN_ALG + "\", "
                + headersStringBuilder.toString().trim()
                + "\", "
                + "signature=\""
                + signature
                + "\"";
    }

    private void getWithoutSignature() throws IOException, NoSuchAlgorithmException {

        System.out.println("----------------< GET WITHOUT SIGNATURE >----------------");

        URL url = new URL(String.format("%s://%s/%s", PROTOCOL, HOST, REQUEST_PATH));
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");


        print_https_cert(con);

        print_content(con);

        String digest = getBodyDigest();

        System.out.println(digest);
    }

    private void getWithSignature(Map<String, String> requestHeaderMap, String signature) throws IOException, NoSuchAlgorithmException {

        System.out.println("----------------< GET WITH SIGNATURE >----------------");

        URL url = new URL(String.format("%s://%s%s", PROTOCOL, HOST, REQUEST_PATH));

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod(HTTPMethod.GET.toString());
        con.setRequestProperty("Host", requestHeaderMap.get("host"));
        con.setRequestProperty("Date", requestHeaderMap.get("date"));
        con.setRequestProperty("Digest", requestHeaderMap.get("digest"));
        con.setRequestProperty("Authorization", signature);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }


        print_https_cert(con);

        print_content(con);

        in.close();
        con.disconnect();
    }

    private void print_https_cert(HttpsURLConnection con) throws IOException {

        if (con != null) {
            System.out.println("Headers:");
            con.getHeaderFields().forEach((k, v) -> {
                System.out.println(String.format("%s: %s", k, v));
            });

            System.out.println(String.format("Host: %s", con.getURL().getHost()));
            System.out.println(String.format("Path: %s", con.getURL().getPath()));
            System.out.println(String.format("Request method: %s", con.getRequestMethod()));
            System.out.println(String.format("Response code: %s", con.getResponseCode()));
            System.out.println(String.format("Cipher suite: %s", con.getCipherSuite()));
            System.out.println("\n");

            Certificate[] certs = con.getServerCertificates();

            for (Certificate cert : certs) {
                System.out.println(String.format("Cert type: %s", cert.getType()));
                System.out.println(String.format("Cert hash code: %s", cert.hashCode()));
                System.out.println(String.format("Cert Public Key Algorithm: %s", cert.getPublicKey().getAlgorithm()));
                System.out.println(String.format("Cert Public Key Format : %s", cert.getPublicKey().getFormat()));
                System.out.println("\n");
            }
        }


    }


    private void print_content(HttpsURLConnection con) {
        if (con != null) {

            try {

                System.out.println("****** Content of the URL ********");
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null) {
                    System.out.println(input);
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


}


