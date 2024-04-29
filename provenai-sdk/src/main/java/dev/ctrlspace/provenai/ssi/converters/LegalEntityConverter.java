package dev.ctrlspace.provenai.ssi.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.ssi.issuer.VerifiableCredentialBuilder;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.id.LegalEntityCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.id.NaturalPersonCredentialSubject;
import id.walt.credentials.vc.vcs.W3CVC;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import org.json.JSONException;

import java.time.Duration;
import java.util.Map;

public class LegalEntityConverter {

    public W3CVC convertToW3CVC(VerifiableCredential<LegalEntityCredentialSubject> legalEntityVerifiableCredential) throws JsonProcessingException, JSONException {

        VerifiableCredentialBuilder credentialBuilder = new VerifiableCredentialBuilder();

        // Add context and type
        credentialBuilder.addContext("https://www.w3.org/2018/credentials/v1");
        credentialBuilder.addType("VerifiableCredential;");
        credentialBuilder.setIssuerDid("did:key:_proven-ai-issuer_");
        credentialBuilder.setCredentialId("urn:uuid:"+ legalEntityVerifiableCredential.getId());
        credentialBuilder.setSubjectDid(legalEntityVerifiableCredential.getCredentialSubject().getId());
        // Set validity duration
        Duration duration = Duration.ofDays(30); // Example: 30 days validity period

        credentialBuilder.validFor(duration);
        Json json = Json.Default;

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObjectString = objectMapper.writeValueAsString(legalEntityVerifiableCredential.getCredentialSubject());
        JsonElement jsonElement = json.parseToJsonElement(jsonObjectString);
        Map<String, JsonElement> map = Map.of("LegalEntity", jsonElement);
        JsonObject jsonObject = new JsonObject(map);
        jsonObject.put("legalPersonIdentifier", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getLegalPersonIdentifier() + "\""));
        jsonObject.put("legalName", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getLegalName() + "\""));
        jsonObject.put("legalAddress", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getLegalAddress() + "\""));
        jsonObject.put("VATRegistration", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getVATRegistration() + "\""));
        jsonObject.put("taxReference", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getTaxReference() + "\""));
        jsonObject.put("LEI", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getLEI() + "\""));
        jsonObject.put("EORI", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getLegalAddress() + "\""));
        jsonObject.put("SEED", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getSEED() + "\""));
        jsonObject.put("SIC", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getSIC() + "\""));
        jsonObject.put("domainName", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getDomainName()+ "\""));


        credentialBuilder.credentialSubject(jsonObject);

        W3CVC credential = credentialBuilder.buildCredential();
        return credential;
    }


}
