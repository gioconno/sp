package com.rz.satispay;

import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Set;


public class HeadersBuilder {

    // FIXME enum
    private final static String REQUEST_TARGET = "(request-target)";
    private final static String HOST = "host";
    private final static String DATE = "date";
    private final static String DIGEST = "digest";

    // Get requests have no body
    static Set<HeaderField> buildGetHeaders() {
        return buildRequest(HTTPMethod.GET, null);
    }

    static Set<HeaderField> buildPostHeaders(String body) {
        return buildRequest(HTTPMethod.POST, body);
    }

    static Set<HeaderField> buildPutHeaders(String body) {
        return buildRequest(HTTPMethod.PUT, body);
    }


    private static Set<HeaderField> buildRequest(HTTPMethod httpMethod, String body) {
        try {
            String digest;
            if (body == null) {
                digest = DigestBuilder.getBodyDigest();
            } else {
                digest = DigestBuilder.getBodyDigest(body);
            }
            return buildRequestSetFromDigest(digest, httpMethod);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            throw new IllegalStateException(String.format("Error(s) while creating headers for request %s: " + e.getMessage(), httpMethod.toString()), e);
        }
    }

    private static Set<HeaderField> buildRequestSetFromDigest(String digest, HTTPMethod httpMethod) {
        Set<HeaderField> headersSet = new LinkedHashSet<>();
        headersSet.add(new HeaderField(REQUEST_TARGET, httpMethod.toString().toLowerCase()
                + " "
                + Constants.REQUEST_PATH.getValue().toLowerCase()));
        headersSet.add(new HeaderField(HOST, Constants.REQUEST_HOST.getValue().toLowerCase()));
        headersSet.add(new HeaderField(DATE, ZonedDateTime.now().toInstant().toString()));
        headersSet.add(new HeaderField(DIGEST, digest));

        return headersSet;
    }

}


