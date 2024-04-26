package dev.ctrlspace.provenai.ssi.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.ssi.issuer.VerifiableCredentialBuilder;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.PermissionOfUseCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.W3CCredentialSubject;
import id.walt.credentials.vc.vcs.W3CVC;
import kotlin.Pair;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import org.json.JSONException;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

public class PermissionOfUseConverter {


        public W3CVC convertToW3CVC(VerifiableCredential<PermissionOfUseCredentialSubject> permissionOfUseCredential) throws JsonProcessingException, JSONException {
            VerifiableCredentialBuilder credentialBuilder = new VerifiableCredentialBuilder();

            // Add context and type
            credentialBuilder.addContext("https://www.w3.org/2018/credentials/v1");
            credentialBuilder.addType("PermissionOfUse");

            // Set credential ID, issuer DID, and subject DID
            credentialBuilder.setCredentialId("urn:uuid:"+ permissionOfUseCredential.getId());
            credentialBuilder.setIssuerDid("did:key:_proven-ai-issuer_");
            credentialBuilder.setSubjectDid(permissionOfUseCredential.getCredentialSubject().getId());

            Json json = Json.Default;

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonObjectString = objectMapper.writeValueAsString(permissionOfUseCredential.getCredentialSubject());
            JsonElement jsonElement = json.parseToJsonElement(jsonObjectString);
            Map<String, JsonElement> map = Map.of("permissionOfUse", jsonElement);
            JsonObject jsonObject = new JsonObject(map);

            credentialBuilder.credentialSubject(jsonObject);


            // Create JsonElements for each field
            JsonElement agentIdElement = json.parseToJsonElement("\"" + permissionOfUseCredential.getCredentialSubject().getId() + "\"");
            JsonElement ownerIdElement = json.parseToJsonElement("\"" + permissionOfUseCredential.getCredentialSubject().getOwnerID() + "\"");
            JsonElement dataSegmentsElement = json.parseToJsonElement(objectMapper.writeValueAsString(permissionOfUseCredential.getCredentialSubject().getDataSegments()));

            // Create Pairs
            Pair<String, JsonElement> agentId = new Pair<>("id", agentIdElement);
            Pair<String, JsonElement> ownerId = new Pair<>("ownerID", ownerIdElement);
            Pair<String, JsonElement> dataSegments= new Pair<>("dataSegments", dataSegmentsElement);

            // Add each field to useData
            credentialBuilder.useData(agentId);
            credentialBuilder.useData(ownerId);
            credentialBuilder.useData(dataSegments);

            // Set validity duration
            Duration duration = Duration.ofDays(30); // Example: 30 days validity period
            credentialBuilder.validFor(duration);

            // Build the credential
            W3CVC credential = credentialBuilder.buildCredential();
            return credential;
        }


        public VerifiableCredential<PermissionOfUseCredentialSubject> convertToPermissionOfUseVerifiableCredential(W3CCredentialSubject w3CCredentialSubject){
            VerifiableCredential<PermissionOfUseCredentialSubject> permissionOfUseCredential = new VerifiableCredential<>();

            permissionOfUseCredential.setContext(w3CCredentialSubject.getContext());
            permissionOfUseCredential.setId(w3CCredentialSubject.getId());
            permissionOfUseCredential.setType(w3CCredentialSubject.getType());
            permissionOfUseCredential.setIssuer(w3CCredentialSubject.getIssuerDid());
            permissionOfUseCredential.setValidFrom(Date.from(w3CCredentialSubject.getValidFrom()));
            permissionOfUseCredential.setValidUntil(Date.from(w3CCredentialSubject.getValidUntil()));
            return permissionOfUseCredential;
        }




}
