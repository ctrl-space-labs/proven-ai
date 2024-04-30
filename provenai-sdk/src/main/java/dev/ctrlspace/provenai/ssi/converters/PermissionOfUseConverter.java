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
            jsonObject.put("id", json.parseToJsonElement("\"" + permissionOfUseCredential.getCredentialSubject().getId() + "\""));
//            jsonObject.put("ownerID", json.parseToJsonElement("\"" + permissionOfUseCredential.getCredentialSubject().getOwnerID() + "\""));
            jsonObject.put("dataSegments", json.parseToJsonElement(objectMapper.writeValueAsString(permissionOfUseCredential.getCredentialSubject().getDataSegments())));

            credentialBuilder.credentialSubject(jsonObject);


            // Set validity duration
            Duration duration = Duration.ofDays(30); // Example: 30 days validity period
            credentialBuilder.validFor(duration);

            // Build the credential
            W3CVC credential = credentialBuilder.buildCredential();
            return credential;
        }







}
