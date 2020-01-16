package com.rz.satispay;

import java.util.Set;

/**
 * <p>
 * Provides a @{@link Set} of {@link HeaderField}.
 * </p>
 * <p>
 * Each set contains several (for now fixed) headers (@see HeadersBuilder):
 *     <ul>
 *         <li>request-target</li>
 *         <li>host</li>
 *         <li>date</li>
 *         <li>digets</li>
 *         <li>authorization</li>
 *         <li>content-type (optional)</li>
 *     </ul>
 * </p>
 */
public class AuthHeaderBuilder {

    public static Set<HeaderField> buildGetHeader() {
        Set<HeaderField> headers = HeadersBuilder.buildGetHeaders();
        headers.add(new HeaderField("authorization", createAuthString(headers)));

        return headers;
    }

    public static Set<HeaderField> buildPostHeader(String body) {

        Set<HeaderField> headers = HeadersBuilder.buildPostHeaders(body);
        headers.add(new HeaderField("authorization", createAuthString(headers)));
        headers.add(new HeaderField("Content-Type", "application/json"));

        return headers;
    }

    public static Set<HeaderField> buildPutHeader(String body) {

        Set<HeaderField> headers = HeadersBuilder.buildPutHeaders(body);
        headers.add(new HeaderField("authorization", createAuthString(headers)));
        headers.add(new HeaderField("Content-Type", "application/json"));

        return headers;
    }

    public static Set<HeaderField> buildDeleteHeader() {

        Set<HeaderField> headers = HeadersBuilder.buildDeleteHeaders();
        headers.add(new HeaderField("authorization", createAuthString(headers)));

        return headers;
    }


    private static String createAuthString(Set<HeaderField> headers) {
        StringBuilder sb = new StringBuilder();

        // Convert each header in a string formatted as <header_name>: <header_value>
        // E.g. (request-target): get ...
        headers.forEach(headerField -> sb.append(headerField.toString()).append("\n"));

        String signature = SignatureBuilder.createSignature(sb.toString(), Constants.PRIVATE_KEY_FILE_NAME.getValue());

        StringBuilder headersStringBuilder = new StringBuilder("headers=\"");
        headers.forEach(headerField -> headersStringBuilder.append(headerField.getHeader()).append(" "));

        return "Signature keyId=\""
                + Constants.KEY_ID.getValue()
                + "\", algorithm=\""
                + Constants.SIGN_ALG.getValue() + "\", "
                + headersStringBuilder.toString().trim()
                + "\", "
                + "signature=\""
                + signature
                + "\"";
    }

}


