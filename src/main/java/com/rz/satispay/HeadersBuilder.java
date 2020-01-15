package com.rz.satispay;

import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Set;


public class HeadersBuilder {

    private final static String REQUEST_TARGET = "(request-target)";
    private final static String HOST = "host";
    private final static String DATE = "date";
    private final static String DIGEST = "digest";

    // Get requests have no body
    static Set<HeaderField> buildGetHeaders() {
        try {
            String digest = DigestBuilder.getBodyDigest();
            return buildGetRequestSetFromDigest(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            throw new IllegalStateException("Error(s) while creating headers: " + e.getMessage(), e);
        }
    }


    static Set<HeaderField> buildGetRequestSetFromDigest(String digest) {
        Set<HeaderField> headersSet = new LinkedHashSet<>();
        headersSet.add(new HeaderField(REQUEST_TARGET, HTTPMethod.GET.toString().toLowerCase()
                + " "
                + Constants.REQUEST_PATH.getValue().toLowerCase()));
        headersSet.add(new HeaderField(HOST, Constants.REQUEST_HOST.getValue().toLowerCase()));
        headersSet.add(new HeaderField(DATE, ZonedDateTime.now().toInstant().toString()));
        headersSet.add(new HeaderField(DIGEST, digest));

        return headersSet;
    }

}


