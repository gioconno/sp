package com.rz.satispay;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;


public class RequestBuilder {

    private final static String REQUEST_URL = String.format("%s://%s/%s", Constants.REQUEST_PROTOCOL.getValue(),
            Constants.REQUEST_HOST.getValue(), Constants.REQUEST_PATH.getValue());

    static void doGetWithoutSignature() {

        System.out.println("----------------< GET WITHOUT SIGNATURE >----------------");

        execute(HTTPMethod.GET.toString());
    }

    static void doGetWithSignature() {
        System.out.println("----------------< GET WITH SIGNATURE >----------------");

        execute(HTTPMethod.GET.toString(), AuthHeaderBuilder.buildGetHeader(), null);
    }

    static void doPost(String body) {
        System.out.println("----------------< POST >----------------");

        execute(HTTPMethod.POST.toString(), AuthHeaderBuilder.buildPostHeader(body), body);

    }

    static void doPut(String body) {
        System.out.println("----------------< PUT >----------------");

        execute(HTTPMethod.PUT.toString(), AuthHeaderBuilder.buildPutHeader(body), body);

    }

    static void doDelete(String body) {
        System.out.println("----------------< DELETE >----------------");

        execute(HTTPMethod.DELETE.toString(), AuthHeaderBuilder.buildDeleteHeader(), null);

    }

    private static void execute(String requestMethod) {
        execute(requestMethod, Collections.emptySet(), null);
    }

    private static void execute(String requestMethod, Set<HeaderField> headers, String body) {
        HttpsURLConnection con = null;
        try {
            URL url = new URL(REQUEST_URL);

            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod(requestMethod);

            for (HeaderField headerField : headers) {
                con.setRequestProperty(headerField.getHeader(), headerField.getValue());
            }

            if (body != null) {
                con.setDoOutput(true);

                try (DataOutputStream dos = new DataOutputStream(con.getOutputStream())) {
                    dos.writeBytes(body);
                    dos.flush();
                }

            }

            PrintOutputUtils.printConnection(con);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con.disconnect();
        }
    }


}


