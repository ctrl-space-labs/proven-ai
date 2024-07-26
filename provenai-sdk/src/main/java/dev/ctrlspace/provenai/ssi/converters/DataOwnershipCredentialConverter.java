package dev.ctrlspace.provenai.ssi.converters;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.ctrlspace.provenai.configuration.SSIConfig;
import dev.ctrlspace.provenai.ssi.issuer.VerifiableCredentialBuilder;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.DataOwnershipCredentialSubject;
import id.walt.credentials.vc.vcs.W3CVC;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.module.Configuration;
import java.time.Duration;
import java.util.UUID;

public class DataOwnershipCredentialConverter {

    private ObjectMapper objectMapper;

    String issuerDid = SSIConfig.getIssuerDid();


    public DataOwnershipCredentialConverter() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
//        this.issuerDid = Con
    }

    public DataOwnershipCredentialConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public W3CVC convertToW3CVC(VerifiableCredential<DataOwnershipCredentialSubject> dataOwnershipCredential) throws JsonProcessingException, JSONException {
        VerifiableCredentialBuilder credentialBuilder = new VerifiableCredentialBuilder();

        // Add context and type
        credentialBuilder.addContext("https://www.w3.org/2018/credentials/v1");
        credentialBuilder.addType("VerifiableDataOwnershipCredential");

        // Set credential ID, issuer DID, and subject DID
        credentialBuilder.setCredentialId("urn:uuid:" + UUID.randomUUID().toString());
        credentialBuilder.setIssuerDid(issuerDid);
        credentialBuilder.setSubjectDid(dataOwnershipCredential.getCredentialSubject().getId());

        Json json = Json.Default;

        String jsonObjectString = objectMapper.writeValueAsString(dataOwnershipCredential.getCredentialSubject());
        JsonObject jsonObject = (JsonObject)json.parseToJsonElement(jsonObjectString);

        credentialBuilder.credentialSubject(jsonObject);

        // Set validity duration
        Duration duration = Duration.ofDays(30); // Example: 30 days validity period
        credentialBuilder.validFor(duration);

        // Build the credential
        return credentialBuilder.buildCredential();
    }
}
