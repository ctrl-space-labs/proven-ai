package dev.ctrlspace.provenai.ssi.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public class CredentialVerificationApi {

    private String waltIdVerifierApi;

    public static final RestTemplate restTemplate = new RestTemplate();

    public CredentialVerificationApi(String waltIdVerifierApi) {
        this.waltIdVerifierApi = waltIdVerifierApi;
    }



    public String verifyCredential(JsonNode requestBody, String successRedirect, String errorRedirect) {
        HttpHeaders headers = buildHeaders(successRedirect, errorRedirect);
        HttpEntity<JsonNode> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                waltIdVerifierApi,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return responseEntity.getBody();
    }

    public HttpHeaders buildHeaders(String successRedirect, String errorRedirect) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "*/*");
        headers.set("authorizeBaseUrl", "openid4vp://authorize");
        headers.set("responseMode", "direct_post");
        headers.set("successRedirectUri", successRedirect);
        headers.set("errorRedirectUri", errorRedirect);

        // Debugging
        System.out.println("Headers: " + headers);

        return headers;
    }
}

