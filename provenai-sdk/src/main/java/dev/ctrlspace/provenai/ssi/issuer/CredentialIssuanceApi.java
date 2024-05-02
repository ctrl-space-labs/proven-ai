package dev.ctrlspace.provenai.ssi.issuer;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This class is responsible for issuing signed verifiable credentials.
 * It sends a request to the Credential Issuance API of Walt.id to issue a signed credential.
 * The API returns the credential offer url that can be imported to a web wallet app to be validated.
 */
public class CredentialIssuanceApi {
    public static final RestTemplate restTemplate = new RestTemplate();

    public String issueCredential(JsonNode requestBody) {
        String apiUrl = "http://localhost:7002/openid4vc/jwt/issue";

        HttpHeaders headers = buildHeaders();

        HttpEntity<JsonNode> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return responseEntity.getBody();
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
