package dev.ctrlspace.provenai.ssi.issuer;

import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.crypto.keys.JwkKeyMeta;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.jwk.JWKKey;
import kotlin.coroutines.Continuation;

public class KeyCreation {

    /**
     * Method to generate a key
     * @param keyType algorithm to produce cryptographic key
     * @param characterLength length of characters the key
     * @return JWKKey
     */

    /**
     * Generate a key pair with a specific key type and character length
     *
     * @param keyType The type of key to generate (enum Ed25519, secp256k1, secp256r1, RSA)
     * @param characterLength The length of the key in characters, should be greater than 2048
     * @return The generated key
     */
    public static JWKKey generateKey(KeyType keyType, int characterLength) {

        // Create an instance of LocalKeyMetadata
        JwkKeyMeta metadata = new JwkKeyMeta("JWKKey",characterLength);
        // Generate a key using the generate function from the companion object
        return  JWKKey.Companion.generateBlocking(keyType,metadata);
    }

}
