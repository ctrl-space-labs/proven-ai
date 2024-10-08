package dev.ctrlspace.provenai.ssi.issuer;

import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.credentials.PresentationBuilder;
import id.walt.crypto.keys.Key;
import id.walt.did.helpers.WaltidServices;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonPrimitive;

import java.util.Collection;
import java.util.UUID;


/**
 * Builder class for creating verifiable presentations and signing them.
 */
public class VerifiablePresentationBuilder {


    private PresentationBuilder presentationBuilder;






    public VerifiablePresentationBuilder() {
        WaltidServices.INSTANCE.initBlocking();
        // Initialize the presentation builder with default settings
        presentationBuilder = new PresentationBuilder();
    }

    /**
     * Adds subject did to the verifiable presentation.
     * @param did
     */
    public void setDid(String did) {
        presentationBuilder.setDid(did);
    }

    /**
     * Sets the presentation id to a UUID.
     */

    public void setPresentationId() {
        presentationBuilder.setPresentationId("urn:uuid:" + UUID.randomUUID().toString());
    }


    /**
     * Sets the nonce of the verifiable presentation.
     * @param nonce
     */
    public void setNonce(String nonce) {
        presentationBuilder.setNonce(nonce);
    }

    /**
     * Adds a verifiable credential to the verifiable presentation.
     * @param credential
     */
    public void addCredential(JsonPrimitive credential) {presentationBuilder.addCredential(credential);}
    /**
     * Adds multiple verifiable credentials to the verifiable presentation.
     * @param credentials
     */
    public void addCredentials(Collection<? extends JsonPrimitive> credentials) {
        presentationBuilder.addCredentials(credentials);
    }

    /**
     * Builds the verifiable presentation JSON.
     * @return The verifiable presentation JSON.
     */
    public JsonElement buildPresentationJson() {
        return presentationBuilder.buildPresentationJson();
    }

    /**
     * Builds the verifiable presentation JSON and signs it with the given key.
     * @param subjectKey The key to sign the verifiable presentation with.
     * @return The signed verifiable presentation JSON.
     */
    public Object buildAndSign(Key subjectKey) {
        // Call the buildAndSign method on the presentationBuilder instance
        return presentationBuilder.buildAndSignBlocking(subjectKey);
    }





    }
