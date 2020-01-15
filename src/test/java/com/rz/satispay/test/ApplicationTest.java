//package com.rz.satispay.test;
//
//import io.restassured.RestAssured;
//import org.junit.Test;
//
//public class ApplicationTest {
//
//    @Test
//    public void test() {
//        RestAssured.given()
//                .log().all()
//                .header("Host", "staging.authservices.satispay.com")
//                .header("Date", "2020-01-07T07:19:18.314Z")
//                .header("Digest", "SHA-256=47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=")
//                .header("Authorization", "Signature keyId=\"signature-test-66289\", algorithm=\"rsa-sha256\", headers=\"(request-target) host date digest\", signature=\"Slsa2tr4sRt2pOConVJXcHwhzD6N7LD3AyguCyRz+4g7IwbDVoMJJK1BBr42zkoj/QBwCQ7CRy5ajLGKhgzN/IHNr3rvwgMFtaMVSKzmQFtU8pjy+qHJelBxuxy//WD9GF6TUxNyJ0ydi+PZHL63W9i4yaZmn2wHVgTPGS249RYFMUO59XYIjtk9hJlWCvxwNerPNSpkhOzi/wSxApv0qxxyf0FDDRSJ2eqq9C+rsNzAPENHPawGjnfTZOVb77WUuQgFCe6TxqXIRVpWGXCl3Apgbasjv4K+mIB+JpZD/+i5JKGurM+d+rFSJ80OkQUWKgKtjLqX2jIrHqCa0ei5vw==\"")
//                .when()
//                .get("https://staging.authservices.satispay.com/wally-services/protocol/tests/signature")
//                .then()
//                .log().all();
//
//    }
//
//}
