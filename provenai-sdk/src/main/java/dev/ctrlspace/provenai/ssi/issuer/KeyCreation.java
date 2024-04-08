package dev.ctrlspace.provenai.ssi.issuer;

import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.LocalKey;
import id.walt.crypto.keys.LocalKeyMetadata;
import kotlin.coroutines.Continuation;

public class KeyCreation {

    public static LocalKey generateKey(KeyType keyType, int characterLength) {
        // Create a continuation object
        Continuation<? super Object> continuation = ContinuationObjectUtils.createSuperContinuation();

        // Create an instance of LocalKeyMetadata
        LocalKeyMetadata metadata = new LocalKeyMetadata(characterLength);

        // Generate a key using the generate function from the companion object
        return (LocalKey) LocalKey.Companion.generate(keyType, metadata, continuation);
    }
}
