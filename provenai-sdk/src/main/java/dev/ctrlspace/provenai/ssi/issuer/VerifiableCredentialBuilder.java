package dev.ctrlspace.provenai.ssi.issuer;


import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import dev.ctrlspace.provenai.utils.KotlinToJavaUtils;
import dev.ctrlspace.provenai.utils.WaltIdServiceInitUtils;
import id.walt.credentials.CredentialBuilder;
import id.walt.credentials.CredentialBuilderType;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.crypto.keys.Key;
import kotlin.Pair;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * Builder class for creating verifiable credentials according to the W3C standard and signing them.
 */
public class VerifiableCredentialBuilder {

    private CredentialBuilder credentialBuilder;


    public VerifiableCredentialBuilder() {
        WaltIdServiceInitUtils.INSTANCE.initializeWaltIdServices();
        // Initialize the credential builder with default settings
        credentialBuilder = new CredentialBuilder(CredentialBuilderType.W3CV11CredentialBuilder);
    }

    /**
     * Adds a context to the verifiable credential.
     * Default context is "https://www.w3.org/2018/credentials/v1".
     * @param context The context to be added.
     */
    public void addContext(String context) {
        credentialBuilder.addContext(context);
    }

    /**
     * Adds a type to the verifiable credential.
     *
     * @param type The type to be added. Supported types:  "VerifiableCredential", "VerifiableAttestation",
     *                                                     "VerifiableAgentID", "VerifiableAgentID",
     *                                                     "EBSILegalEntityVerifiableID ","VerifiableId"
     *
     */

    public void addType(String type) {
        credentialBuilder.addType(type);
    }

    public void setCredentialId(String credentialId) {
        credentialBuilder.setCredentialId(credentialId);
    }

    /**
     * Sets the issuer of the verifiable credential.
     * @param issuerDid The issuer DID.
     */
    public void setIssuerDid(String issuerDid) {
        credentialBuilder.setIssuerDid(issuerDid);
    }

    public void validFromNow() {
        credentialBuilder.validFromNow();
    }

  /**
     * Sets the validity duration of the verifiable credential.
     * @param duration The duration of the validity.
     */
    public void validFor(Duration duration) {
        Instant validUntil = Instant.now().plus(duration);
        credentialBuilder.setValidUntil(KotlinToJavaUtils.toKotlinInstant(validUntil));
    }

    public void validUntil(Instant validUntil) {
        credentialBuilder.setValidUntil(KotlinToJavaUtils.toKotlinInstant(validUntil));
    }


/**
     * Adds extra data about the credential.
     * @param keyValue The key-value pair to be added.
     */
    public void useData(Pair<String, JsonElement> keyValue) {
        credentialBuilder.useData(keyValue);
    }

    /**
     * Adds the credential subject to the verifiable credential.
     * According to the credential type it contains data in json format about the subject of the credential.
     * @param data JsonOnject
     */
    public void credentialSubject(JsonObject data) {
        credentialBuilder.useCredentialSubject(data);
    }

 /**
     * Sets the subject DID of the verifiable credential.
     * @param subjectDid The subject DID.
     */
    public void setSubjectDid(String subjectDid) {
        credentialBuilder.setSubjectDid(subjectDid);
    }

    /**
     * Builds the verifiable credential according to the W3C standard.
     *
     * @return The verifiable credential.
     */

    public W3CVC buildCredential() {
        return credentialBuilder.buildW3C();
    }

    /**
     * Signs the verifiable credential with the issuer key.
     *
     * @param vc The verifiable credential to be signed.
     * @param issuerKey The issuer key.
     * @param issuerDid The issuer DID.
     * @param subjectDid The subject DID.
     * @param additionalJwtHeaders Additional JWT headers.
     * @param additionalJwtOptions Additional JWT options.
     * @return The signed verifiable credential in JWS format.
     */
    public Object signCredential(
            W3CVC vc,
            Key issuerKey,
            String issuerDid,
            String subjectDid,
            Map<String, String> additionalJwtHeaders,
            Map<String, JsonElement> additionalJwtOptions
    ) {

        return vc.signJwsBlocking(issuerKey, issuerDid, subjectDid, subjectDid, additionalJwtHeaders, additionalJwtOptions);
    }
}




