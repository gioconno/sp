package com.rz.satispay;

import java.util.Set;


public class AuthHeaderBuilder {

    public static Set<HeaderField> build() {

        Set<HeaderField> headers = HeadersBuilder.buildGetHeaders();
        headers.add(new HeaderField("authorization", createAuthString(headers)));

        return headers;
    }

    private static String createAuthString(Set<HeaderField> headers) {
        StringBuilder sb = new StringBuilder();
        headers.forEach(headerField -> sb.append(headerField.toString()).append("\n"));

        String privateKey = "C:\\Users\\a43n\\Progetti\\Satispay\\src\\main\\resources\\client-rsa-private-key.pem";
        String signature = SignatureBuilder.createSignature(sb.toString(), privateKey);

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


