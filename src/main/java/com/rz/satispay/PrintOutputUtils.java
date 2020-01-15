package com.rz.satispay;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.Certificate;

public class PrintOutputUtils {

    static void printConnection(HttpsURLConnection connection) {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println(response);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void print_https_cert(HttpsURLConnection con) throws IOException {

        if (con != null) {
            System.out.println("Headers:");
            con.getHeaderFields().forEach((k, v) -> System.out.println(String.format("%s: %s", k, v)));

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


    static void print_content(HttpsURLConnection con) {
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
