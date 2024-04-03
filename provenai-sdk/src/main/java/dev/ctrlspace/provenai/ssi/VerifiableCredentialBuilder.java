package dev.ctrlspace.provenai.ssi;


import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import dev.ctrlspace.provenai.utils.KotlinToJavaUtils;
import id.walt.credentials.CredentialBuilder;
import id.walt.credentials.CredentialBuilderType;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.crypto.keys.Key;
import id.walt.did.helpers.WaltidServices;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;

public class VerifiableCredentialBuilder {

    private CredentialBuilder credentialBuilder;
    private Continuation<Unit> continuationPlain;

    public VerifiableCredentialBuilder() {
        this.continuationPlain = ContinuationObjectUtils.createPlainContinuation();
        WaltidServices.INSTANCE.minimalInit(continuationPlain);
        // Initialize the credential builder with default settings
        credentialBuilder = new CredentialBuilder(CredentialBuilderType.W3CV11CredentialBuilder);
    }

    public void addContext(String context) {
        credentialBuilder.addContext(context);
    }

    /**
     * Adds a type to the verifiable credential.
     *
     * @param type The type to be added. Supported types:
     */
    public void addType(String type) {
        credentialBuilder.addType(type);
    }

    public void setCredentialId(String credentialId) {
        credentialBuilder.setCredentialId(credentialId);
    }

    public void setIssuerDid(String issuerDid) {
        credentialBuilder.setIssuerDid(issuerDid);
    }

    public void validFromNow() {
        credentialBuilder.validFromNow();
    }


    public void validFor(Duration duration) {
        Instant validUntil = Instant.now().plus(duration);
        credentialBuilder.setValidUntil(KotlinToJavaUtils.toKotlinInstant(validUntil));
    }

    public void validUntil(Instant validUntil) {
        credentialBuilder.setValidUntil(KotlinToJavaUtils.toKotlinInstant(validUntil));
    }


    public void useData(String key, JsonElement value) {
        credentialBuilder.useData(key, value);
    }


    public void credentialSubject(JsonObject data) {
        credentialBuilder.useCredentialSubject(data);
    }


    public void setSubjectDid(String subjectDid) {
        credentialBuilder.setSubjectDid(subjectDid);
    }

    // Build and return the verifiable credential
    public W3CVC buildCredential() {
        return credentialBuilder.buildW3C();
    }

    public String signCredential(
            Key issuerKey,
            String issuerDid,
            String subjectDid,
            Continuation<? super Object> continuation,
            Map<String, String> additionalJwtHeaders,
            Map<String, JsonElement> additionalJwtOptions
    ) {
        W3CVC vc = buildCredential(); // Build the Verifiable Credential

        // Convert additionalJwtOptions to the format expected by the signJws method
        Map<String, JsonElement> jsonElementAdditionalJwtOptions = new HashMap<>(additionalJwtOptions);

        return (String) vc.signJws(issuerKey, issuerDid, subjectDid, additionalJwtHeaders, jsonElementAdditionalJwtOptions, continuation);
    }
}




