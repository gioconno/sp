package com.rz.satispay;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;


public class RequestBuilder {

    private final static String REQUEST_URL = String.format("%s://%s/%s", Constants.REQUEST_PROTOCOL.getValue(),
            Constants.REQUEST_HOST.getValue(), Constants.REQUEST_PATH.getValue());

    static void getWithoutSignature() {

        System.out.println("----------------< GET WITHOUT SIGNATURE >----------------");

        execute(HTTPMethod.GET.toString());
    }

    static void getWithSignature() {
        System.out.println("----------------< GET WITH SIGNATURE >----------------");

        execute(HTTPMethod.GET.toString(), AuthHeaderBuilder.build());
    }

    private static void execute(String requestMethod) {
        execute(requestMethod, Collections.emptySet());
    }

    private static void execute(String requestMethod, Set<HeaderField> headers) {
        HttpsURLConnection con = null;
        try {
            URL url = new URL(REQUEST_URL);

            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod(requestMethod);

            for (HeaderField headerField : headers) {
                con.setRequestProperty(headerField.getHeader(), headerField.getValue());
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


