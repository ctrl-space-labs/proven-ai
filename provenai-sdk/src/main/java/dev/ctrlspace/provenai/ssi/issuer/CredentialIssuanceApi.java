package dev.ctrlspace.provenai.ssi.issuer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.ctrlspace.provenai.ssi.model.dto.WaltIdCredentialIssuanceRequest;
import dev.ctrlspace.provenai.utils.SSIConstants;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This class is responsible for issuing signed verifiable credentials.
 * It sends a request to, an OID4VC compliant, Credential Issuance API to issue a signed credential.
 * The API returns the credential offer url that can be imported to a web wallet app to be validated.
 */
public class CredentialIssuanceApi {
    public static final RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper objectMapper;

    public CredentialIssuanceApi() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public CredentialIssuanceApi(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a request to, an OID4VC compliant, Credential Issuance API to issue a signed credential.
     * The API returns the credential offer url that can be imported to a web wallet app to be validated.
     *
     * @param requestBody The request body containing the credential subject and other parameters.
     * @return The credential offer url.
     */
    public String issueCredential(JsonNode requestBody) {

        HttpHeaders headers = buildHeaders();

        HttpEntity<JsonNode> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                SSIConstants.WALT_ID_ISSUER_API,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return responseEntity.getBody();
    }

    public String issueCredential(WaltIdCredentialIssuanceRequest credentialIssuanceRequest) {

        credentialIssuanceRequest.setCredentialConfigurationId("");
        JsonNode requestBody = objectMapper.valueToTree(credentialIssuanceRequest);
        return issueCredential(requestBody);


    }


    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
