package dev.ctrlspace.provenai.ssi.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.ctrlspace.provenai.configuration.SSIConfig;
import dev.ctrlspace.provenai.ssi.issuer.VerifiableCredentialBuilder;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.id.LegalEntityCredentialSubject;
import id.walt.credentials.vc.vcs.W3CVC;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import org.json.JSONException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LegalEntityConverter {

    private ObjectMapper objectMapper;
    String issuerDid = SSIConfig.getIssuerDid();


    public LegalEntityConverter() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public LegalEntityConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public W3CVC convertToW3CVC(VerifiableCredential<LegalEntityCredentialSubject> legalEntityCredential) throws JsonProcessingException, JSONException {
        VerifiableCredentialBuilder credentialBuilder = new VerifiableCredentialBuilder();

        // Add context and type
        credentialBuilder.addContext("https://www.w3.org/2018/credentials/v1");
        credentialBuilder.addType("VerifiableCredential");
        credentialBuilder.addType("VerifiableLegalEntity");

        // Set credential ID, issuer DID, and subject DID
        credentialBuilder.setCredentialId("urn:uuid:" + UUID.randomUUID());
        credentialBuilder.setIssuerDid(issuerDid);
        credentialBuilder.setSubjectDid(legalEntityCredential.getCredentialSubject().getId());
        Json json = Json.Default;

        String jsonObjectString = objectMapper.writeValueAsString(legalEntityCredential.getCredentialSubject());
        JsonElement jsonElement = json.parseToJsonElement(jsonObjectString);
        Map<String, JsonElement> map = Map.of("legalEntity", jsonElement);
        JsonObject jsonObject = new JsonObject(map);

        credentialBuilder.credentialSubject(jsonObject);

        // Set validity duration
        Duration duration = Duration.ofDays(365); // Example: 365 days validity period
        credentialBuilder.validFor(duration);

        // Build the credential
        W3CVC credential = credentialBuilder.buildCredential();
        return credential;
    }
}


