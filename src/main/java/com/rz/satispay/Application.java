package com.rz.satispay;

public class Application {


    public static void main(String[] args) {
        RequestBuilder.doGetWithoutSignature();
        RequestBuilder.doGetWithSignature();

        String body = "{\n" +
                "  \"flow\": \"MATCH_CODE\",\n" +
                "  \"amount_unit\": 100,\n" +
                "  \"currency\": \"EUR\"\n" +
                "}";

        RequestBuilder.doPost(body);
        RequestBuilder.doPut(body);
        RequestBuilder.doDelete(body);
    }
}


