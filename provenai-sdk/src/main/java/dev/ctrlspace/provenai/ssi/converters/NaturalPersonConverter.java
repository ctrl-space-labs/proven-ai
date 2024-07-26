package dev.ctrlspace.provenai.ssi.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.ssi.issuer.VerifiableCredentialBuilder;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.PermissionOfUseCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.id.NaturalPersonCredentialSubject;
import id.walt.credentials.vc.vcs.W3CVC;
import kotlin.Pair;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import org.json.JSONException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * NaturalPersonConverter class is used to convert VerifiableCredential<NaturalPersonCredentialSubject> to W3CVC format.
 */
public class NaturalPersonConverter {
    public W3CVC convertToW3CVC(VerifiableCredential<NaturalPersonCredentialSubject> naturalPersonVerifiableCredential) throws JsonProcessingException, JSONException {

        VerifiableCredentialBuilder credentialBuilder = new VerifiableCredentialBuilder();

        // Add context and type
        credentialBuilder.addContext("https://www.w3.org/2018/credentials/v1");
        credentialBuilder.addType("VerifiableCredential;");
        credentialBuilder.setIssuerDid("did:key:_proven-ai-issuer_");
        credentialBuilder.setCredentialId("urn:uuid:"+ naturalPersonVerifiableCredential.getId());
        credentialBuilder.setSubjectDid(naturalPersonVerifiableCredential.getCredentialSubject().getId());
        // Set validity duration
        Duration duration = Duration.ofDays(30); // Example: 30 days validity period

        credentialBuilder.validFor(duration);
        Json json = Json.Default;

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObjectString = objectMapper.writeValueAsString(naturalPersonVerifiableCredential.getCredentialSubject());
        JsonElement jsonElement = json.parseToJsonElement(jsonObjectString);
        Map<String, JsonElement> map = Map.of("naturalPerson", jsonElement);
        JsonObject jsonObject = new JsonObject(map);
        jsonObject.put("familyName", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getFamilyName() + "\""));
        jsonObject.put("firstName", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getFirstName() + "\""));
        jsonObject.put("familyNameAtBirth", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getFamilyNameAtBirth() + "\""));
        jsonObject.put("firstNameAtBirth", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getFirstNameAtBirth()+ "\""));
        jsonObject.put("dateOfBirth", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getDateOfBirth() + "\""));
        jsonObject.put("yearOfBirth", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getYearOfBirth() + "\""));
        jsonObject.put("ageOverNN", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().isAgeOverNN() + "\""));
        jsonObject.put("ageInYears", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getAgeInYears() + "\""));
        jsonObject.put("personalIdentifier", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getPersonalIdentifier() + "\""));
        jsonObject.put("placeOfBirth", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getPlaceOfBirth() + "\""));
        jsonObject.put("currentAddress", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getCurrentAddress() + "\""));
        jsonObject.put("gender", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getGender() + "\""));
        jsonObject.put("nationality", json.parseToJsonElement("\"" + naturalPersonVerifiableCredential.getCredentialSubject().getNationality() + "\""));

        credentialBuilder.credentialSubject(jsonObject);

        W3CVC credential = credentialBuilder.buildCredential();
        return credential;
    }
}
