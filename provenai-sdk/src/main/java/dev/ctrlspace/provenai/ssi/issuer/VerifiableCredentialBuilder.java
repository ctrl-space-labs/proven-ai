package dev.ctrlspace.provenai.ssi.issuer;


import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import dev.ctrlspace.provenai.utils.KotlinToJavaUtils;
import id.walt.credentials.CredentialBuilder;
import id.walt.credentials.CredentialBuilderType;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.crypto.keys.Key;
import id.walt.did.helpers.WaltidServices;
import kotlin.Pair;
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

    private Continuation<? super Object> continuationSuper;


    public VerifiableCredentialBuilder() {
        this.continuationPlain = ContinuationObjectUtils.createPlainContinuation();
        this.continuationSuper = ContinuationObjectUtils.createSuperContinuation();
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



public void useData(Pair<String, JsonElement> keyValue) {
    credentialBuilder.useData(keyValue);
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

    public Object signCredential(
            W3CVC vc,
            Key issuerKey,
            String issuerDid,
            String subjectDid,
            Map<String, String> additionalJwtHeaders,
            Map<String, JsonElement> additionalJwtOptions
    ) {

        // Convert additionalJwtOptions to the format expected by the signJws method
        Map<String, JsonElement> jsonElementAdditionalJwtOptions = new HashMap<>(additionalJwtOptions);

        Continuation<? super Object> continuationSuper = ContinuationObjectUtils.createSuperContinuation();

        return vc.signJws(issuerKey, issuerDid, subjectDid,
                                    additionalJwtHeaders, jsonElementAdditionalJwtOptions, continuationSuper);
    }
}




