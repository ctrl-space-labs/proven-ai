package dev.ctrlspace.provenai.ssi.issuer;

import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.credentials.PresentationBuilder;
import id.walt.crypto.keys.Key;
import id.walt.did.helpers.WaltidServices;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonPrimitive;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;

/**
 * Builder class for creating verifiable presentations and signing them.
 */
public class VerifiablePresentationBuilder {


    private PresentationBuilder presentationBuilder;

    private Continuation<Unit> continuationPlain;

    private Continuation<? super Object> continuationSuper;




    public VerifiablePresentationBuilder() {
        this.continuationPlain = ContinuationObjectUtils.createPlainContinuation();
        this.continuationSuper = ContinuationObjectUtils.createSuperContinuation();
        WaltidServices.INSTANCE.minimalInit(continuationPlain);
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
    public void addCredential(JsonPrimitive credential) {
        presentationBuilder.addCredential(credential);
    }

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

    public Object buildAndSign(Key subjectKey, JsonElement presentationJson) {
        // Build the verifiable presentation JSON

        // Convert the JSON element to a string
        String presentationJsonString = presentationJson.toString();

        Continuation<? super Object> continuationSuper = ContinuationObjectUtils.createSuperContinuation();


        // Sign the JSON string using the subject key
        Object signedPresentation =  subjectKey.signJws(
                presentationJsonString.getBytes(StandardCharsets.UTF_8),
                new HashMap<>(),
                continuationSuper
        );

        return signedPresentation;
    }
}