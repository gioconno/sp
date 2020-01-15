package com.rz.satispay;

public enum Constants {
    REQUEST_PROTOCOL("https"),
    REQUEST_HOST("staging.authservices.satispay.com"),
    REQUEST_PATH("/wally-services/protocol/tests/signature"),
    ENCRYPT_ALG("SHA-256"),
    SIGN_ALG("rsa-sha256"),
    PRIVATE_KEY_FILE_NAME("client-rsa-private-key.pem"),
    KEY_ID("signature-test-66289");

    private String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
