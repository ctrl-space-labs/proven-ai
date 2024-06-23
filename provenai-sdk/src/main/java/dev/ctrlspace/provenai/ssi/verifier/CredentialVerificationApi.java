package dev.ctrlspace.provenai.ssi.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import dev.ctrlspace.provenai.utils.SSIConstants;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public class CredentialVerificationApi {

    public static final RestTemplate restTemplate = new RestTemplate();

    public String verifyCredential(JsonNode requestBody, String successRedirect, String errorRedirect) {
        HttpHeaders headers = buildHeaders(successRedirect, errorRedirect);
        HttpEntity<JsonNode> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                SSIConstants.WALT_ID_VERIFIER_API,
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

