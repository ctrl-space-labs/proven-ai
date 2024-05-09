package dev.ctrlspace.provenai.ssi.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class CredentialVerificationApi {

    public static final RestTemplate restTemplate = new RestTemplate();


    public String verifyCredential(JsonNode requestBody) {
        // TODO read from properties
        String apiUrl = "http://localhost:7002/openid4vc/verify";

        HttpHeaders headers = buildHeaders("http://localhost:7002/openid4vc/verify", "http://localhost:7002/openid4vc/verify");

        HttpEntity<JsonNode> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return responseEntity.getBody();
    }




    public HttpHeaders buildHeaders(String successRedirectUri, String errorRedirectUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("authorizeBaseUrl", authorizeBaseUrl);
//        headers.set("responseMode", responseMode);
        headers.set("successRedirectUri", successRedirectUri);
        headers.set("errorRedirectUri", errorRedirectUri);
        return headers;

    }
}


