package dev.ctrlspace.provenai.ssi.issuer;


import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.LocalKey;
import kotlin.coroutines.Continuation;

public class LocalKeyWrapper {
    Continuation<? super Object> continuation = ContinuationObjectUtils.createSuperContinuation();

    public String exportJWK(LocalKey localKey) {
        return (String) localKey.exportJWK(continuation);
    }

    public String exportJWKObject(LocalKey localKey) {
        String jwk = (String) localKey.exportJWK(continuation);
        return jwk;
    }

//        I WANT OT USE THE OTHER METHODS OF KEY ALSO


    public String getPublicKey(LocalKey localKey) {
        String publicKey = (String) localKey.getPublicKey(continuation);
        return publicKey;
    }

    public String getKeyId(LocalKey localKey) {
        String keyId = (String) localKey.getKeyId(continuation);
        return keyId;
    }

    public KeyType getKeyType(LocalKey localKey) {
        KeyType keyType = localKey.getKeyType();
        return keyType;
    }

    public String getJwk(LocalKey localKey) {
        String jwk = localKey.getJwk();
        return jwk;
    }

    public Object exportPem(LocalKey localKey) {
        Object pem = localKey.exportPEM(continuation);
        return pem;
    }

    public Object getPublicKeyRepresentation(LocalKey localKey) {
        Object publicKeyRepresentation = localKey.getPublicKeyRepresentation(continuation);
        return publicKeyRepresentation;
    }

    public Object getThumbprint(LocalKey localKey) {
        Object keyThumbprint = localKey.getThumbprint(continuation);
        return keyThumbprint;
    }

    public Object Boolean (LocalKey localKey) {
        Boolean HasPrivateKey = localKey.getHasPrivateKey();
        return HasPrivateKey;
    }

}
