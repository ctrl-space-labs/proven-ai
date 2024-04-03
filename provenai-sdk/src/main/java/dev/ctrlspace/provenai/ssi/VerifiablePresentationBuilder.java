package dev.ctrlspace.provenai.ssi;

import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.credentials.PresentationBuilder;
import id.walt.crypto.keys.Key;
import id.walt.did.helpers.WaltidServices;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.serialization.json.JsonElement;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;

public class VerifiablePresentationBuilder {


    private PresentationBuilder presentationBuilder;

    private Continuation<Unit> continuationPlain;




    public VerifiablePresentationBuilder() {
        this.continuationPlain = ContinuationObjectUtils.createPlainContinuation();
        WaltidServices.INSTANCE.minimalInit(continuationPlain);
        // Initialize the presentation builder with default settings
        presentationBuilder = new PresentationBuilder();
    }

    public void setDid(String did) {
        presentationBuilder.setDid(did);
    }

    public void setNonce(String nonce) {
        presentationBuilder.setNonce(nonce);
    }

    public void addCredential(JsonElement credential) {
        presentationBuilder.addCredential(credential);
    }

    public void addCredentials(Collection<? extends JsonElement> credentials) {
        presentationBuilder.addCredentials(credentials);
    }

    // Method to build a verifiable presentation
    public JsonElement buildPresentationJson() {
        return presentationBuilder.buildPresentationJson();
    }

    public String buildAndSign(Key subjectKey, Continuation<? super Object> continuation) {
        // Build the verifiable presentation JSON
        JsonElement presentationJson = buildPresentationJson();

        // Convert the JSON element to a string
        String presentationJsonString = presentationJson.toString();

        // Sign the JSON string using the subject key
        String signedPresentation = (String) subjectKey.signJws(
                presentationJsonString.getBytes(StandardCharsets.UTF_8),
                new HashMap<>(),
                continuation
        );

        return signedPresentation;
    }
}