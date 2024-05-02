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

/**
 * LegalEntityConverter class is used to convert VerifiableCredential<LegalEntityCredentialSubject> to W3CVC format.
 */
public class LegalEntityConverter {

    public W3CVC convertToW3CVC(VerifiableCredential<LegalEntityCredentialSubject> legalEntityVerifiableCredential) throws JsonProcessingException, JSONException {

        VerifiableCredentialBuilder credentialBuilder = new VerifiableCredentialBuilder();


        // Add context and type
        credentialBuilder.addContext("https://www.w3.org/2018/credentials/v1" );
        credentialBuilder.addType("VerifiableCredential");
        credentialBuilder.addType("EBSILegalEntityVerifiableID ");
        credentialBuilder.setIssuerDid("did:key:z4MXj1wBzi9jUstyQ9Be6tS8NnnnVfLFUbMTRmpmB8xb56XLEzhHLNUgiDSVWqZtmcambEfswNGimSwxr59jW9zLFfQjGkaXH6RghxFJxdbXrBrCA7cgh6MYF7XQw5G1ktAAY2ff7oZo2CxHBVtK5sL4WyEFivMxqtHJXCPoscVVNKgYLBuAY19Vvx45uY9GsiaLYYzFyMC9LGpc2AyuWeTrec1uqZVN9ebAZ36spoPf3AxH9yF8wtpsGkRNA5yiCQfnhskSHdQ1MyfZ9fHzxaKWvMwbgsQu1qrEQ7Rj2BLmW4pbkhJAcmBUC4D4riZPySGqeD1ieZ2okpNrD7ccuk5jdYGf7XKvxX2StGe7r5hTxKvtNhSyW");
        credentialBuilder.setCredentialId("urn:uuid:"+ legalEntityVerifiableCredential.getId());
//        credentialBuilder.setSubjectDid(legalEntityVerifiableCredential.getCredentialSubject().getId());
        credentialBuilder.setSubjectDid("did:web:your-web-domain:your-web-path#wA-pJvAXGG8HlBDb1B2tPsv9V35i4LBa4XoMoPbRAqU");
        //
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
        jsonObject.put("EORI", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getEORI() + "\""));
        jsonObject.put("SEED", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getSEED() + "\""));
        jsonObject.put("SIC", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getSIC() + "\""));
        jsonObject.put("domainName", json.parseToJsonElement("\"" + legalEntityVerifiableCredential.getCredentialSubject().getDomainName()+ "\""));

        String issuerKeyJsonString = "{\"type\":\"jwk\",\"jwk\":{\"kty\":\"OKP\",\"d\":\"mDhpwaH6JYSrD2Bq7Cs-pzmsjlLj4EOhxyI-9DM1mFI\",\"crv\":\"Ed25519\",\"kid\":\"Vzx7l5fh56F3Pf9aR3DECU5BwfrY6ZJe05aiWYWzan8\",\"x\":\"T3T4-u1Xz3vAV2JwPNxWfs4pik_JLiArz_WTCvrCFUM\"}}";
        jsonObject.put("issuerKey", json.parseToJsonElement(issuerKeyJsonString));
        credentialBuilder.credentialSubject(jsonObject);

        W3CVC credential = credentialBuilder.buildCredential();
        return credential;
    }


}
