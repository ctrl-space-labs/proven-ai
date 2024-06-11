package dev.ctrlspace.provenai.ssi.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.ctrlspace.provenai.ssi.issuer.VerifiableCredentialBuilder;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.PermissionOfUseCredentialSubject;
import id.walt.credentials.vc.vcs.W3CVC;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import org.json.JSONException;

import java.time.Duration;
import java.util.Map;

public class PermissionOfUseConverter {
    private ObjectMapper objectMapper;

    public PermissionOfUseConverter() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public PermissionOfUseConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public W3CVC convertToW3CVC(VerifiableCredential<PermissionOfUseCredentialSubject> permissionOfUseCredential) throws JsonProcessingException, JSONException {
        VerifiableCredentialBuilder credentialBuilder = new VerifiableCredentialBuilder();

        // Add context and type
        credentialBuilder.addContext("https://www.w3.org/2018/credentials/v1");
        credentialBuilder.addType("PermissionOfUseCredential");

        // Set credential ID, issuer DID, and subject DID
        credentialBuilder.setCredentialId("urn:uuid:" + permissionOfUseCredential.getId());
        credentialBuilder.setIssuerDid("\"did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQ0ZRLU5yYTV5bnlCc2Z4d3k3YU5mOGR1QUVVQ01sTUlyUklyRGc2REl5NCIsIngiOiJoNW5idzZYOUptSTBCdnVRNU0wSlhmek84czJlRWJQZFYyOXdzSFRMOXBrIn0\"");
        credentialBuilder.setSubjectDid(permissionOfUseCredential.getCredentialSubject().getId());


        Json json = Json.Default;

        String jsonObjectString = objectMapper.writeValueAsString(permissionOfUseCredential.getCredentialSubject());
        JsonElement jsonElement = json.parseToJsonElement(jsonObjectString);
        Map<String, JsonElement> map = Map.of("permissionOfUse", jsonElement);
        JsonObject jsonObject = new JsonObject(map);

        credentialBuilder.credentialSubject(jsonObject);


        // Set validity duration
        Duration duration = Duration.ofDays(30); // Example: 30 days validity period
        credentialBuilder.validFor(duration);

        // Build the credential
        W3CVC credential = credentialBuilder.buildCredential();
        return credential;
    }
}
