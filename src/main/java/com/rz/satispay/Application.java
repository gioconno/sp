package com.rz.satispay;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class Application {


    public static void main(String[] args) {
        RequestBuilder.getWithoutSignature();
        RequestBuilder.getWithSignature();

    }


    private void getWithSignature(Map<String, String> requestHeaderMap, String signature) throws IOException {

        System.out.println("----------------< GET WITH SIGNATURE >----------------");

        URL url = new URL(String.format("%s://%s%s", Constants.REQUEST_PROTOCOL.getValue(), Constants.REQUEST_HOST.getValue(), Constants.REQUEST_PATH.getValue()));

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod(HTTPMethod.GET.toString());
        con.setRequestProperty("Host", requestHeaderMap.get("host"));
        con.setRequestProperty("Date", requestHeaderMap.get("date"));
        con.setRequestProperty("Digest", requestHeaderMap.get("digest"));
        con.setRequestProperty("Authorization", signature);

//        con.setRequestProperty("Host", "staging.authservices.satispay.com");
//        con.setRequestProperty("Date", "2020-01-07T07:19:18.314Z");
//        con.setRequestProperty("Digest","SHA-256=47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=" );
//        con.setRequestProperty("Authorization", "Signature keyId=\"signature-test-66289\", algorithm=\"rsa-sha256\", headers=\"(request-target) host date digest\", signature=\"Slsa2tr4sRt2pOConVJXcHwhzD6N7LD3AyguCyRz+4g7IwbDVoMJJK1BBr42zkoj/QBwCQ7CRy5ajLGKhgzN/IHNr3rvwgMFtaMVSKzmQFtU8pjy+qHJelBxuxy//WD9GF6TUxNyJ0ydi+PZHL63W9i4yaZmn2wHVgTPGS249RYFMUO59XYIjtk9hJlWCvxwNerPNSpkhOzi/wSxApv0qxxyf0FDDRSJ2eqq9C+rsNzAPENHPawGjnfTZOVb77WUuQgFCe6TxqXIRVpWGXCl3Apgbasjv4K+mIB+JpZD/+i5JKGurM+d+rFSJ80OkQUWKgKtjLqX2jIrHqCa0ei5vw==\"");

//        con.setRequestProperty("Host", "staging.authservices.satispay.com");
//        con.setRequestProperty("Date", "2020-01-12T17:32:24.013Z");
//        con.setRequestProperty("Digest","SHA-256=47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=" );
//        con.setRequestProperty("Authorization", "Signature keyId=\"signature-test-66289\", algorithm=\"rsa-sha256\", headers=\"(request-target) host date digest\", signature=\"CRJ5zYvH6FBBbN82ek2iLXh7BA4WwBhyYsHjj4xylZP/toZV9R+YvqdeKOS4+qi3bZOencgy9GEuZxdhbFeiwVlpuM1GOkkHepzAYSJQqiaA/Lom41UtS6CPw6GSBAFuQGq0tlweQ/3PtQY34L7Trt94KdIBE6S9NpCz+y/PYMJsMOYOXpnlo+4THVsAky3kO2mTEuklwqrZP65h2znTXy1HxawQ8XYUzcGXhQvAw2zcHwFH1mOdqXMYRBQhamCY5ZHlkKoQNsRyr99uPCRTIlBn69K5X+lW+dfUnYMCsQtAJYGaF/4HUZpi5OsNcxLd0ozQDfyTHwFhikUOMF5neg==\"");


        con.setDoOutput(true);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        System.out.println(response);


        PrintOutputUtils.print_https_cert(con);

        PrintOutputUtils.print_content(con);

        in.close();
        con.disconnect();
    }


}


