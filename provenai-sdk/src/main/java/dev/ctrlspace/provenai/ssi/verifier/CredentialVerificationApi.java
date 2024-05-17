package dev.ctrlspace.provenai.ssi.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import dev.ctrlspace.provenai.utils.SSIConstants;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class CredentialVerificationApi {

    public static final RestTemplate restTemplate = new RestTemplate();


    public String verifyCredential(JsonNode requestBody) {

        HttpHeaders headers = buildHeaders(SSIConstants.VERIFIER_SUCCESS_URL, SSIConstants.VERIFIER_ERROR_URL);

        HttpEntity<JsonNode> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                SSIConstants.WALT_ID_VERIFIER_API,
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


